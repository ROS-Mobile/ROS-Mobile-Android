package com.schneewittchen.rosandroid.ui.widgetDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.schneewittchen.rosandroid.R;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(WidgetDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}
