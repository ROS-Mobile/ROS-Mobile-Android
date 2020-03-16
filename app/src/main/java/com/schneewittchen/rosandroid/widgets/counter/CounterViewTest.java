package com.schneewittchen.rosandroid.widgets.counter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseViewLayout;
import com.schneewittchen.rosandroid.widgets.base.DataListener;
import com.schneewittchen.rosandroid.widgets.base.Interactable;
import com.schneewittchen.rosandroid.widgets.base.WidgetData;

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
public class CounterViewTest extends androidx.appcompat.widget.AppCompatTextView implements Interactable {

    public static final String TAG = "CounterViewTest";

    DataListener dataListener;
    float counter;
    Timer timer;

    public CounterViewTest(Context context) {
        super(context);
        init();
        Log.i(TAG, "Call first constructor");
    }

    public CounterViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        Log.i(TAG, "Call second constructor");
    }

    public void init() {
        timer = new Timer();
        counter = 0;
        startCounter();
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
        this.postDelayed(new Runnable() {

            @Override
            public void run() {
                setText(String.format("%.2f", counter));

            }
        }, 0);

        CounterData data = new CounterData();
        data.counter = counter;

        this.informDataChange(data);
    }

    @Override
    public void informDataChange(WidgetData data) {
        if(dataListener != null) {
            dataListener.onNewData(data);
        }
    }

    @Override
    public void setData(WidgetData data) {
        // Default data set, but nothing to see here!
    }

    @Override
    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    @Override
    public void removeDataListener() {
        this.dataListener = null;
    }
}
