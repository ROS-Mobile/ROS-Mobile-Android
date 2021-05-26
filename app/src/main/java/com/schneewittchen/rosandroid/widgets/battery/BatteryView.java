package com.schneewittchen.rosandroid.widgets.battery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
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
    int level;
    boolean charging;
    float textSize;
    float borderWidth;
    String displayedText;
    private boolean displayVoltage;


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
        level = 3;

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

    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);

        BatteryEntity entity = (BatteryEntity) widgetEntity;
        this.displayVoltage = entity.displayVoltage;

        if (displayVoltage) {
            this.updateVoltage(0f);
        } else {
            this.updatePercentage(0f);
        }
    }

    @Override
    public void onNewMessage(Message message) {
        super.onNewMessage(message);

        BatteryState state = (BatteryState)message;

        this.charging = state.getPowerSupplyStatus() == BatteryState.POWER_SUPPLY_STATUS_CHARGING;

        if (displayVoltage) {
            this.updateVoltage(state.getVoltage());
        } else {
            this.updatePercentage(state.getPercentage());
        }
        this.invalidate();
    }

    private void updatePercentage(float value) {
        int perc = (int)(value * 100);
        displayedText = perc + "%";
        level = Math.min(5, perc / 20 + 1);
        updateColor();
    }

    private void updateVoltage(float value) {
        if (value >= 10) {
            displayedText = String.format("%.1fV", value);
        } else {
            displayedText = String.format("%.2fV", value);
        }

        level = -1;
        updateColor();
    }

    private void updateColor() {
        int color;

        if (level == 1)         color = R.color.battery1;
        else if (level == 2)    color = R.color.battery2;
        else if (level == 3)    color = R.color.battery3;
        else if (level == 4)    color = R.color.battery4;
        else if (level == 5)    color = R.color.battery5;
        else                    color = R.color.colorPrimary;

        innerPaint.setColor(getResources().getColor(color));
    }

    @Override
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float middleX = width/2;

        float left = borderWidth/2;
        float right = width - borderWidth/2;
        float top = borderWidth * 2;
        float bottom = height - borderWidth - textSize;

        // Draw pad
        canvas.drawRoundRect(middleX - width/4, top - borderWidth,
                middleX + width/4, top,
                borderWidth, borderWidth,
                outerPaint);

        // Draw body
        canvas.drawRoundRect(left, top, right, bottom, borderWidth, borderWidth, outerPaint);

        if (this.charging) {
            // Draw lightning
            float batWidth = right - left;
            float batHeight = bottom - top;
            middleX = (right + left) /2;
            float middleY = (bottom + top) /2;
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
            int batLevel = level == -1? 5 : level;
            float innerLeft = left + borderWidth * 1.5f;
            float innerRight = right - borderWidth * 1.5f;
            float innerTop = top + borderWidth * 1.5f;
            float innerBottom = bottom - borderWidth * 1.5f;
            float heightStep = (innerBottom - innerTop + borderWidth) / MAX_LEVEL;

            for (int i = 0; i < batLevel; i++) {
                float b = innerBottom - heightStep * i;
                float t = b - heightStep + borderWidth;

                canvas.drawRect(innerLeft, t, innerRight, b, innerPaint);
            }
        }

        // Draw status text
        canvas.drawText(displayedText, middleX, height, textPaint);
    }
}
