package com.schneewittchen.rosandroid.widgets.base;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.ros.internal.node.response.Response;
import org.ros.master.client.TopicType;

import java.util.List;


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
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class BaseDetailViewHolder<T extends BaseEntity> extends RecyclerView.ViewHolder {

    public View viewBackground, viewForeground;
    public LinearLayout detailContend;
    protected TextView title;
    protected ImageView openButton;
    protected ImageButton renameButton;
    protected DetailListener updateListener;
    protected EditText xEdittext, yEdittext, widthEditText, heightEdittext;
    protected T entity;
    protected DetailsViewModel mViewModel;

    public BaseDetailViewHolder(@NonNull View view, DetailListener updateListener) {
        super(view);
        this.updateListener = updateListener;
        baseInit(view);
    }


    private void baseInit(View view) {
        title = view.findViewById(R.id.title);
        detailContend = view.findViewById(R.id.detailContend);
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

        xEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    entity.posX = Integer.parseInt(xEdittext.getText().toString());
                    update();
                } catch (Exception ignored) {
                }
            }
        });
        yEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    entity.posY = Integer.parseInt(yEdittext.getText().toString());
                    update();
                } catch (Exception ignored) {
                }
            }
        });
        widthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    entity.width = Integer.parseInt(widthEditText.getText().toString());
                    update();
                } catch (Exception ignored) {
                }
            }
        });
        heightEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    entity.height = Integer.parseInt(heightEdittext.getText().toString());
                    update();
                } catch (Exception ignored) {
                }
            }
        });

        renameButton.setOnClickListener(v -> showRenameDialog());
    }

    protected void update() {
        baseUpdateEntity();
        updateListener.onDetailsChanged(entity);
    }

    public void baseBind(T entity) {
        this.entity = entity;

        title.setText(entity.name);
        xEdittext.setText(String.valueOf(entity.posX));
        yEdittext.setText(String.valueOf(entity.posY));
        widthEditText.setText(String.valueOf(entity.width));
        heightEdittext.setText(String.valueOf(entity.height));

        bind(entity);
    }

    private void baseUpdateEntity() {
        entity.name = title.getText().toString();
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

    public void init(View view) {};

    protected void bind(T entity) {};

    protected void updateEntity() {};

    public void setViewModel(DetailsViewModel viewModel) {
        this.mViewModel = viewModel;
    }
}
