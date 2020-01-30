package com.schneewittchen.rosandroid.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroidlib.model.entities.Widget;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.01.20
 * @updated on 24.01.20
 * @modified by
 */
public class WidgetListAdapter extends RecyclerView.Adapter<WidgetListAdapter.MyViewHolder> {

    private List<Widget> widgetList;


    public WidgetListAdapter() {
        widgetList = new ArrayList<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Widget widget = widgetList.get(position);

        holder.type.setText(widget.getType());
        holder.description.setText("Description");

    }

    @Override
    public int getItemCount() {
        return widgetList.size();
    }

    public void setWidgets(List<Widget> widgets){
        this.widgetList = widgets;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        widgetList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Widget widget, int position) {
        widgetList.add(position, widget);
        // notify item added by position
        notifyItemInserted(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView type, description;
        public RelativeLayout viewBackground, viewForeground;


        public MyViewHolder(View view) {
            super(view);

            type = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }
}