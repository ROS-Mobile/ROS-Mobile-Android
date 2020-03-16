package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.ui.custum_views.WidgetGroup;
import com.schneewittchen.rosandroid.ui.helper.WidgetDiffCallback;
import com.schneewittchen.rosandroid.viewmodel.VizViewModel;
import com.schneewittchen.rosandroid.widgets.base.DataListener;
import com.schneewittchen.rosandroid.widgets.base.WidgetData;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 13.03.20
 * @modified by
 */
public class VizFragment extends Fragment implements DataListener {

    public static final String TAG = VizFragment.class.getCanonicalName();

    private VizViewModel mViewModel;
    private WidgetGroup widgetGroupview;


    public static VizFragment newInstance() {
        return new VizFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        widgetGroupview = view.findViewById(R.id.widget_groupview);
        widgetGroupview.setDataListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(VizViewModel.class);

        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), widgetEntities -> {
            // Connect widgets with ros
            updateWidgets(widgetEntities);

            // Set widgets on view
            widgetGroupview.setWidgets(widgetEntities);

        });
    }

    private void updateWidgets(List<WidgetEntity> newWidgets) {
        List<WidgetEntity> oldWidgets = widgetGroupview.getWidgets();

        WidgetDiffCallback diffCallback = new WidgetDiffCallback(newWidgets, oldWidgets);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int index, int count) {
                Log.i(TAG, "Inserted: Position " + index + " count " + count);
                mViewModel.register(newWidgets.get(index));
            }

            @Override
            public void onRemoved(int index, int count) {
                mViewModel.unregister(oldWidgets.get(index));
                Log.i(TAG, "Removed: Position " + index + " count " + count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                Log.i(TAG, "Moved: From " + fromPosition + " to " + toPosition);
            }

            @Override
            public void onChanged(int index, int count, @Nullable Object payload) {
                for(int i = index; i < index +count; i++) {
                    this.onChanged(i);
                }
                Log.i(TAG, "Changed: From " + index + " count " + count);
            }

            private void onChanged(int index) {
                mViewModel.reregister(oldWidgets.get(index));
            }
        });
    }


    @Override
    public void onNewData(WidgetData data) {
        mViewModel.informWidgetDataChange(data);
    }
}
