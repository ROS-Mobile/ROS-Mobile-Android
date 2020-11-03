package com.schneewittchen.rosandroid.widgets.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickView;

import androidx.annotation.Nullable;

public class ButtonView extends BaseView {
    public static final String TAG = "ButtonView";

    JoystickView.UpdateListener updateListener;

    Paint buttonPaint;
    Paint textPaint;
    ButtonState state;
    float sizeX;
    float sizeY;

    public ButtonView(Context context) {
        super(context);
        init();
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private  void changeState(ButtonState state) {
        this.informDataChange(new ButtonData(state));
        this.state = state;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                changeState(ButtonState.Idle);
                break;
            case MotionEvent.ACTION_DOWN:
                changeState(ButtonState.Pressed);
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

        canvas.drawRect(new Rect(0,0,(int)width,(int)height),buttonPaint);
    }
}
