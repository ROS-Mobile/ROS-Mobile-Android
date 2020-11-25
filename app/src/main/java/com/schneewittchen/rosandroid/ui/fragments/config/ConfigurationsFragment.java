package com.schneewittchen.rosandroid.ui.fragments.config;

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
    private TextView titleText;
    private ImageButton renameButton;
    private ImageButton deleteButton;


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
        lastOpenedMoreButton = view.findViewById(R.id.last_opened_more_button);
        titleText = view.findViewById(R.id.current_config_textview);
        renameButton = view.findViewById(R.id.current_config_rename_button);
        deleteButton = view.findViewById(R.id.current_config_delete_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ConfigurationsViewModel.class);

        this.setUpRecyclerViews();

        addConfigButton.setOnClickListener(v -> mViewModel.addConfig());
        renameButton.setOnClickListener(v -> this.showRenameDialog());
        deleteButton.setOnClickListener(v -> this.showDeleteDialog());

        lastOpenedMoreButton.setOnClickListener(v -> {
            if (lastOpenedRV.getVisibility() == View.GONE) {
                lastOpenedRV.setVisibility(View.VISIBLE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
            } else {
                lastOpenedRV.setVisibility(View.GONE);
                lastOpenedMoreButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
            }
        });

        mViewModel.getLastOpenedConfigNames().observe(getViewLifecycleOwner(), 
                configNames -> lastOpenedAdapter.setConfigs(configNames));

        mViewModel.getConfigTitle().observe(getViewLifecycleOwner(), configTitle -> {
            if (configTitle == null) {
                titleText.setText(R.string.no_config);
            }else{
                titleText.setText(configTitle);
            }
        });
    }

    private void setUpRecyclerViews() {
        lastOpenedRV.setLayoutManager(new CustumLinearLayoutManager(this.getContext()));
        lastOpenedRV.setItemAnimator(new DefaultItemAnimator());
        lastOpenedAdapter = new ConfigListAdapter();
        lastOpenedRV.setAdapter(lastOpenedAdapter);

        lastOpenedRV.addOnItemTouchListener(new CustomRVItemTouchListener(this.getContext(), lastOpenedRV,
                (parent, view, position) -> openConfig(parent, position)));
    }

    private void openConfig(RecyclerView parent,int position) {
        String configName = lastOpenedAdapter.configNameList.get(position);

        mViewModel.chooseConfig(configName);
    }

    private void showRenameDialog() {
        if (this.getContext() == null)
            return;

        // Set up the input
        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog dialog =  new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.rename_config)
                .setView(input)
                .setPositiveButton(R.string.ok, (view, which) ->
                    mViewModel.renameConfig(input.getText().toString()))
                .setNegativeButton(R.string.cancel, (view, which) -> view.cancel())
                .create();

        dialog.show();
    }


    private void showDeleteDialog() {
        if (this.getContext() == null)
            return;

        AlertDialog dialog = new AlertDialog.Builder(this.getContext())
                .setTitle("Remove config")
                .setMessage(R.string.really_delete)
                .setPositiveButton(R.string.yes, (view, which) -> mViewModel.deleteConfig())
                .setNegativeButton(R.string.no, (view, which) -> view.cancel())
                .create();

        int color = getResources().getColor(R.color.delete_red);
        dialog.setOnShowListener(arg0 ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color));

        dialog.show();
    }
}
