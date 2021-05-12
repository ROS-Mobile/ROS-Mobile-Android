package com.schneewittchen.rosandroid.ui.fragments.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.03.21
 */
public class WidgetListAdapter extends RecyclerView.Adapter<WidgetListAdapter.ViewHolder> {

    public static String TAG = WidgetDetailListAdapter.class.getSimpleName();

    private List<BaseEntity> currentWidgets;
    private Comparator<BaseEntity> compareByTime;
    private WidgetClickListener clickListener;


    public WidgetListAdapter(WidgetClickListener clickListener) {
        this.clickListener = clickListener;
        this.currentWidgets = new ArrayList<>();

        this.compareByTime = (BaseEntity o1, BaseEntity o2) ->
                Long.compare(o2.creationTime, o1.creationTime);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_widget_card_list, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseEntity widget = currentWidgets.get(position);
        holder.setEntity(widget);

        holder.getCard().setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(widget);
            }
        });
    }


    @Override
    public int getItemCount() {
        return currentWidgets.size();
    }


    public void setWidgets(List<BaseEntity> newWidgets) {
        currentWidgets.clear();
        currentWidgets.addAll(newWidgets);
        Collections.sort(currentWidgets, compareByTime);

        this.notifyDataSetChanged();
    }

    public BaseEntity getItem(int index) {
        return currentWidgets.get(index);
    }


    public interface WidgetClickListener {
        void onClick(BaseEntity entity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView typeTextView;
        public View viewBackground, viewForeground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.widget_name_textview);
            typeTextView = itemView.findViewById(R.id.widget_type_textview);
            viewBackground  = itemView.findViewById(R.id.view_background);
            viewForeground  = itemView.findViewById(R.id.view_foreground);
        }

        public CardView getCard() {
            return (CardView)this.itemView;
        }

        public void setEntity(BaseEntity entity) {
            String formatText = itemView.getResources().getString(R.string.widget_list_type);
            String typeText = String.format(formatText, entity.type);

            nameTextView.setText(entity.name);
            typeTextView.setText(typeText);
        }
    }
}
