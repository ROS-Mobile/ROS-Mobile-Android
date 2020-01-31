package com.schneewittchen.rosandroid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.MasterConfigViewModel;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.2.2
 * @created on 10.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class MasterConfigFragment extends Fragment {

    private static final String TAG = MasterConfigFragment.class.getCanonicalName();

    private MasterConfigViewModel mViewModel;

    private TextInputLayout masterIpInputLayout;
    private TextInputEditText masterIpEditText;
    private TextInputLayout masterPortInputLayout;
    private TextInputEditText masterPortEditText;

    private CheckBox affixesCheckbox;
    private ImageButton notificationTitleEditButton;
    private ImageButton notificationTickerEditButton;
    private TextView notificationTitleText;
    private TextView notificationTickerTitleText;
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

        masterIpInputLayout = view.findViewById(R.id.master_ip_inputLayout);
        masterIpEditText = view.findViewById(R.id.master_ip_editText);
        masterPortInputLayout = view.findViewById(R.id.master_port_inputLayout);
        masterPortEditText = view.findViewById(R.id.master_port_editText);
        affixesCheckbox = view.findViewById(R.id.affixes_checkbox);
        notificationTitleEditButton = view.findViewById(R.id.notification_title_edit_button);
        notificationTickerEditButton = view.findViewById(R.id.notification_ticker_edit_button);
        notificationTitleText = view.findViewById(R.id.NotificationTitleChoiceText);
        notificationTickerTitleText = view.findViewById(R.id.TickerTitleChoiceText);
        networkSSIDChoiceText = view.findViewById(R.id.network_SSID_choice_text);
        ipAddressChoiceText = view.findViewById(R.id.ip_address_choice_text);
        connectButton = view.findViewById(R.id.connectButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MasterConfigViewModel.class);


        // View model connection -------------------------------------------------------------------

        mViewModel.getMaster().observe(getViewLifecycleOwner(), master -> {
            Log.i(TAG, "New Master: " + master);

            if (master == null) return;

            notificationTitleText.setText(master.notificationTitle);
            notificationTickerTitleText.setText(master.notificationTickerTitle);
            masterIpEditText.setText(master.ip);
            masterPortEditText.setText(String.format("%d", master.port));
        });

        mViewModel.getCurrentNetworkSSID().observe(getViewLifecycleOwner(),
                networkSSID -> networkSSIDChoiceText.setText(networkSSID));
        mViewModel.getDeviceIp().observe(getViewLifecycleOwner(),
                deviceIp -> ipAddressChoiceText.setText(deviceIp));


        // User input ------------------------------------------------------------------------------

        affixesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mViewModel.useIpWithAffixes(isChecked);
        });

        TextView.OnEditorActionListener actionListener = (v, actionId, event) -> {
            System.out.println(actionId +  " Event: " + event);

            if (actionId == EditorInfo.IME_ACTION_DONE){
                if (v.getId() == masterIpEditText.getId() && masterIpEditText.getText() != null)
                    mViewModel.setMasterIp(masterIpEditText.getText().toString());

                if (v.getId() == masterPortEditText.getId() && masterPortEditText.getText() != null)
                    mViewModel.setMasterPort(masterPortEditText.getText().toString());

                v.clearFocus();
                hideSoftKeyboard();
                return true;
            }

            return false;
        };

        masterIpEditText.setOnEditorActionListener(actionListener);
        masterPortEditText.setOnEditorActionListener(actionListener);

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
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
