package com.schneewittchen.rosandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.databinding.FragmentMasterBinding;
import com.schneewittchen.rosandroid.model.repositories.ConnectionType;
import com.schneewittchen.rosandroid.viewmodel.MasterViewModel;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.3.0
 * @created on 10.01.20
 * @updated on 07.04.20
 * @modified by
 */
public class MasterFragment extends Fragment implements TextView.OnEditorActionListener {

    private static final String TAG = "MasterConfigFragment";

    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    private MasterViewModel mViewModel;
    private FragmentMasterBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMasterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
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


        // View model connection -------------------------------------------------------------------

        mViewModel.getMaster().observe(getViewLifecycleOwner(), master -> {
            if (master == null) return;

            binding.NotificationTitleText.setText(master.notificationTitle);
            binding.TickerTitleChoiceText.setText(master.notificationTickerTitle);
            binding.masterIpEditText.setText(master.ip);
            binding.masterPortEditText.setText(String.valueOf(master.port));
        });

        mViewModel.getCurrentNetworkSSID().observe(getViewLifecycleOwner(),
                networkSSID -> binding.networkSSIDChoiceText.setText(networkSSID));

        mViewModel.getDeviceIp().observe(getViewLifecycleOwner(),
                deviceIp -> binding.ipAddressChoiceText.setText(deviceIp));

        mViewModel.getRosConnection().observe(getViewLifecycleOwner(), this::setRosConnection);

        // User input ------------------------------------------------------------------------------

        binding.affixesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mViewModel.useIpWithAffixes(isChecked);
        });

        binding.connectButton.setOnClickListener(v -> mViewModel.connectToMaster());
        binding.disconnectButton.setOnClickListener(v -> mViewModel.disconnectFromMaster());
        binding.masterIpEditText.setOnEditorActionListener(this);
        binding.masterPortEditText.setOnEditorActionListener(this);
    }

    private void setRosConnection(ConnectionType connectionType) {
        int connectVisibility = View.GONE;
        int disconnectVisibility = View.GONE;
        int pendingVisibility = View.GONE;

        if (connectionType == ConnectionType.DISCONNECTED
                || connectionType == ConnectionType.FAILED) {
            connectVisibility = View.VISIBLE;

        } else if (connectionType == ConnectionType.CONNECTED) {
            disconnectVisibility = View.VISIBLE;

        } else if (connectionType == ConnectionType.PENDING) {
            pendingVisibility = View.VISIBLE;
        }

        binding.connectButton.setVisibility(connectVisibility);
        binding.disconnectButton.setVisibility(disconnectVisibility);
        binding.pendingBar.setVisibility(pendingVisibility);
    }

    private void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        int id = view.getId();

        if (actionId == EditorInfo.IME_ACTION_DONE){
            if (id == R.id.master_ip_editText) {
                Editable masterIp = binding.masterIpEditText.getText();

                if (masterIp != null) {
                    mViewModel.setMasterIp(masterIp.toString());
                }

            } else if (id == R.id.master_port_editText) {
                Editable masterPort = binding.masterPortEditText.getText();

                if (masterPort != null) {
                    mViewModel.setMasterPort(masterPort.toString());
                }
            }

            view.clearFocus();
            hideSoftKeyboard();

            return true;
        }

        return false;
    }
}
