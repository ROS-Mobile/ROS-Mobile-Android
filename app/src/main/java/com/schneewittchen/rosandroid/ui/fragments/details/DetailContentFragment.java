package com.schneewittchen.rosandroid.ui.fragments.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.details.DetailViewHolder;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.21
 */
public class DetailContentFragment extends Fragment implements WidgetChangeListener {

    public static String TAG = DetailContentFragment.class.getSimpleName();

    private NavController navController;
    private DetailsViewModel viewModel;
    private ViewGroup widgetContainer;
    private MaterialButton backButtonOverview;
    private MaterialButton backButtonGroup;
    private DetailViewHolder widgetHolder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_content, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (widgetHolder != null) {
            widgetHolder.forceWidgetUpdate();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(viewModel != null) {
            viewModel.popPath(1);
        }

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        widgetContainer = view.findViewById(R.id.widget_container);
        backButtonOverview = view.findViewById(R.id.back_button_overview);
        backButtonGroup = view.findViewById(R.id.back_button_group);

        viewModel.getWidget().observe(getViewLifecycleOwner(), this::initView);

        // Construct back buttons
        backButtonOverview.setOnClickListener(v ->
            navController.popBackStack(R.id.detailOverviewFragment, false));

        backButtonGroup.setOnClickListener(v ->
            navController.popBackStack());
    }

    private void initView(BaseEntity baseEntity) {
        BaseEntity entity = null;
        List<Long> widgetPath = viewModel.getWidgetPath().getValue();

        if (widgetPath.size() == 1) {
            backButtonGroup.setVisibility(View.INVISIBLE);
            entity = baseEntity;

        } else if(widgetPath.size() == 2){
            backButtonGroup.setText(baseEntity.name);
            backButtonGroup.setVisibility(View.VISIBLE);
            entity = baseEntity.getChildById(widgetPath.get(1));
        }

        try {
            // create and init widget view
            String layoutStr = String.format(Constants.DETAIL_LAYOUT_FORMAT, entity.type.toLowerCase());
            int detailContentLayout = Utils.getResId(layoutStr, R.layout.class);
            LayoutInflater inflator = LayoutInflater.from(widgetContainer.getContext());
            View itemView = inflator.inflate(detailContentLayout, widgetContainer, false);

            // Create and init view holder
            String viewholderClassPath = BuildConfig.APPLICATION_ID
                    + String.format(Constants.VIEWHOLDER_FORMAT, entity.type.toLowerCase(), entity.type);
            Class<DetailViewHolder> clazzObject =
                    (Class<DetailViewHolder>) Class.forName(viewholderClassPath);
            Constructor<DetailViewHolder> cons = clazzObject.getConstructor();

            widgetHolder = cons.newInstance();
            widgetHolder.setWidgetChangeListener(this);
            widgetHolder.setViewModel(viewModel);
            widgetHolder.setView(itemView);
            widgetHolder.setEntity(entity);

            // Add view
            widgetContainer.removeAllViews();
            widgetContainer.addView(itemView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {
        viewModel.updateWidget(widgetEntity);
    }
}
