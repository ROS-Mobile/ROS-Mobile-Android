package com.schneewittchen.rosandroid.ui.views.details;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.fragments.details.RecyclerWidgetItemTouchHelper;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetListAdapter;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.Arrays;
import java.util.Collections;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
public abstract class WidgetGroupViewHolder extends DetailViewHolder
        implements RecyclerWidgetItemTouchHelper.TouchListener{

    public static String TAG = WidgetGroupViewHolder.class.getSimpleName();

    private WidgetViewHolder widgetViewHolder;
    private RecyclerView recyclerView;
    private WidgetListAdapter mAdapter;
    private MaterialCardView addLayerCard;


    public WidgetGroupViewHolder() {
        this.widgetViewHolder = new WidgetViewHolder(this);
    }


    public void baseInitView(View view) {
        widgetViewHolder.baseInitView(view);

        recyclerView = view.findViewById(R.id.recyclerview);
        addLayerCard = view.findViewById(R.id.add_layer_card);

        // React on new widget click action
        addLayerCard.setOnClickListener(v -> showDialogWithLayerNames());

        // Setup recyclerview
        mAdapter = new WidgetListAdapter(this::onLayerClicked);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.suppressLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback touchHelper = new RecyclerWidgetItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView);
    }

    public void baseBindEntity(BaseEntity entity) {
        widgetViewHolder.baseBindEntity(entity);

        mAdapter.setWidgets(entity.childEntities);
    }

    public void baseUpdateEntity(BaseEntity entity) {
        widgetViewHolder.baseUpdateEntity(entity);
    }

    private void onLayerClicked(BaseEntity entity) {
        Log.i(TAG, "Clicked layer: " + entity.id);
        viewModel.select(entity.id);
        Navigation.findNavController(itemView)
                .navigate(R.id.action_depth1_to_depth2);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WidgetListAdapter.ViewHolder) {
            deleteWidget(viewHolder.getAdapterPosition());
        }
    }

    private void deleteWidget(int index) {
        Context context = this.itemView.getContext();

        if (context == null)
            return;

        // get the removed item name to display it in snack bar
        final BaseEntity deletedWidget = mAdapter.getItem(index);

        // remove the item from recycler view
        viewModel.deleteWidget(deletedWidget);

        // showing snack bar with Undo option
        String undoText = context.getString(R.string.widget_undo, deletedWidget.name);
        Snackbar snackbar = Snackbar.make(this.itemView, undoText, Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", view -> viewModel.restoreWidget());
        snackbar.setActionTextColor(context.getResources().getColor(R.color.color_attention));
        snackbar.show();
    }

    private void showDialogWithLayerNames() {
        Context context = this.itemView.getContext();
        if (context == null) {
            return;
        }

        String[] layerNames = context.getResources().getStringArray(R.array.layer_names);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Create Layer");

        dialogBuilder.setItems(layerNames, (dialog, item) -> {
            String title = layerNames[item];
            String descName = title + "_description";
            String description = Utils.getStringByName(context, descName);

            AlertDialog.Builder dialogChecker = new AlertDialog.Builder(context);
            dialogChecker.setTitle(title);
            dialogChecker.setMessage(description);

            dialogChecker.setPositiveButton("Create", (dialog1, which) ->
                viewModel.createWidget(title));

            dialogChecker.setNegativeButton("Cancel", null);

            AlertDialog dialogCheckerObject = dialogChecker.create();
            dialogCheckerObject.show();
        });

        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //Show the dialog
        alertDialogObject.show();
    }
}
