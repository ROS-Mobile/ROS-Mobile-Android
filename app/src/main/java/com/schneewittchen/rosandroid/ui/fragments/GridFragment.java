package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.ui.custum_views.WidgetGroup;
import com.schneewittchen.rosandroid.viewmodel.GridViewModel;

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
public class GridFragment extends Fragment {

    private GridViewModel mViewModel;
    private WidgetGroup widgetGroupview;


    public static GridFragment newInstance() {
        return new GridFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        widgetGroupview = view.findViewById(R.id.widget_groupview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(GridViewModel.class);

        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), new Observer<List<WidgetEntity>>() {
            @Override
            public void onChanged(List<WidgetEntity> widgetEntities) {

            }
        });
    }



}
