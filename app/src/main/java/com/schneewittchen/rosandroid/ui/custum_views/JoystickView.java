package com.schneewittchen.rosandroid.ui.custum_views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 18.10.19
 * @updated on 10.01.20
 * @modified by
 */
public class JoystickView extends View {

    Handler mHandler;
    Runnable mRunnable;

    long updateTime = 100L;
    long animationTime = 300L;
    ValueAnimator animator;

    UpdateListener updateListener;

    Paint outerPaint;
    Paint linePaint;
    Paint joystickPaint;

    float joystickRadius;
    float posX;
    float posY;
    float lastPosX;
    float lastPosY;


    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }


    private void init(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.JoystickView, 0, 0);

        int joystickColor = a.getColor(R.styleable.JoystickView_stickColor,
                                getResources().getColor(R.color.colorAccent));
        int outerColor = a.getColor(R.styleable.JoystickView_outerRingColor,
                                getResources().getColor(R.color.colorPrimary));
        int lineColor = a.getColor(R.styleable.JoystickView_decorationColor,
                                getResources().getColor(R.color.colorPrimaryDark));
        a.recycle();

        joystickRadius = Utils.cmToPx(getContext(), 1)/2;
        joystickPaint = new Paint();
        joystickPaint.setColor(joystickColor);

        outerPaint = new Paint();
        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(Utils.dpToPx(getContext(), 3));

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(Utils.dpToPx(getContext(), 2));

        // Run timer
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                updateListeners();
                mHandler.postDelayed(this, updateTime);
            }
        };
    }

    // Move to polarCoordinates
    private void moveTo(float x, float y){
        posX = x;
        posY = y;

        // Redraw
        invalidate();
    }


    private void updateListeners(){
        if(updateListener != null){
            updateListener.onUpdate(posX, posY);
        }

        lastPosX = posX;
        lastPosY = posY;
    }

    public void setUpdateListener(UpdateListener listener){
        this.updateListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        float[] polars = convertFromPxToPolar(eventX, eventY);

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                moveTo(0, 0);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                moveTo(polars[0], polars[1]);
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

        float[] px = convertFromPolarToPx(posX, posY);

        // Outer ring
        canvas.drawCircle(width/2, height/2, width/2-joystickRadius, outerPaint);

        // Inner drawings
        canvas.drawCircle(width/2, height/2, width/4-joystickRadius/2, linePaint);
        canvas.drawLine(joystickRadius, height/2, width-joystickRadius, height/2,  linePaint);
        canvas.drawLine(width/2, joystickRadius , width/2, height-joystickRadius,  linePaint);

        // Stick
        canvas.drawCircle(px[0], px[1], joystickRadius, joystickPaint);
    }


    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.postDelayed(mRunnable, updateTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacks(mRunnable);
        super.onDetachedFromWindow();
    }


    private float[] convertFromPxToPolar(float x, float y) {
        float middleX = getWidth()/2f;
        float middleY = getHeight()/2f;
        float r = middleX -joystickRadius;

        float dx = x - middleX;
        float dy = y - middleY;
        double rad = Math.atan2(dy, dx);

        double len = Math.sqrt(dx*dx + dy*dy)/r;
        len = Math.min(1, len);

        float[] polar = new float[2];

        polar[0] = (float) (Math.cos(rad)*len);
        polar[1] = (float) (-Math.sin(rad)*len);

        return polar;
    }

    private float[] convertFromPolarToPx(float x, float y){
        float middleX = getWidth()/2f;
        float middleY = getHeight()/2f;
        float r = middleX -joystickRadius;

        float[] px = new float[2];
        px[0] = middleX + x*r;
        px[1] = middleY - y*r;

        return px;
    }


    public interface UpdateListener {

        void onUpdate(float x, float y);
    }
}
