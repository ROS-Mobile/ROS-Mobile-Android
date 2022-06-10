package com.schneewittchen.rosandroid.widgets.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;

import androidx.annotation.Nullable;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.05.2022
 */
public class SwitchView extends PublisherWidgetView {

    public static final String TAG = SwitchView.class.getSimpleName();

    Paint innerSwitchOnPaint;
    Paint innerSwitchOffPaint;
    Paint thumbSwitchOnPaint;
    Paint thumbSwitchOffPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;

    boolean switchState;


    public SwitchView(Context context) {
        super(context);
        init();
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        thumbSwitchOnPaint = new Paint();
        switchPaint.setColor(getResources().getColor(R.color.colorPrimary));
        switchPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerSwitchOnPaint = new Paint();
        switchPaint.setColor(getResources().getColor(R.color.colorSecondary));
        switchPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        thumbSwitchOffPaint = new Paint();
        switchPaint.setColor(getResources().getColor(R.color.colorPrimary));
        switchPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerSwitchOffPaint = new Paint();
        switchPaint.setColor(getResources().getColor(R.color.colorSecondary));
        switchPaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
    }

    private void changeState(boolean pressed) {
        this.publishViewData(new SwitchData(pressed));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                switchPaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(false);
                break;
            case MotionEvent.ACTION_DOWN:
                switchPaint.setColor(getResources().getColor(R.color.color_attention));
                changeState(true);
                break;
            default:
                return false;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;
        float thumbRadius = height;

        SwitchEntity entity = (SwitchEntity) widgetEntity;

        Paint innerSwitchPaint = entity.pressed? innerSwitchOnPaint : innerSwitchOffPaint;
        Paint thumbSwitchPaint = entity.pressed? thumbSwitchOnPaint : thumbSwitchOffPaint;

        float thumbPosX = 0;
        float thumbPosY = height / 2f;

        // Draw inner switch
        canvas.drawRoundedRect(new Rect(0, 0, (int) width, (int) height), 10, innerSwitchPaint);

        // Draw thumb button
        canvas.drawCircle(thumbPosX, thumbPosY, thumbRadius, thumbSwitchPaint);


        staticLayout = new StaticLayout(entity.text, textPaint,
                (int) textLayoutWidth, Layout.Alignment.ALIGN_CENTER,
                1.0f, 0, false);
                
        canvas.save();
        canvas.rotate(entity.rotation, width / 2, height / 2);
        canvas.translate(((width / 2) - staticLayout.getWidth() / 2), height / 2 - staticLayout.getHeight() / 2);

        staticLayout.draw(canvas);
        canvas.restore();
    }
}
