package com.schneewittchen.rosandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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
public class MasterFragment extends Fragment {

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
            Log.i(TAG, "New Master: " + master);

            if (master == null) return;

            Log.i(TAG, "New Master port: " + master.port);

            binding.NotificationTitleText.setText(master.notificationTitle);
            binding.TickerTitleChoiceText.setText(master.notificationTickerTitle);
            binding.masterIpEditText.setText(master.ip);
            binding.masterPortEditText.setText(String.format("%d", master.port));
        });

        mViewModel.getCurrentNetworkSSID().observe(getViewLifecycleOwner(),
                networkSSID -> binding.networkSSIDChoiceText.setText(networkSSID));
        mViewModel.getDeviceIp().observe(getViewLifecycleOwner(),
                deviceIp -> binding.ipAddressChoiceText.setText(deviceIp));


        // User input ------------------------------------------------------------------------------

        binding.affixesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mViewModel.useIpWithAffixes(isChecked);
        });

        TextView.OnEditorActionListener actionListener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                switch(v.getId()) {
                    case R.id.master_ip_editText:
                        Editable masterIp = binding.masterIpEditText.getText();

                        if (masterIp != null) {
                            mViewModel.setMasterIp(masterIp.toString());
                        }

                    case R.id.master_port_editText:
                        Editable masterPort = binding.masterPortEditText.getText();

                        if (masterPort != null) {
                            mViewModel.setMasterPort(masterPort.toString());
                        }
                }

                v.clearFocus();
                hideSoftKeyboard();
                return true;
            }

            return false;
        };

        binding.masterIpEditText.setOnEditorActionListener(actionListener);
        binding.masterPortEditText.setOnEditorActionListener(actionListener);

        binding.notificationTitleEditButton.setOnClickListener(v -> {
            Log.i(TAG, "Ticker title pressed");
            // TODO : Open choice dialog for notification title
        });

        binding.notificationTickerEditButton.setOnClickListener(v -> {
            Log.i(TAG, "Ticker pressed");
            // TODO : Open choice dialog for notification ticker title
        });

        binding.connectButton.setOnClickListener(v -> {
            Log.i(TAG, "Connect pressed");
            mViewModel.connectToMaster();
        });
    }

    public void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
