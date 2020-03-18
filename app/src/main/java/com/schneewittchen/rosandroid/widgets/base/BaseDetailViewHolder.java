package com.schneewittchen.rosandroid.widgets.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.02.20
 * @updated on 13.02.20
 * @modified by
 */
public class BaseDetailViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private View detailContend;
    private ImageView openButton;
    private View viewBackground, viewForeground;
    private WidgetEntity entity;

    public BaseDetailViewHolder(@NonNull View view) {
        super(view);

        baseInit(view);
    }

    private void baseInit(View view) {
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

    public void update(WidgetEntity entity) {
        this.entity = entity;

        this.title.setText(entity.getName());
    }
}
