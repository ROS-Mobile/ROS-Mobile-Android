package com.schneewittchen.rosandroid.ui.fragments.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.DetailViewHolder;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import java.lang.reflect.Constructor;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.21
 */
public class DetailWidgetFragment extends Fragment implements WidgetChangeListener {

    public static String TAG = DetailWidgetFragment.class.getSimpleName();

    private NavController navController;
    private DetailsViewModel viewModel;
    private ViewGroup widgetContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_widget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        widgetContainer = view.findViewById(R.id.widget_container);

        viewModel.getWidget().observe(getViewLifecycleOwner(), this::initView);
    }

    private void initView(BaseEntity entity) {
        Log.i(TAG, "Got entity: " + entity.name);

        try {
            // create and init widget view
            String layoutStr = String.format(Constants.DETAIL_LAYOUT_FORMAT, entity.type.toLowerCase());
            int detailContentLayout = Utils.getResId(layoutStr, R.layout.class);
            LayoutInflater inflator = LayoutInflater.from(widgetContainer.getContext());
            View itemView = inflator.inflate(detailContentLayout, widgetContainer, false);

            Log.i(TAG, "Create view: " + layoutStr);

            // Create and init view holder
            String viewholderClassPath = BuildConfig.APPLICATION_ID
                    + String.format(Constants.VIEWHOLDER_FORMAT, entity.type.toLowerCase(), entity.type);
            Class<DetailViewHolder> clazzObject =
                    (Class<DetailViewHolder>) Class.forName(viewholderClassPath);
            Constructor<DetailViewHolder> cons = clazzObject.getConstructor();

            DetailViewHolder viewHolder = cons.newInstance();
            viewHolder.setWidgetChangeListener(this);
            viewHolder.setViewModel(viewModel);
            viewHolder.setView(itemView);
            viewHolder.setEntity(entity);

            // Add view
            widgetContainer.removeAllViews();
            widgetContainer.addView(itemView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {

    }
}
