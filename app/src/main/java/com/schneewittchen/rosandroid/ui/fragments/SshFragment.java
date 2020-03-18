package com.schneewittchen.rosandroid.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.SshRecyclerViewAdapter;
import com.schneewittchen.rosandroid.viewmodel.SshViewModel;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 18.03.20
 * @updated on
 * @modified by
 */

public class SshFragment extends Fragment {

    public static final String TAG = SshFragment.class.getCanonicalName();

    private SshViewModel mViewModel;

    private RecyclerView recyclerView;
    private SshRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextInputLayout ipAddressInputLayout;
    private TextInputEditText ipAddressEditText;
    private TextInputLayout portInputLayout;
    private TextInputEditText portEditText;
    private TextInputLayout usernameInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;

    private TextInputLayout terminalInputLayout;
    private TextInputEditText terminalEditText;

    private Button connectButton;
    private Button sendButton;

    private boolean connected;

    public static SshFragment newInstance() {
        return new SshFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ssh, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.outputRV);
        // Set recycler view to a maximum size
        recyclerView.setHasFixedSize(true);
        // Use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // Specify an adapter
        mAdapter = new SshRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);


        ipAddressInputLayout    = view.findViewById(R.id.ip_address_inputLayout);
        ipAddressEditText       = view.findViewById(R.id.ip_address_editText);
        portInputLayout         = view.findViewById(R.id.port_inputLayout);
        portEditText            = view.findViewById(R.id.port_editText);
        usernameInputLayout     = view.findViewById(R.id.username_inputLayout);
        usernameEditText        = view.findViewById(R.id.username_editText);
        passwordInputLayout     = view.findViewById(R.id.password_inputLayout);
        passwordEditText        = view.findViewById(R.id.password_editText);

        terminalInputLayout     = view.findViewById(R.id.terminal_inputLayout);
        terminalEditText        = view.findViewById(R.id.terminal_editText);

        connectButton           = view.findViewById(R.id.sshConnectButton);
        sendButton              = view.findViewById(R.id.sshSendButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(SshViewModel.class);

        // Connect Buttons
        connectButton.setOnClickListener(v -> {
            if (connected) {
                mViewModel.stopSsh();
            } else {
                connectSsh();
            }
        });

        sendButton.setOnClickListener(v -> {
            final String message = terminalEditText.getText().toString();
            mViewModel.sendViaSSH(message);
        });

        mViewModel.getOutputData().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mAdapter.addItem(s);
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mViewModel.isConnected().observe(this.getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    connected = true;
                    connectButton.setText("Disconnect");
                } else {
                    connected = false;
                    connectButton.setText("Connect");
                }
            }
        });
    }

    private void connectSsh() {
        final String ipAddress  = ipAddressEditText.getText().toString();
        final String portStr    = portEditText.getText().toString();
        final String username   = usernameEditText.getText().toString();
        final String password   = passwordEditText.getText().toString();

        int port = 22;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        mViewModel.connectViaSSH(username, password, ipAddress, port);
    }

}
