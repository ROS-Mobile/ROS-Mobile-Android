package com.schneewittchen.rosandroid.widgets.battery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;

import org.ros.internal.message.Message;

import sensor_msgs.BatteryState;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.05.2021
 */
public class BatteryView extends SubscriberWidgetView {

    public static final String TAG = BatteryView.class.getSimpleName();
    public static final int MAX_LEVEL = 5;

    Paint outerPaint;
    Paint innerPaint;
    Paint textPaint;
    Path lightningPath;
    int level;
    int perc;
    boolean charging;
    float textSize;
    float borderWidth;
    BatteryState lastState;


    float p;
    boolean c;


    public BatteryView(Context context) {
        super(context);
        init();
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        float textDip = 18f;
        float borderDip = 10f;

        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textDip,
                        getResources().getDisplayMetrics());
        borderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderDip,
                getResources().getDisplayMetrics());

        borderWidth = 10;
        level = 2;

        // Init paints
        innerPaint = new Paint();
        innerPaint.setColor(getResources().getColor(R.color.battery5));
        innerPaint.setStrokeWidth(borderWidth);
        innerPaint.setStrokeCap(Paint.Cap.ROUND);

        outerPaint = new Paint();
        outerPaint.setColor(getResources().getColor(R.color.whiteHigh));
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(borderWidth);
        outerPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.whiteHigh));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);

        updateColor();
        
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                if (c) {
                    p += 0.01;
                    if (p > 1) {
                        p = 1;
                        c = false;
                    }

                } else {
                    p -= 0.01;
                    if (p < 0) {
                        p = 0;
                        c = true;
                    }
                }

                charging = c;

                updatePercentage(p);
                invalidate();

                handler.postDelayed(this, 100);
            }
        };

        runnable.run();
    }

    @Override
    public void onNewMessage(Message message) {
        super.onNewMessage(message);

        lastState = (BatteryState)message;

        this.charging = lastState.getPowerSupplyStatus() == BatteryState.POWER_SUPPLY_STATUS_CHARGING;
        this.updatePercentage(lastState.getPercentage());
        this.invalidate();
    }

    private void updatePercentage(float value) {
        perc = (int)(value * 100);
        level = Math.min(5, perc / 20 + 1);
        updateColor();
    }

    private void updateColor() {
        int color;

        if (level == 1)         color = R.color.battery1;
        else if (level == 2)    color = R.color.battery2;
        else if (level == 3)    color = R.color.battery3;
        else if (level == 4)    color = R.color.battery4;
        else                    color = R.color.battery5;

        innerPaint.setColor(getResources().getColor(color));
    }

    @Override
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float middleX = width/2;
        float middleY = height/2;

        float left = borderWidth/2;
        float right = width - borderWidth/2;
        float top = borderWidth * 2;
        float bottom = height - borderWidth - textSize;

        // Draw pad
        canvas.drawRoundRect(middleX - 30, top - borderWidth,
                middleX +30, top,
                borderWidth, borderWidth,
                outerPaint);

        // Draw body
        canvas.drawRoundRect(left, top, right, bottom, borderWidth, borderWidth, outerPaint);

        if (this.charging) {
            // Draw lightning
            float batWidth = right - left;
            float batHeight = bottom - top;
            middleX = (right + left) /2;
            middleY = (bottom + top) /2;
            float partWidth = batWidth / 5;
            float partHeight = batHeight / 6;


            float[][] path = new float[][]{
                    {middleX, middleY - partHeight},
                    {middleX - partWidth, middleY + partHeight/5},
                    {middleX + partWidth, middleY - partHeight/5},
                    {middleX, middleY + partHeight}
            };

            for(int i = 0; i < path.length-1; i++) {
                canvas.drawLine(path[i][0], path[i][1], path[i+1][0], path[i+1][1], innerPaint);
            }

        } else {
            // Draw Bat level
            float innerLeft = left + borderWidth * 1.5f;
            float innerRight = right - borderWidth * 1.5f;
            float innerTop = top + borderWidth * 1.5f;
            float innerBottom = bottom - borderWidth * 1.5f;
            float heightStep = (innerBottom - innerTop + borderWidth) / MAX_LEVEL;

            for (int i = 0; i < level; i++) {
                float b = innerBottom - heightStep * i;
                float t = b - heightStep + borderWidth;

                canvas.drawRect(innerLeft, t, innerRight, b, innerPaint);
            }
        }

        // Draw status text
        String percText = perc + "%";
        canvas.drawText(percText, middleX, height, textPaint);
    }
}
