package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.databinding.FragmentMasterBinding;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.connection.ConnectionType;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.MasterViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observer;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.3.0
 * @created on 10.01.2020
 * @updated on 05.10.2020
 * @modified by Nico Studt
 * @updated on 16.11.2020
 * @modified by Nils Rottmann
 */
public class MasterFragment extends Fragment implements TextView.OnEditorActionListener {

    private static final String TAG = MasterFragment.class.getSimpleName();

    private MasterViewModel mViewModel;
    private FragmentMasterBinding binding;

    private ArrayList<String> ipItemList;
    protected AutoCompleteTextView ipAddressField;
    protected TextInputLayout ipAddressLayout;
    private ArrayAdapter<String> ipArrayAdapter;

    public static MasterFragment newInstance() {
        return new MasterFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMasterBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MasterViewModel.class);

        // Define Views --------------------------------------------------------------
        ipAddressField = getView().findViewById(R.id.ipAddessTextView);
        ipAddressLayout = getView().findViewById(R.id.ipAddessLayout);

        ipItemList = new ArrayList<>();
        ipArrayAdapter = new ArrayAdapter<>(this.getContext(),
                R.layout.dropdown_menu_popup_item, ipItemList);
        ipAddressField.setAdapter(ipArrayAdapter);

        String firstDeviceIp = mViewModel.getIPAddress();
        if (firstDeviceIp != null) {
            ipAddressField.setText(firstDeviceIp, false);
        }

        ipAddressField.setOnClickListener(clickedView -> {
            updateIpSpinner();
            ipAddressField.showDropDown();
        });

        ipAddressLayout.setEndIconOnClickListener(v -> {
            ipAddressField.requestFocus();
            ipAddressField.callOnClick();
        });

        ipAddressField.setOnItemClickListener((parent, view, position, id) -> {
            ipAddressField.clearFocus();
        });

        // View model connection -------------------------------------------------------------------

        mViewModel.getMaster().observe(getViewLifecycleOwner(), master -> {
            if (master == null) return;
            binding.masterIpEditText.setText(master.ip);
            binding.masterPortEditText.setText(String.valueOf(master.port));
        });

        mViewModel.getCurrentNetworkSSID().observe(getViewLifecycleOwner(),
                networkSSID -> binding.NetworkSSIDText.setText(networkSSID));

        mViewModel.getRosConnection().observe(getViewLifecycleOwner(), this::setRosConnection);

        // User input ------------------------------------------------------------------------------

        binding.connectButton.setOnClickListener(v -> {
                mViewModel.setMasterDeviceIp(ipAddressField.getText().toString());
                mViewModel.connectToMaster();
        });
        binding.disconnectButton.setOnClickListener(v -> mViewModel.disconnectFromMaster());
        binding.masterIpEditText.setOnEditorActionListener(this);
        binding.masterPortEditText.setOnEditorActionListener(this);
    }

    private void updateIpSpinner() {
        ipItemList = new ArrayList<>();
        ipItemList = mViewModel.getIPAddressList();
        ipArrayAdapter.clear();
        ipArrayAdapter.addAll(ipItemList);
    }

    private void setRosConnection(ConnectionType connectionType) {
        int connectVisibility = View.GONE;
        int disconnectVisibility = View.GONE;
        int pendingVisibility = View.GONE;

        if (connectionType == ConnectionType.DISCONNECTED
                || connectionType == ConnectionType.FAILED) {
            connectVisibility = View.VISIBLE;
            binding.disconnectButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        } else if (connectionType == ConnectionType.CONNECTED) {
            disconnectVisibility = View.VISIBLE;
            binding.disconnectButton.setBackgroundColor(getResources().getColor(R.color.delete_red));

        } else if (connectionType == ConnectionType.PENDING) {
            pendingVisibility = View.VISIBLE;
        }

        binding.connectButton.setVisibility(connectVisibility);
        binding.disconnectButton.setVisibility(disconnectVisibility);
        binding.pendingBar.setVisibility(pendingVisibility);
    }

    private void updateMasterDetails() {
        // Update master IP
        Editable masterIp = binding.masterIpEditText.getText();

        if (masterIp != null) {
            mViewModel.setMasterIp(masterIp.toString());
        }

        // Update master port
        Editable masterPort = binding.masterPortEditText.getText();

        if (masterPort != null) {
            mViewModel.setMasterPort(masterPort.toString());
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                updateMasterDetails();

                view.clearFocus();
                Utils.hideSoftKeyboard(Objects.requireNonNull(getView()));

                return true;
        }

        return false;
    }
}
