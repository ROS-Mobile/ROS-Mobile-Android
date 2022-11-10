package com.schneewittchen.rosandroid.widgets.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;
import com.schneewittchen.rosandroid.utility.Utils;


/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 * @updated on 10.03.2021
 * @modified by Nico Studt
 */

public class ButtonView extends PublisherWidgetView {

    public static final String TAG = ButtonView.class.getSimpleName();

    float cornerRadius = 10;
    float lineWidth;
    Paint buttonPaintOn;
    Paint buttonPaintOff;
    TextPaint textPaintOn;
    TextPaint textPaintOff;

    boolean pressed = false;


    public ButtonView(Context context) {
        super(context);
        init();
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        lineWidth = Utils.dpToPx(getContext(), 3);

        textPaintOff = new TextPaint();
        textPaintOff.setTextAlign(Paint.Align.CENTER);
        textPaintOff.setColor(getResources().getColor(R.color.colorPrimaryDark));
        textPaintOff.setTextSize(20 * getResources().getDisplayMetrics().density);
        textPaintOff.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        buttonPaintOff = new Paint();
        buttonPaintOff.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaintOff.setStyle(Paint.Style.FILL);

        textPaintOn = new TextPaint();
        textPaintOn.setTextAlign(Paint.Align.CENTER);
        textPaintOn.setColor(getResources().getColor(R.color.colorPrimary));
        textPaintOn.setTextSize(20 * getResources().getDisplayMetrics().density);
        textPaintOn.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        buttonPaintOn = new Paint();
        buttonPaintOn.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaintOn.setStyle(Paint.Style.STROKE);
        buttonPaintOn.setStrokeWidth(lineWidth);
    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);
        invalidate();
    }

    private void changeState(boolean pressed) {
        this.pressed = pressed;
        this.publishViewData(new ButtonData(pressed));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (super.onTouchEvent(event)) {
            return true;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                changeState(false);
                break;
            case MotionEvent.ACTION_DOWN:
                changeState(true);
                break;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ButtonEntity entity = (ButtonEntity) widgetEntity;
        float width = getWidth();
        float height = getHeight();

        TextPaint textPaint = pressed ? textPaintOn : textPaintOff;
        Paint innerSwitchPaint = pressed ? buttonPaintOn : buttonPaintOff;

        float offset = (pressed ? lineWidth : 0) / 2f;
        float baseline = (textPaint.descent() + textPaint.ascent()) / 2f;
        float xPos = width / 2;
        float yPos = height / 2f - baseline;

        canvas.drawRoundRect(offset, offset, width - offset, height - offset, cornerRadius, cornerRadius, innerSwitchPaint);
        canvas.save();
        canvas.rotate(entity.rotation, width / 2, height / 2);
        canvas.drawText(entity.text, xPos, yPos, textPaint);
        canvas.restore();
    }
}
