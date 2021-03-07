package com.schneewittchen.rosandroid.widgets.gltest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 07.03.2021
 */
public class GLTestView extends SubscriberView {

    public static final String TAG = GLTestView.class.getSimpleName();

    private Paint borderPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private GLSurfaceView glSurfaceView;


    public GLTestView(Context context) {
        super(context);
        init();
    }

    public GLTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    
    private void init() {
        this.cornerWidth = Utils.dpToPx(getContext(), 4);

        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(R.color.whiteHigh));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);

        // Background color
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(100, 0, 0, 0));
        paintBackground.setStyle(Paint.Style.FILL);

        glSurfaceView = new GLSurfaceView(getContext());
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(paintBackground);

        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();


        // Draw Border
        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, borderPaint);
    }

    @Override
    public void onNewMessage(Message message) {
        this.invalidate();
    }
    
}