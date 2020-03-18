package com.schneewittchen.rosandroid.ui.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.ui.fragments.WidgetDetailsFragment;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

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
    public BaseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseDetailViewHolder holder, int position) {
        holder.update(widgetList.get(position));
    }


    /*
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        widgetList.get(position).getDetailViewHolderType();
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());

        View itemView = inflator.inflate(R.layout.widget_detail_item, parent, false);

        int addLayoutId = getWidgetLayout(viewType);

        LinearLayout ll = itemView.findViewById(R.id.detailContend);
        inflator.inflate(addLayoutId, ll, true);

        return new MyViewHolder(itemView);
    }
    */

    private int getWidgetLayout(int viewType) {
        switch (viewType) {
            case WidgetEntity.JOYSTICK:
                return R.layout.widget_detail_joystick;
            case WidgetEntity.MAP:
                return R.layout.widget_detail_map;
        }

        return -1;
    }

    /*
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WidgetEntity widget = widgetList.get(position);

        holder.title.setText(widget.getName());
    }

    @Override
    public int getItemViewType(int position) {
        return widgetList.get(position).type;
    }*/

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

    public void setChangeListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    @Override
    public void onDetailsChanged(int widgetId) {
        if(detailListener != null) {
            this.detailListener.onDetailsChanged(widgetId);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public View detailContend;
        public ImageView openButton;
        public View viewBackground, viewForeground;


        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            detailContend = view.findViewById(R.id.detailContend);
            openButton = view.findViewById(R.id.open_button);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            openButton.setOnClickListener(v -> {
                if (detailContend.getVisibility() == View.GONE) {
                    detailContend.setVisibility(View.VISIBLE);
                    openButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
                }else{
                    detailContend.setVisibility(View.GONE);
                    openButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
                }
            });
        }
    }
}