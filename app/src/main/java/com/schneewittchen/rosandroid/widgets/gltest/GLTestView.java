package com.schneewittchen.rosandroid.widgets.gltest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.common.collect.Lists;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.gltest.layer.LaserScanLayer;
import com.schneewittchen.rosandroid.widgets.gltest.layer.Layer;
import com.schneewittchen.rosandroid.widgets.gltest.layer.OccupancyGridLayer;
import com.schneewittchen.rosandroid.widgets.gltest.layer.PoseSubscriberLayer;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.internal.message.Message;

import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovarianceStamped;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 07.03.2021
 */
public class GLTestView extends SubscriberView {

    public static final String TAG = GLTestView.class.getSimpleName();

    private Button button;
    private Paint borderPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private VisualizationView layerView;


    public GLTestView(Context context) {
        super(context);
        init();
    }

    public GLTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layerView.layout(0, 0, getWidth(), getHeight());
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


        layerView = new VisualizationView(getContext());

        layerView.onCreate(Lists.newArrayList(
                new OccupancyGridLayer("map")
                ));
        this.addView(layerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return layerView.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "OnDraw");
        super.onDraw(canvas);

        canvas.drawPaint(paintBackground);

        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        //button.draw(canvas);
        layerView.requestRender();

        // Draw Border
        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, borderPaint);
    }


    @Override
    public void onNewMessage(Message message) {
        layerView.onNewMessage(message);
    }

}