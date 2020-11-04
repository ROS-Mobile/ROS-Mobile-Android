package com.schneewittchen.rosandroid.widgets.gridmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.general.MatrixGestureDetector;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.ros.internal.message.Message;

import nav_msgs.OccupancyGrid;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 18.10.19
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class GridMapView extends SubscriberView implements View.OnTouchListener {

    public static final String TAG = "GridmapView";

    // Grid Map Information
    private GridMapData data;

    // Rectangle Surrounding
    private Paint borderPaint;
    private Paint gridPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private RectF drawRect;
    private Matrix matrix;
    private MatrixGestureDetector gestureDetector;


    public GridMapView(Context context) {
        super(context);
        init();
    }

    public GridMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        this.matrix = new Matrix();
        this.matrix.setScale(1, -1);

        this.gestureDetector = new MatrixGestureDetector(matrix, matrix -> {
            this.invalidate();
        });

        this.cornerWidth = Utils.dpToPx(getContext(), 8);

        this.borderPaint = new Paint();
        this.borderPaint.setColor(getResources().getColor(R.color.whiteHigh));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(10);

        this.gridPaint = new Paint();
        this.gridPaint.setColor(getResources().getColor(R.color.whiteHigh));
        this.gridPaint.setAntiAlias(false);
        this.gridPaint.setFilterBitmap(false);

        // Background color
        this.paintBackground = new Paint();
        this.paintBackground.setColor(Color.argb(100, 0, 0, 0));
        this.paintBackground.setStyle(Paint.Style.FILL);

        this.drawRect = new RectF(0, 0, 0, 0);

        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(paintBackground);

        if (data != null && data.map != null) {
            //canvas.drawBitmap(data.map, 0, 0, gridPaint);
            canvas.drawBitmap(data.map, matrix, gridPaint);
        }

        // Border the view
        float width = getWidth();
        float height = getHeight();

        // Do the canvas drawing
        drawRect.set(0, 0, width, height);

        canvas.drawRoundRect(drawRect, cornerWidth, cornerWidth, borderPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onNewMessage(Message message) {
        this.data = new GridMapData((OccupancyGrid) message);
        this.invalidate();
    }
}