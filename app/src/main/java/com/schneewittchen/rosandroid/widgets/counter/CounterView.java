package com.schneewittchen.rosandroid.widgets.counter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseViewLayout;

import java.util.Timer;
import java.util.TimerTask;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.20
 * @updated on 15.03.20
 * @modified by
 */
public class CounterView extends BaseViewLayout {

    public static final String TAG = "CounterView";

    TextView counterText;
    Button resetButton;
    Button startButton;
    float counter;
    Timer timer;

    public CounterView(Context context) {
        super(context);
        Log.i(TAG, "Call first constructor");
    }

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "Call second constructor");
    }

    @Override
    public void init() {
        Log.i(TAG, "Init");
        counterText = findViewById(R.id.counterText);
        resetButton = findViewById(R.id.resetButton);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> startCounter());
        resetButton.setOnClickListener(v -> reset());

        timer = new Timer();
        counter = 0;
        this.timer = null;
    }

    private void startCounter() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                increaseCounter(1);
            }
        }, 0, 1000);
    }

    private void reset() {
        timer.cancel();
        this.setCounter(0);
    }

    private void increaseCounter(float value) {
        this.setCounter(counter + value);
    }

    private void setCounter(float value) {
        this.counter = value;
        this.counterText.setText(String.format("%.2f", counter));

        CounterData data = new CounterData();
        data.counter = counter;

        this.informDataChange(data);
    }

    @Override
    protected int getLayoutId() {
        Log.i(TAG, "Get layout id");
        return R.layout.viz_counter;
    }
}
