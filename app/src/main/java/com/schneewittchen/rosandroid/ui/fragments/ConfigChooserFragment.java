package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.ui.helper.ConfigListAdapter;
import com.schneewittchen.rosandroid.ui.helper.CustomRVItemTouchListener;
import com.schneewittchen.rosandroid.ui.helper.CustumLinearLayoutManager;
import com.schneewittchen.rosandroid.ui.helper.RecyclerViewItemClickListener;
import com.schneewittchen.rosandroid.viewmodel.ConfigChooserViewModel;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class ConfigChooserFragment extends Fragment {

    private ConfigChooserViewModel mViewModel;
    private Button addConfigButton;
    private RecyclerView lastOpenedRV;
    private ConfigListAdapter lastOpenedAdapter;
    private ImageButton lastOpenedMoreButton;
    private RecyclerView favouriteRV;
    private ConfigListAdapter favouriteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configation_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addConfigButton = view.findViewById(R.id.add_config_button);
        lastOpenedRV = view.findViewById(R.id.last_opened_recyclerview);
        favouriteRV = view.findViewById(R.id.favorite_opened_recyclerview);
        lastOpenedMoreButton = view.findViewById(R.id.last_opened_more_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ConfigChooserViewModel.class);

        addConfigButton.setOnClickListener(v -> this.addNewConfig());

        lastOpenedMoreButton.setOnClickListener(v -> {
            if (lastOpenedRV.getVisibility() == View.GONE) {
                lastOpenedRV.setVisibility(View.VISIBLE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
            } else {
                lastOpenedRV.setVisibility(View.GONE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
        });

        favouriteRV.setLayoutManager(new CustumLinearLayoutManager(this.getContext()));
        favouriteRV.setItemAnimator(new DefaultItemAnimator());
        favouriteAdapter = new ConfigListAdapter();
        favouriteRV.setAdapter(favouriteAdapter);

        lastOpenedRV.setLayoutManager(new CustumLinearLayoutManager(this.getContext()));
        lastOpenedRV.setItemAnimator(new DefaultItemAnimator());
        lastOpenedAdapter = new ConfigListAdapter();
        lastOpenedRV.setAdapter(lastOpenedAdapter);

        mViewModel.getLastOpenedConfigs().observe(getViewLifecycleOwner(), configs -> {
            lastOpenedAdapter.setConfigs(configs);
            favouriteAdapter.setConfigs(configs);
        });


        lastOpenedRV.addOnItemTouchListener(new CustomRVItemTouchListener(this.getContext(), lastOpenedRV,
                (parent, view, position) -> openConfig(parent, position)));
    }

    private void addNewConfig() {
        mViewModel.addConfig();
    }

    private void openConfig(RecyclerView parent,int position) {
        ConfigEntity config = lastOpenedAdapter.configList.get(position);

        mViewModel.chooseConfig(config.id);
    }
}
