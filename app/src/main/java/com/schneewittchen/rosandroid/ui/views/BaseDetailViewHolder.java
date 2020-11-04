package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
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
 */
public abstract class BaseDetailViewHolder<T extends BaseEntity> extends RecyclerView.ViewHolder implements TextView.OnEditorActionListener {

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
        this.detailContend = view.findViewById(R.id.detailContend);
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
    }

    protected void baseBindEntity(T entity) {
        this.entity = entity;

        title.setText(entity.name);
        xEdittext.setText(String.valueOf(entity.posX));
        yEdittext.setText(String.valueOf(entity.posY));
        widthEditText.setText(String.valueOf(entity.width));
        heightEdittext.setText(String.valueOf(entity.height));
    }

    protected void baseUpdateEntity() {
        entity.name = title.getText().toString();
        entity.posX = Integer.parseInt(xEdittext.getText().toString());
        entity.posY = Integer.parseInt(yEdittext.getText().toString());
        entity.width = Integer.parseInt(widthEditText.getText().toString());
        entity.height = Integer.parseInt(heightEdittext.getText().toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                Utils.hideSoftKeyboard(itemView);
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
