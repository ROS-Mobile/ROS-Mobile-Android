package com.schneewittchen.rosandroid.ui.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 24.01.20
 * @updated on 02.04.20
 * @modified by
 */
public class WidgetDetailListAdapter extends RecyclerView.Adapter<BaseDetailViewHolder> implements DetailListener{

    public List<BaseEntity> widgetList;
    private DetailListener detailListener;


    public WidgetDetailListAdapter() {
        widgetList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        System.err.println("Get ViewHolder for: " + position);

        BaseEntity entity = widgetList.get(position);
        Class<? extends BaseDetailViewHolder> viewHolderClazz = entity.getDetailViewHolderType();
        
        try {
            Constructor<? extends BaseDetailViewHolder> cons  = viewHolderClazz
                                                    .getConstructor(View.class, DetailListener.class);
            LayoutInflater inflator = LayoutInflater.from(parent.getContext());
            View itemView = inflator.inflate(R.layout.widget_detail_item, parent, false);

            return cons.newInstance(itemView, detailListener);

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

        BaseEntity entity = widgetList.get(position);

        LayoutInflater inflator = LayoutInflater.from(holder.itemView.getContext());
        holder.detailContend.removeView(holder.detailContend.getChildAt(1));

        int detailContentLayout = entity.getWidgetDetailViewId();
        inflator.inflate(detailContentLayout, holder.detailContend, true);

        holder.update(widgetList.get(position));
    }

    @Override
    public int getItemCount() {
        return widgetList.size();
    }



    public void setWidgets(List<BaseEntity> newWidgets){
        // TODO: Implement Diff callback with widgets
        // WidgetDiffCallback diffCallback = new WidgetDiffCallback(this.widgetList, newWidgets);
        // DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);

        // diffResult.dispatchUpdatesTo(this);
        // TODO: Change that to DiffUtil, but does not work yet with diffUtil
        //  ("https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in")
        notifyDataSetChanged();
    }

    public void setChangeListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    @Override
    public void onDetailsChanged(BaseEntity widgetId) {
        if(detailListener != null) {
            this.detailListener.onDetailsChanged(widgetId);
        }
    }
}