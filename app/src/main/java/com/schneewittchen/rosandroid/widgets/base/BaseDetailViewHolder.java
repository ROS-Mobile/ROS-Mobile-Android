package com.schneewittchen.rosandroid.widgets.base;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 13.02.20
 * @updated on 10.05.20
 * @modified by Nico Studt
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */
public abstract class BaseDetailViewHolder<T extends BaseWidget> extends RecyclerView.ViewHolder {

    public View viewBackground, viewForeground;
    public LinearLayout detailContend;
    protected TextView title;
    protected ImageView openButton;
    protected ImageButton updateButton;
    protected ImageButton renameButton;
    protected DetailListener updateListener;
    protected EditText xEdittext, yEdittext, widthEditText, heightEdittext;
    protected T widget;


    public BaseDetailViewHolder(@NonNull View view, DetailListener updateListener) {
        super(view);
        this.updateListener = updateListener;
        baseInit(view);
    }


    public abstract void init(View view);

    protected abstract void bind(T entity);

    protected abstract void updateEntity();


    public void baseInit(View view) {
        title = view.findViewById(R.id.title);
        detailContend = view.findViewById(R.id.detailContend);
        updateButton = view.findViewById(R.id.update_button);
        openButton = view.findViewById(R.id.open_button);
        renameButton = view.findViewById(R.id.rename_button);
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
            update();
        });
        updateButton.setEnabled(true);

        renameButton.setOnClickListener(v -> showRenameDialog());
    }

    public void baseBind(T entity) {
        this.widget = entity;

        title.setText(entity.name);
        xEdittext.setText(String.valueOf(entity.posX));
        yEdittext.setText(String.valueOf(entity.posY));
        widthEditText.setText(String.valueOf(entity.width));
        heightEdittext.setText(String.valueOf(entity.height));

        bind(entity);
    }

    protected void update() {
        baseUpdateEntity();
        updateEntity();

        updateListener.onDetailsChanged(widget);
    }

    private void baseUpdateEntity() {
        widget.name = title.getText().toString();
        widget.posX = Integer.parseInt(xEdittext.getText().toString());
        widget.posY = Integer.parseInt(yEdittext.getText().toString());
        widget.width = Integer.parseInt(widthEditText.getText().toString());
        widget.height = Integer.parseInt(heightEdittext.getText().toString());
    }

    private void showRenameDialog() {
        Context context = this.itemView.getContext();

        if (context == null)
            return;

        // Set up the input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog dialog =  new AlertDialog.Builder(context)
                .setTitle(R.string.rename_widget)
                .setView(input)
                .setPositiveButton(R.string.ok, (view, which) ->
                    rename(input.getText().toString()))
                .setNegativeButton(R.string.cancel, (view, which) -> view.cancel())
                .create();

        dialog.show();
    }

    public void rename(String newName) {
        newName = newName.trim();
        title.setText(newName);
    }

}
