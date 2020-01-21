package com.schneewittchen.rosandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.MasterConfigViewModel;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 19.01.20
 * @modified by
 */
public class MasterConfigFragment extends Fragment {

    private MasterConfigViewModel mViewModel;

    private EditText masterIpEdittext;
    private CheckBox affixesCheckbox;
    private ImageButton notificationTitleEditButton;
    private ImageButton notificationTickerEditButton;
    private TextView networkSSIDChoiceText;
    private TextView ipAddressChoiceText;
    private Button connectButton;


    public static MasterConfigFragment newInstance() {
        return new MasterConfigFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        masterIpEdittext = view.findViewById(R.id.master_ip_edittext);
        affixesCheckbox = view.findViewById(R.id.affixes_checkbox);
        notificationTitleEditButton = view.findViewById(R.id.notification_title_edit_button);
        notificationTickerEditButton = view.findViewById(R.id.notification_ticker_edit_button);
        networkSSIDChoiceText = view.findViewById(R.id.network_SSID_choice_text);
        ipAddressChoiceText = view.findViewById(R.id.ip_address_choice_text);
        connectButton = view.findViewById(R.id.connectButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MasterConfigViewModel.class);


        mViewModel.getCurrentNetworkSSID().observe(this, networkSSID -> {
            networkSSIDChoiceText.setText(networkSSID);
        });

        mViewModel.getDeviceIp().observe(this, (deviceIp) -> {
            ipAddressChoiceText.setText(deviceIp);
        });

        affixesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mViewModel.useIpWithAffixes(isChecked);
        });

        masterIpEdittext.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ){
                mViewModel.setMasterIp(masterIpEdittext.getText().toString());
                hideSoftKeyboard();

                return true;
            }

            return false;
        });

        notificationTitleEditButton.setOnClickListener(v -> {
            System.out.println("Ticker title pressed");
            // TODO : Open choice dialog for notification title
        });

        notificationTickerEditButton.setOnClickListener(v -> {
            System.out.println("Ticker pressed");
            // TODO : Open choice dialog for notification ticker title
        });

        connectButton.setOnClickListener(v -> {
            System.out.println("Connect pressed");
            mViewModel.connectToMaster();
        });
    }

    public void hideSoftKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

}
