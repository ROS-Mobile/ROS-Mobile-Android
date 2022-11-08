package com.schneewittchen.rosandroid.ui.views.details;

import android.content.Context;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.IPositionEntity;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.general.Position;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;


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
 * @updated on 05.11.2020
 * @modified by Nico Studt
 */
public abstract class BaseDetailViewHolder<T extends BaseEntity> extends RecyclerView.ViewHolder
        implements TextView.OnEditorActionListener {

    public View viewBackground, viewForeground;
    public LinearLayout detailContend;
    protected TextView title;
    protected ImageView openButton;
    protected ImageButton updateButton;
    protected ImageButton renameButton;
    protected WidgetChangeListener changeListener;
    protected EditText xEdittext, yEdittext, widthEditText, heightEdittext;

    protected T entity;
    protected DetailsViewModel mViewModel;
    

    public BaseDetailViewHolder(@NonNull View view, WidgetChangeListener changeListener) {
        super(view);

        this.changeListener = changeListener;
        this.detailContend = view.findViewById(R.id.detailContent);
    }


    protected abstract void initView(View parentView);
    protected abstract void bindEntity(T entity);
    protected abstract void updateEntity();


    public void init() {
        baseInitView(this.itemView);
        initView(this.itemView);
    }

    public void bind(T entity) {
        baseBindEntity(entity);
        bindEntity(entity);
    }

    /**
     * Call this method internally to update the bound widget info
     * and subsequently force an update of the widget list.
     */
    protected void forceWidgetUpdate() {
        baseUpdateEntity();
        updateEntity();
        changeListener.onWidgetDetailsChanged(entity);
    }


    protected void baseInitView(View parentView) {
        title           = parentView.findViewById(R.id.title);
        updateButton    = parentView.findViewById(R.id.update_button);
        openButton      = parentView.findViewById(R.id.open_button);
        renameButton    = parentView.findViewById(R.id.rename_button);
        viewBackground  = parentView.findViewById(R.id.view_background);
        viewForeground  = parentView.findViewById(R.id.view_foreground);
        xEdittext       = parentView.findViewById(R.id.x_edit_text);
        yEdittext       = parentView.findViewById(R.id.y_edit_text);
        widthEditText   = parentView.findViewById(R.id.width_edit_text);
        heightEdittext  = parentView.findViewById(R.id.height_edit_text);

        xEdittext.setOnEditorActionListener(this);
        yEdittext.setOnEditorActionListener(this);
        widthEditText.setOnEditorActionListener(this);
        heightEdittext.setOnEditorActionListener(this);

        openButton.setOnClickListener(v -> {
            if (detailContend.getVisibility() == View.GONE) {
                detailContend.setVisibility(View.VISIBLE);
                openButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
            }else{
                detailContend.setVisibility(View.GONE);
                openButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
        });

        updateButton.setOnClickListener(v -> forceWidgetUpdate());
        updateButton.setEnabled(true);

        renameButton.setOnClickListener(v -> showRenameDialog());

        parentView.setOnClickListener(v -> parentView.requestFocus());
        parentView.setOnFocusChangeListener((v, hasFocus) -> Utils.hideSoftKeyboard(itemView));
    }

    protected void baseBindEntity(T entity) {
        this.entity = entity;

        title.setText(entity.name);

        Position position = ((IPositionEntity)entity).getPosition();

        xEdittext.setText(String.valueOf(position.x));
        yEdittext.setText(String.valueOf(position.y));
        widthEditText.setText(String.valueOf(position.width));
        heightEdittext.setText(String.valueOf(position.height));
    }

    protected void baseUpdateEntity() {
        entity.name = title.getText().toString();

        Position position = new Position();
        position.x = Integer.parseInt(xEdittext.getText().toString());
        position.y = Integer.parseInt(yEdittext.getText().toString());
        position.width = Integer.parseInt(widthEditText.getText().toString());
        position.height = Integer.parseInt(heightEdittext.getText().toString());

        ((IPositionEntity)entity).setPosition(position);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                itemView.requestFocus();
                return true;
        }

        return false;
    }

    private void showRenameDialog() {
        Context context = this.itemView.getContext();

        if (context == null)
            return;

        // Set up the input
        final EditText input = new EditText(context);
        input.setText(entity.name);
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

    public void setViewModel(DetailsViewModel viewModel) {
        this.mViewModel = viewModel;
    }

}
