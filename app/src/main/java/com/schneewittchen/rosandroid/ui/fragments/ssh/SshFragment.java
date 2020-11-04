package com.schneewittchen.rosandroid.ui.fragments.ssh;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.databinding.FragmentSshBinding;
import com.schneewittchen.rosandroid.viewmodel.SshViewModel;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 18.03.20
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 */

public class SshFragment extends Fragment implements TextView.OnEditorActionListener {

    public static final String TAG = SshFragment.class.getCanonicalName();

    private SshViewModel mViewModel;
    private FragmentSshBinding binding;
    private RecyclerView recyclerView;
    private SshRecyclerViewAdapter mAdapter;
    private TextInputEditText terminalEditText;
    private Button connectButton;
    private FloatingActionButton sendButton;
    private boolean connected;


    public static SshFragment newInstance() {
        return new SshFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSshBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Specify an adapter
        mAdapter = new SshRecyclerViewAdapter();

        // Define the Recycler View
        recyclerView = view.findViewById(R.id.outputRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        // Get the handles
        terminalEditText = view.findViewById(R.id.terminal_editText);
        connectButton = view.findViewById(R.id.sshConnectButton);
        sendButton = view.findViewById(R.id.sshSendButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(SshViewModel.class);

        // Define ViewModel Connection ------
        mViewModel.getSSH().observe(getViewLifecycleOwner(), ssh -> {
            if (ssh == null) return;
            binding.ipAddressEditText.setText(ssh.ip);
            binding.portEditText.setText(Integer.toString(ssh.port));
            binding.usernameEditText.setText(ssh.username);
            binding.passwordEditText.setText(ssh.password);
        });

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
            terminalEditText.setText("");
            hideSoftKeyboard();
        });

        mViewModel.getOutputData().observe(this.getViewLifecycleOwner(), s -> {
            mAdapter.addItem(s);
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        });

        mViewModel.isConnected().observe(this.getViewLifecycleOwner(), connectionFlag -> {
            connected = connectionFlag;

            if (connectionFlag) {
                connectButton.setText("Disconnect");
            } else {
                connected = false;
                connectButton.setText("Connect");
            }
        });

        // User Input
        binding.ipAddressEditText.setOnEditorActionListener(this);
        binding.portEditText.setOnEditorActionListener(this);
        binding.usernameEditText.setOnEditorActionListener(this);
        binding.passwordEditText.setOnEditorActionListener(this);
    }

    private void connectSsh() {
        mViewModel.connectViaSSH();
    }

    private void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        int id = view.getId();

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (id == R.id.ip_address_editText) {
                Editable sshIp = binding.ipAddressEditText.getText();

                if (sshIp != null) {
                    mViewModel.setSshIp(sshIp.toString());
                }

            } else if (id == R.id.port_editText) {
                Editable sshPort = binding.portEditText.getText();

                if (sshPort != null) {
                    mViewModel.setSshPort(sshPort.toString());
                }

            } else if (id == R.id.password_editText) {
                Editable sshPassword = binding.passwordEditText.getText();

                if (sshPassword != null) {
                    mViewModel.setSshPassword(sshPassword.toString());
                }

            } else if (id == R.id.username_editText) {
                Editable sshUsername = binding.usernameEditText.getText();
                if (sshUsername != null) {
                    mViewModel.setSshUsername(sshUsername.toString());
                }
            }

            view.clearFocus();
            hideSoftKeyboard();

            return true;
        }

        return false;
    }

}
