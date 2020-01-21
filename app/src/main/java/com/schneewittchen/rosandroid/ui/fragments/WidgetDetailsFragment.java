package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.WidgetDetailsViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class WidgetDetailsFragment extends Fragment {

    private WidgetDetailsViewModel mViewModel;

    private Button addWidgetButton;


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

        addWidgetButton = view.findViewById(R.id.add_widget_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(WidgetDetailsViewModel.class);
        // TODO: Use the ViewModel

        addWidgetButton.setOnClickListener((View v) -> {
            showDialogWithWidgetNames();
        });
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
            String selectedText = widgetNames[item].toString();  //Selected item in listview
            System.out.println(selectedText);
        });

        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();

        //Show the dialog
        alertDialogObject.show();
    }

}
