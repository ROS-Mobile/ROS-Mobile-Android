package com.schneewittchen.rosandroid.widgets.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;
import com.schneewittchen.rosandroid.utility.Utils;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 18.10.19
 */
public class JoystickView extends PublisherWidgetView {

    public static final String TAG = JoystickView.class.getSimpleName();

    Paint outerPaint;
    Paint linePaint;
    Paint joystickPaint;

    float joystickRadius;
    float posX;
    float posY;


    public JoystickView(Context context) {
        super(context);
        init();
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        joystickRadius = Utils.cmToPx(getContext(), 1)/2;
        joystickPaint = new Paint();
        joystickPaint.setColor(getResources().getColor(R.color.colorAccent));

        outerPaint = new Paint();
        outerPaint.setColor(getResources().getColor(R.color.colorPrimary));
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeWidth(Utils.dpToPx(getContext(), 3));

        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.colorPrimary));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAlpha(50);
        linePaint.setStrokeWidth(Utils.dpToPx(getContext(), 2));
    }

    // Move to polarCoordinates
    private void moveTo(float x, float y){
        posX = x;
        posY = y;
        this.publishViewData(new JoystickData(posX, posY));

        // Redraw
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }

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
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        float[] px = convertFromPolarToPx(posX, posY);

        // Outer ring
        canvas.drawCircle(width/2, height/2, width/2-joystickRadius, outerPaint);

        // Inner drawings
        canvas.drawCircle(width/2, height/2, width/4-joystickRadius/2, linePaint);
        canvas.drawLine(joystickRadius, height/2, width-joystickRadius, height/2,  linePaint);
        canvas.drawLine(width/2, height/2 - width/2 + joystickRadius ,
                        width/2, height/2 + width/2 - joystickRadius,  linePaint);

        // Stick
        canvas.drawCircle(px[0], px[1], joystickRadius, joystickPaint);
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
}
