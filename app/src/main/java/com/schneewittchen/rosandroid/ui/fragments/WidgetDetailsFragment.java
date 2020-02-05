package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.RecyclerItemTouchHelper;
import com.schneewittchen.rosandroid.ui.helper.WidgetListAdapter;
import com.schneewittchen.rosandroid.viewmodel.WidgetDetailsViewModel;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.2.3
 * @created on 10.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class WidgetDetailsFragment extends Fragment implements RecyclerItemTouchHelper.TouchListener{

    static final String TAG = WidgetDetailsFragment.class.getCanonicalName();

    private WidgetDetailsViewModel mViewModel;
    private CoordinatorLayout coordinatorLayout;
    private TextView noWidgetTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton addWidgetButton;
    private WidgetListAdapter mAdapter;
    private List<WidgetEntity> mWidgets;

    public static WidgetDetailsFragment newInstance() {
        return new WidgetDetailsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_widgets_details, container, false);
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

        mViewModel = new ViewModelProvider(this).get(WidgetDetailsViewModel.class);

        addWidgetButton.setOnClickListener((View v) -> showDialogWithWidgetNames());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new WidgetListAdapter();
        recyclerView.setAdapter(mAdapter);

        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), newWidgets -> {
            mWidgets = new ArrayList<>();
            mWidgets.addAll(newWidgets);
            mAdapter.setWidgets(mWidgets);
            Log.i(TAG, "Widgets changed with amount: " + mWidgets.size());
        });

        mViewModel.widgetsEmpty().observe(getViewLifecycleOwner(), empty ->
                noWidgetTextView.setVisibility(empty? View.VISIBLE : View.GONE));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback touchHelper = new RecyclerItemTouchHelper(0,
                                                                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView);

    }

    private void showDialogWithWidgetNames() {
        Preconditions.checkArgument(getContext() != null);

        int[] mWidgetIds = mViewModel.getAvailableWidgetNames();

        String[] widgetNames = new String[mWidgetIds.length];

        for(int i = 0; i < mWidgetIds.length; i++){
            widgetNames[i] = getResources().getString(mWidgetIds[i]);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Widgets");
        dialogBuilder.setItems(widgetNames, (dialog, item) -> {
            String selectedText = widgetNames[item];  //Selected item in listview
            mViewModel.createWidget(selectedText);

            Log.i(TAG, "Selected Text: " + selectedText);
        });

        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //Show the dialog
        alertDialogObject.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WidgetListAdapter.MyViewHolder) {
            new Handler().postDelayed(() ->
                    deleteWidget(viewHolder.getAdapterPosition())
                    , 500);
        }
    }

    private void deleteWidget(int index) {
        // get the removed item name to display it in snack bar
        final WidgetEntity deletedWidget = mAdapter.widgetList.get(index);

        String name = deletedWidget.getType();

        // remove the item from recycler view
        //mAdapter.removeItem(viewHolder.getAdapterPosition());
        mViewModel.deleteWidget(deletedWidget);

        // showing snack bar with Undo option
        String undoText = getString(R.string.widget_undo, name);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, undoText, Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", view -> {
            // undo is selected, restore the deleted item
            //mAdapter.restoreItem(deletedItem, deletedIndex);
            mViewModel.restoreWidget();
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.color_attention));
        snackbar.show();
    }
}
