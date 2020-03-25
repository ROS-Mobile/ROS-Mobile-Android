package com.schneewittchen.rosandroid.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.ui.fragments.WidgetDetailsFragment;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.base.Position;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 24.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class WidgetDetailListAdapter extends RecyclerView.Adapter<BaseDetailViewHolder> implements DetailListener{

    public List<WidgetEntity> widgetList;
    private DetailListener detailListener;


    public WidgetDetailListAdapter() {
        widgetList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        System.err.println("Get Viewholer for: " + position);

        WidgetEntity entity = widgetList.get(position);
        Class<? extends BaseDetailViewHolder> viewHolderClazz = entity.getDetailViewHolderType();
        
        try {
            Constructor<? extends BaseDetailViewHolder> cons  = viewHolderClazz.getConstructor(View.class);
            int detailContentLayout = entity.getDetailViewLayoutId();

            LayoutInflater inflator = LayoutInflater.from(parent.getContext());
            View itemView = inflator.inflate(R.layout.widget_detail_item, parent, false);

            return cons.newInstance(itemView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseDetailViewHolder holder, int position) {
        System.err.println("Pos: " + position);

        WidgetEntity entity = widgetList.get(position);

        LayoutInflater inflator = LayoutInflater.from(holder.itemView.getContext());
        holder.detailContend.removeView(holder.detailContend.getChildAt(1));

        int detailContentLayout = entity.getDetailViewLayoutId();
        inflator.inflate(detailContentLayout, holder.detailContend, true);

        holder.update(widgetList.get(position));
    }

    @Override
    public int getItemCount() {
        return widgetList.size();
    }



    public void setWidgets(List<WidgetEntity> newWidgets){
        // TODO: Implement Diff callback with widgets
        WidgetDiffCallback diffCallback = new WidgetDiffCallback(this.widgetList, newWidgets);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setChangeListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    @Override
    public void onDetailsChanged(int widgetId) {
        if(detailListener != null) {
            this.detailListener.onDetailsChanged(widgetId);
        }
    }
}