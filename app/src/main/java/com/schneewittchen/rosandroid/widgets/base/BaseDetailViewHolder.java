package com.schneewittchen.rosandroid.widgets.base;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 13.02.20
 * @updated on 2.04.20
 * @modified by
 */
public class BaseDetailViewHolder<T extends BaseEntity> extends RecyclerView.ViewHolder {

    public View viewBackground, viewForeground;
    public LinearLayout detailContend;
    protected TextView title;
    protected ImageView openButton;
    protected ImageButton updateButton;
    protected T entity;
    DetailListener updateListener;
    EditText xEdittext, yEdittext, widthEditText, heightEdittext;


    public BaseDetailViewHolder(@NonNull View view, DetailListener updateListener) {
        super(view);
        this.updateListener = updateListener;
        baseInit(view);
    }


    private void baseInit(View view) {
        title = view.findViewById(R.id.title);
        detailContend = view.findViewById(R.id.detailContend);
        updateButton = view.findViewById(R.id.update_button);
        openButton = view.findViewById(R.id.open_button);
        viewBackground = view.findViewById(R.id.view_background);
        viewForeground = view.findViewById(R.id.view_foreground);
        xEdittext = view.findViewById(R.id.x_edit_text);
        yEdittext = view.findViewById(R.id.y_edit_text);
        widthEditText = view.findViewById(R.id.width_edit_text);
        heightEdittext = view.findViewById(R.id.height_edit_text);

        openButton.setOnClickListener(v -> {
            if (detailContend.getVisibility() == View.GONE) {
                detailContend.setVisibility(View.VISIBLE);
                openButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
            }else{
                detailContend.setVisibility(View.GONE);
                openButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
        });

        updateButton.setOnClickListener(v -> {
            entity.posX = Integer.parseInt(xEdittext.getText().toString());
            entity.posY = Integer.parseInt(yEdittext.getText().toString());
            entity.width = Integer.parseInt(widthEditText.getText().toString());
            entity.height = Integer.parseInt(heightEdittext.getText().toString());

            updateListener.onDetailsChanged(entity);
        });

        updateButton.setEnabled(true);
    }

    public void update(T entity) {
        this.entity = entity;
        this.title.setText(entity.getName());

        xEdittext.setText(String.valueOf(entity.posX));
        yEdittext.setText(String.valueOf(entity.posY));
        widthEditText.setText(String.valueOf(entity.width));
        heightEdittext.setText(String.valueOf(entity.height));
    }
}
