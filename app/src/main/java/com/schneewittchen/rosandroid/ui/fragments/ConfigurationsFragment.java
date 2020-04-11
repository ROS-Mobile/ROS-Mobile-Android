package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.ui.helper.ConfigListAdapter;
import com.schneewittchen.rosandroid.ui.helper.CustomRVItemTouchListener;
import com.schneewittchen.rosandroid.ui.helper.CustumLinearLayoutManager;
import com.schneewittchen.rosandroid.viewmodel.ConfigurationsViewModel;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 10.01.20
 * @updated on 06.02.20
 * @modified by
 */
public class ConfigurationsFragment extends Fragment {

    private ConfigurationsViewModel mViewModel;
    private Button addConfigButton;
    private RecyclerView lastOpenedRV;
    private ConfigListAdapter lastOpenedAdapter;
    private ImageButton lastOpenedMoreButton;
    private RecyclerView favouriteRV;
    private ConfigListAdapter favouriteAdapter;
    private TextView currentConfigTextview;
    private ImageButton currentConfigRenameButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configurations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addConfigButton = view.findViewById(R.id.add_config_button);
        lastOpenedRV = view.findViewById(R.id.last_opened_recyclerview);
        favouriteRV = view.findViewById(R.id.favorite_opened_recyclerview);
        lastOpenedMoreButton = view.findViewById(R.id.last_opened_more_button);
        currentConfigTextview = view.findViewById(R.id.current_config_textview);
        currentConfigRenameButton = view.findViewById(R.id.current_config_rename_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ConfigurationsViewModel.class);

        this.setUpRecyclerViews();

        addConfigButton.setOnClickListener(v -> this.addNewConfig());
        currentConfigRenameButton.setOnClickListener(v -> this.showConfigRenameDialog());

        lastOpenedMoreButton.setOnClickListener(v -> {
            if (lastOpenedRV.getVisibility() == View.GONE) {
                lastOpenedRV.setVisibility(View.VISIBLE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
            } else {
                lastOpenedRV.setVisibility(View.GONE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
        });

        mViewModel.getLastOpenedConfigs().observe(getViewLifecycleOwner(), configs -> {
            lastOpenedAdapter.setConfigs(configs);
            favouriteAdapter.setConfigs(configs);
        });

        mViewModel.getConfigTitle().observe(getViewLifecycleOwner(), s ->
                currentConfigTextview.setText(s));
    }

    private void setUpRecyclerViews() {
        favouriteRV.setLayoutManager(new CustumLinearLayoutManager(this.getContext()));
        favouriteRV.setItemAnimator(new DefaultItemAnimator());
        favouriteAdapter = new ConfigListAdapter();
        favouriteRV.setAdapter(favouriteAdapter);

        lastOpenedRV.setLayoutManager(new CustumLinearLayoutManager(this.getContext()));
        lastOpenedRV.setItemAnimator(new DefaultItemAnimator());
        lastOpenedAdapter = new ConfigListAdapter();
        lastOpenedRV.setAdapter(lastOpenedAdapter);

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

    private void showConfigRenameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.new_config_name);

        // Set up the input
        final EditText input = new EditText(this.getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.ok, (dialog, which) ->
                System.out.println(input.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
