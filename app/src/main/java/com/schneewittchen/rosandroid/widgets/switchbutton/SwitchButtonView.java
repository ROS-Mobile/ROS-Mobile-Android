package com.schneewittchen.rosandroid.widgets.switchbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;
import com.schneewittchen.rosandroid.utility.Utils;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.05.2022
 */
public class SwitchButtonView extends PublisherWidgetView {

    public static final String TAG = SwitchButtonView.class.getSimpleName();

    float cornerRadius = 10;
    float lineWidth;
    Paint switchPaintOn;
    Paint switchPaintOff;
    TextPaint textPaintOn;
    TextPaint textPaintOff;
    boolean switchState = false;


    public SwitchButtonView(Context context) {
        super(context);
        init();
    }

    public SwitchButtonView(Context context, @Nullable AttributeSet attrs) {
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

        switchPaintOff = new Paint();
        switchPaintOff.setColor(getResources().getColor(R.color.colorPrimary));
        switchPaintOff.setStyle(Paint.Style.FILL);

        textPaintOn = new TextPaint();
        textPaintOn.setTextAlign(Paint.Align.CENTER);
        textPaintOn.setColor(getResources().getColor(R.color.colorPrimary));
        textPaintOn.setTextSize(20 * getResources().getDisplayMetrics().density);
        textPaintOn.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        switchPaintOn = new Paint();
        switchPaintOn.setColor(getResources().getColor(R.color.colorPrimary));
        switchPaintOn.setStyle(Paint.Style.STROKE);
        switchPaintOn.setStrokeWidth(lineWidth);
    }

    private void changeState() {
        this.switchState = !switchState;
        this.publishViewData(new SwitchButtonData(this.switchState));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (super.onTouchEvent(event)) {
            return true;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            changeState();
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        SwitchButtonEntity entity = (SwitchButtonEntity) widgetEntity;
        float width = getWidth();
        float height = getHeight();

        String text = switchState? entity.onText : entity.offText;
        Paint innerSwitchPaint = switchState ? switchPaintOn : switchPaintOff;
        TextPaint textPaint = switchState ? textPaintOn : textPaintOff;

        float offset = (switchState? lineWidth : 0) / 2f;
        float baseline = (textPaint.descent() + textPaint.ascent()) / 2f;
        float xPos = width / 2;
        float yPos = height / 2f - baseline;

        canvas.drawRoundRect(offset, offset, width - offset, height - offset, cornerRadius, cornerRadius, innerSwitchPaint);
        canvas.save();
        canvas.drawText(text, xPos, yPos, textPaint);
        canvas.restore();
    }
}
