package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;
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
import com.schneewittchen.rosandroidlib.model.entities.Widget;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.2.1
 * @created on 10.01.20
 * @updated on 28.01.20
 * @modified by
 */
public class WidgetDetailsFragment extends Fragment implements RecyclerItemTouchHelper.TouchListener{

    private WidgetDetailsViewModel mViewModel;
    private CoordinatorLayout coordinatorLayout;
    private TextView noWidgetTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton addWidgetButton;
    private WidgetListAdapter mAdapter;


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

        mViewModel = ViewModelProviders.of(this).get(WidgetDetailsViewModel.class);

        addWidgetButton.setOnClickListener((View v) -> showDialogWithWidgetNames());

        mAdapter = new WidgetListAdapter(this.getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.hasFixedSize();

        mViewModel.getAllWidgets().observe(this, widgets -> {
            mAdapter.setWidgets(widgets);
            System.out.println("Changed");
        });

        mViewModel.widgetsEmpty().observe(this, empty ->
                noWidgetTextView.setVisibility(empty? View.VISIBLE : View.GONE));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void showDialogWithWidgetNames() {
        Preconditions.checkArgument(getContext() != null);

        int[] mWidgetIds = mViewModel.getAllWidgetNames();

        String[] widgetNames = new String[mWidgetIds.length];

        for(int i = 0; i < mWidgetIds.length; i++){
            widgetNames[i] = getResources().getString(mWidgetIds[i]);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Widgets");
        dialogBuilder.setItems(widgetNames, (dialog, item) -> {
            String selectedText = widgetNames[item];  //Selected item in listview
            mViewModel.addWidget(selectedText);
            System.out.println(selectedText);
        });

        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //Show the dialog
        alertDialogObject.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WidgetListAdapter.MyViewHolder) {
            List<Widget> widgetList = mViewModel.getAllWidgets().getValue();

            // get the removed item name to display it in snack bar
            String name = widgetList.get(viewHolder.getAdapterPosition()).getType();

            // backup of removed item for undo purpose
            final Widget deletedItem = widgetList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            String undoText = getString(R.string.widget_undo, name);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, undoText, Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> {
                // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        mViewModel.deleteWidget(deletedItem);
                        System.out.println("Widget deleted");
                    }
                }

                @Override
                public void onShown(Snackbar snackbar) {
                }
            });


            snackbar.setActionTextColor(getResources().getColor(R.color.color_attention));
            snackbar.show();
        }
    }
}
