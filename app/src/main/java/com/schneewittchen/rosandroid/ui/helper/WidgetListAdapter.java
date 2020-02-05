package com.schneewittchen.rosandroid.ui.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

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
public class WidgetListAdapter extends RecyclerView.Adapter<WidgetListAdapter.MyViewHolder> {

    public List<WidgetEntity> widgetList;


    public WidgetListAdapter() {
        widgetList = new ArrayList<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_detail_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WidgetEntity widget = widgetList.get(position);

        holder.type.setText(widget.getType());
        holder.description.setText("Description");

    }

    @Override
    public int getItemCount() {
        return widgetList.size();
    }

    public void setWidgets(List<WidgetEntity> newWidgets){
        // TODO: Implement Diff callback with widgets
        /*
        WidgetDiffCallback diffCallback = new WidgetDiffCallback(this.widgetList, newWidgets);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
        */


        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView type, description;
        public ImageButton openButton;
        public RelativeLayout viewBackground, viewForeground;


        public MyViewHolder(View view) {
            super(view);

            type = view.findViewById(R.id.title);
            openButton = view.findViewById(R.id.open_button);
            description = view.findViewById(R.id.description);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            System.out.println("Create");
            openButton.setOnClickListener(v -> {
                System.out.println("Click");
                int vis = description.getVisibility() == View.GONE? View.VISIBLE : View.GONE;
                description.setVisibility(vis);
            });
        }
    }
}