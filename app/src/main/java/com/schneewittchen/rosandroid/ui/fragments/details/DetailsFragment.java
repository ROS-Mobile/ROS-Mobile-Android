package com.schneewittchen.rosandroid.ui.fragments.details;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.2.5
 * @created on 10.01.20
 * @updated on 15.05.20
 * @modified by Nico Studt
 */
public class DetailsFragment extends Fragment implements RecyclerItemTouchHelper.TouchListener, WidgetChangeListener {

    static final String TAG = "WidgetDetailsFragment";

    private DetailsViewModel mViewModel;
    private CoordinatorLayout coordinatorLayout;
    private TextView noWidgetTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton addWidgetButton;
    private WidgetDetailListAdapter mAdapter;


    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatorLayout = view.findViewById(R.id.coordinator_Layout);
        addWidgetButton = view.findViewById(R.id.add_widget_button);
        noWidgetTextView = view.findViewById(R.id.no_widget_text);
        recyclerView = view.findViewById(R.id.recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        addWidgetButton.setOnClickListener((View v) -> showDialogWithWidgetNames());

        // Setup recyclerview
        mAdapter = new WidgetDetailListAdapter(mViewModel);
        mAdapter.setChangeListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback touchHelper = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView);

        // Bind to view model
        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), newWidgets -> {
            mAdapter.setWidgets(newWidgets);
        });

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        mViewModel.widgetsEmpty().observe(getViewLifecycleOwner(), empty ->
                noWidgetTextView.setVisibility(empty ? View.VISIBLE : View.GONE));
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
                mViewModel.createWidget(widgetNames[item]);
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
        if (viewHolder instanceof BaseDetailViewHolder) {
            deleteWidget(viewHolder.getAdapterPosition());
        }
    }

    private void deleteWidget(int index) {
        // get the removed item name to display it in snack bar
        final BaseEntity deletedWidget = mAdapter.getItem(index);

        // remove the item from recycler view
        //mAdapter.removeItem(viewHolder.getAdapterPosition());
        mViewModel.deleteWidget(deletedWidget);

        // showing snack bar with Undo option
        String undoText = getString(R.string.widget_undo, deletedWidget.name);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, undoText, Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", view -> {
            // undo is selected, restore the deleted item
            mViewModel.restoreWidget();
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.color_attention));
        snackbar.show();
    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {
        Log.i(TAG, "Changed: " + widgetEntity.name + " " + widgetEntity.posX);
        mViewModel.updateWidget(widgetEntity);
    }
}
