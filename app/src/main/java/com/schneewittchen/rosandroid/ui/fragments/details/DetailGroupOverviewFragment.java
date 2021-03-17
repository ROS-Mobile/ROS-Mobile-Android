package com.schneewittchen.rosandroid.ui.fragments.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.03.21
 */
public class DetailGroupOverviewFragment extends Fragment
        implements RecyclerWidgetItemTouchHelper.TouchListener, WidgetChangeListener{

    public static String TAG = DetailGroupOverviewFragment.class.getSimpleName();

    public static DetailGroupOverviewFragment newInstance() {
        return new DetailGroupOverviewFragment();
    }


    private DetailsViewModel viewModel;
    private CardView addWidgetCard;
    private RecyclerView recyclerView;
    private WidgetListAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "On create view");
        return inflater.inflate(R.layout.fragment_detail_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        // Find views
        recyclerView = view.findViewById(R.id.recyclerview);
        addWidgetCard = view.findViewById(R.id.add_widget_card);

        // React on new widget clickaction
        addWidgetCard.setOnClickListener(v -> showDialogWithWidgetNames());

        // Setup recyclerview
        mAdapter = new WidgetListAdapter(this::onWidgetClicked);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.suppressLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback touchHelper = new RecyclerWidgetItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView);

        // Bind view model widgets etc.
        viewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), newWidgets -> {
            mAdapter.setWidgets(newWidgets);
        });
    }

    public void onWidgetClicked(BaseEntity entity) {
        Log.i(TAG, "Clicked " + entity.name);
    }

    private void showDialogWithWidgetNames() {
        if (getContext() == null) {
            return;
        }

        String[] widgetNames = getResources().getStringArray(R.array.widget_names);
        String[] widgetDescr = getResources().getStringArray(R.array.widget_descr);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle("Create Widget");

        dialogBuilder.setItems(widgetNames, (dialog, item) -> {
            AlertDialog.Builder dialogChecker = new AlertDialog.Builder(getContext());

            dialogChecker.setTitle(widgetNames[item]);
            dialogChecker.setMessage(widgetDescr[item]);

            dialogChecker.setPositiveButton("Create", (dialog1, which) -> {
                viewModel.createWidget(widgetNames[item]);
                Log.i(TAG, "Selected Text: " + widgetNames[item]);
            });

            dialogChecker.setNegativeButton("Cancel", null);

            AlertDialog dialogCheckerObject = dialogChecker.create();
            dialogCheckerObject.show();
        });

        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //Show the dialog
        alertDialogObject.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WidgetListAdapter.ViewHolder) {
            deleteWidget(viewHolder.getAdapterPosition());
        }
    }

    private void deleteWidget(int index) {
        // get the removed item name to display it in snack bar
        final BaseEntity deletedWidget = mAdapter.getItem(index);

        // remove the item from recycler view
        //mAdapter.removeItem(viewHolder.getAdapterPosition());
        viewModel.deleteWidget(deletedWidget);

        // showing snack bar with Undo option
        String undoText = getString(R.string.widget_undo, deletedWidget.name);
        Snackbar snackbar = Snackbar.make(this.getView(), undoText, Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", view -> {
            // undo is selected, restore the deleted item
            viewModel.restoreWidget();
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.color_attention));
        snackbar.show();
    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {
        viewModel.updateWidget(widgetEntity);
    }
}
