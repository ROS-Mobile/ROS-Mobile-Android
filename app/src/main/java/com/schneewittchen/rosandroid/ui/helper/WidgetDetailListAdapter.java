package com.schneewittchen.rosandroid.ui.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 24.01.20
 * @updated on 15.04.20
 * @modified by Nico Studt
 * @updated on 25.09.20
 * @modified by Nico Studt
 */
public class WidgetDetailListAdapter extends RecyclerView.Adapter<BaseDetailViewHolder<?>> implements DetailListener{

    private DetailListener detailListener;
    private AsyncListDiffer<BaseWidget> mDiffer;
    private ArrayList<Class<? extends BaseDetailViewHolder<?>>> types;
    private DetailsViewModel mViewModel;


    public WidgetDetailListAdapter(DetailsViewModel viewModel) {
        this.mViewModel = viewModel;
        mDiffer = new AsyncListDiffer(this, diffCallback);
        types = new ArrayList<>();
    }


    @NonNull
    @Override
    public BaseDetailViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
        try {
            Class<? extends BaseDetailViewHolder<?>> viewHolderClazz = types.get(itemType);
            Constructor<? extends BaseDetailViewHolder<?>> cons  = viewHolderClazz.getConstructor(View.class, DetailListener.class);

            LayoutInflater inflator = LayoutInflater.from(parent.getContext());
            View itemView = inflator.inflate(R.layout.widget_detail_base, parent, false);

            BaseDetailViewHolder<?> baseDetailViewHolder = cons.newInstance(itemView, this);
            baseDetailViewHolder.setViewModel(mViewModel);

            return baseDetailViewHolder;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BaseDetailViewHolder holder, int position) {
        BaseWidget widget = this.getItem(position);

        LayoutInflater inflator = LayoutInflater.from(holder.itemView.getContext());
        holder.detailContend.removeView(holder.detailContend.getChildAt(1));

        // Get layout id
        String layoutStr = String.format(Constants.DETAIL_LAYOUT_FORMAT, widget.type.toLowerCase());
        int detailContentLayout = Utils.getResId(layoutStr, R.layout.class);
        //int detailContentLayout = entity.getWidgetDetailViewId();

        // Inflate layout
        View inflatedView = inflator.inflate(detailContentLayout, null);
        holder.detailContend.addView(inflatedView);

        // Bind to widget
        holder.init(holder.detailContend);
        holder.baseBind(widget);
    }

    @Override
    public int getItemViewType(int position) {
        BaseWidget entity = this.getItem(position);

        String classPath = BuildConfig.APPLICATION_ID
                + String.format(Constants.VIEWHOLDER_FORMAT, entity.type.toLowerCase(), entity.type);

        try {
            Class<?> clazzObject =  Class.forName(classPath);

            if (clazzObject.getSuperclass() != BaseDetailViewHolder.class) {
                return -1;
            } else if (types.contains(clazzObject)) {
                return types.indexOf(clazzObject);

            } else {
                types.add((Class<? extends BaseDetailViewHolder<?>>) clazzObject);
                return types.size() -1;
            }

        } catch (ClassNotFoundException e) {
            return -1;
        }

    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }


    public BaseWidget getItem(int position) {
        return mDiffer.getCurrentList().get(position);
    }

    public void setWidgets(List<BaseWidget> newWidgets){
        mDiffer.submitList(newWidgets);
    }

    public void setChangeListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }


    @Override
    public void onDetailsChanged(BaseWidget widget) {
        if(detailListener != null) {
            this.detailListener.onDetailsChanged(widget);
        }
    }


    private DiffUtil.ItemCallback<BaseWidget> diffCallback = new DiffUtil.ItemCallback<BaseWidget>() {
        @Override
        public boolean areItemsTheSame(BaseWidget oldItem, BaseWidget newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(BaseWidget oldItem, BaseWidget newItem) {
            return oldItem.equals(newItem);
        }
    };
}