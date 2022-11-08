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

    Paint switchOnPaint;
    Paint switchOffPaint;

    Paint innerSwitchOnPaint;
    Paint innerSwitchOffPaint;
    Paint thumbSwitchOnPaint;
    Paint thumbSwitchOffPaint;
    TextPaint textPaintOn;
    TextPaint textPaintOff;

    StaticLayout staticLayout;
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
        switchOnPaint = new Paint();
        switchOnPaint.setColor(getResources().getColor(R.color.colorPrimary));
        switchOnPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        switchOffPaint = new Paint();
        switchOffPaint.setColor(getResources().getColor(R.color.colorPrimary));
        switchOffPaint.setStyle(Paint.Style.STROKE);
        switchOffPaint.setStrokeWidth(Utils.dpToPx(getContext(), 3));

        textPaintOn = new TextPaint();
        textPaintOn.setColor(getResources().getColor(R.color.colorPrimaryDark));
        textPaintOn.setTextSize(26 * getResources().getDisplayMetrics().density);
        textPaintOn.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        textPaintOff = new TextPaint();
        textPaintOff.setColor(getResources().getColor(R.color.colorPrimary));
        textPaintOff.setTextSize(26 * getResources().getDisplayMetrics().density);
        textPaintOff.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        thumbSwitchOnPaint = new Paint();
        thumbSwitchOnPaint.setColor(getResources().getColor(R.color.battery1));
        thumbSwitchOnPaint.setStyle(Paint.Style.FILL);

        innerSwitchOnPaint = new Paint();
        innerSwitchOnPaint.setColor(getResources().getColor(R.color.battery2));
        innerSwitchOnPaint.setStyle(Paint.Style.FILL);

        thumbSwitchOffPaint = new Paint();
        thumbSwitchOffPaint.setColor(getResources().getColor(R.color.battery3));
        thumbSwitchOffPaint.setStyle(Paint.Style.FILL);

        innerSwitchOffPaint = new Paint();
        innerSwitchOffPaint.setColor(getResources().getColor(R.color.battery5));
        innerSwitchOffPaint.setStyle(Paint.Style.FILL);

    }

    private void changeState() {
        this.switchState = !switchState;
        this.publishViewData(new SwitchButtonData(this.switchState));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, event.toString());

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            float eventX = event.getX();
            float eventY = event.getY();
            float width = getWidth();
            float height = getHeight();

            if (eventX > 0 && eventX < width && eventY > 0 && eventY < height)
                changeState();
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        Paint innerSwitchPaint = switchState ? switchOnPaint : switchOffPaint;
        canvas.drawRoundRect(5, 5, width - 10, height - 10, 10, 10, innerSwitchPaint);

        SwitchButtonEntity entity = (SwitchButtonEntity) widgetEntity;
        String entityText = entity == null ? "Switch" : entity.text;

        TextPaint textPaint = switchState ? textPaintOn : textPaintOff;
        staticLayout = new StaticLayout(entityText, textPaint,
                (int) width, Layout.Alignment.ALIGN_CENTER,
                1.0f, 0, false);

        canvas.save();
        canvas.translate(((width - staticLayout.getWidth()) / 2f), (height - staticLayout.getHeight()) / 2f);

        staticLayout.draw(canvas);
        canvas.restore();
    }
}
