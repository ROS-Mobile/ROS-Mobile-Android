package com.schneewittchen.rosandroid.model.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroid.jcraft.jsch.ChannelShell;
import com.schneewittchen.rosandroid.jcraft.jsch.JSch;
import com.schneewittchen.rosandroid.jcraft.jsch.JSchException;
import com.schneewittchen.rosandroid.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.1
 * @created on 19.02.20
 * @updated on 03.03.20
 * @modified by Nico Studt
 */
public class SshRepositoryImpl implements SshRepository {

    public static final String TAG = SshRepositoryImpl.class.getSimpleName();
    private static SshRepositoryImpl mInstance;

    JSch jsch;
    Session session;
    ChannelShell channelssh;
    OutputStream input_for_the_channel;
    InputStream output_from_the_channel;
    PrintStream commander;
    BufferedReader br;
    MutableLiveData<String> outputData;


    public SshRepositoryImpl() {
        outputData = new MutableLiveData<>();
    }


    public static SshRepositoryImpl getInstance() {
        if (mInstance == null) {
            mInstance = new SshRepositoryImpl();
        }

        return mInstance;
    }


    @Override
    public void startSession(String username, String password, String ip, int port) {
        new Thread(() -> {
            try {
                startSessionTask(username, password, ip, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startSessionTask(String username, String password, String ip, int port) throws JSchException, IOException {
        // Check if session already running
        if(session != null && session.isConnected()){
            Log.i(TAG, "Session is running already");
            return;
        }

        Log.i(TAG, "Start session");

        // Create new session
        jsch = new JSch();
        session = jsch.getSession(username, ip, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        java.util.Properties prop = new java.util.Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        // Start connection
        session.connect(30000);

        // SSH Channel
        channelssh = (ChannelShell)
                session.openChannel("shell");
        input_for_the_channel = channelssh.getOutputStream();
        output_from_the_channel = channelssh.getInputStream();

        commander = new PrintStream(input_for_the_channel, true);
        br = new BufferedReader(new InputStreamReader(output_from_the_channel));

        // Connect to channel
        channelssh.connect();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String line;
        while ((line = br.readLine()) != null && channelssh.isConnected()) {
            Log.i(TAG, "looper session");

            /*line = line.replaceAll("[^\\x00-\\x7F]", "");
            line = line.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            line = line.replaceAll("\\p{C}", "");
            line = line.replaceAll("\u001B\\[[;\\d]*m", "");*/

            // Remove ANSI control chars (Terminal VT 100)
            line = line.replaceAll("\u001B\\[[\\d;]*[^\\d;]","");

            // Publish lineData to LiveData
            outputData.postValue(line);
        }
    }

    @Override
    public void stopSession() {
        if (channelssh.isConnected()) {
            channelssh.disconnect();
        }
    }

    @Override
    public void sendMessage(String message) {
        commander.println(message);
    }

    @Override
    public LiveData<String> getOutputData() {
        return outputData;
    }
}
