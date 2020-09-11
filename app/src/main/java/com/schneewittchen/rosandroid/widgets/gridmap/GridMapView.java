package com.schneewittchen.rosandroid.widgets.gridmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.MatrixGestureDetector;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 18.10.19
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class GridMapView extends BaseView implements View.OnTouchListener {

    public static final String TAG = "GridmapView";

    // Grid Map Information
    GridMapData data;

    // Rectangle Surrounding
    Paint paint;
    float cornerWidth;
    private RectF drawRect;

    private Matrix matrix;
    MatrixGestureDetector gestureDetector;


    public GridMapView(Context context) {
        super(context);
        init();
    }

    public GridMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        matrix = new Matrix();
        gestureDetector = new MatrixGestureDetector(matrix, matrix -> {
            this.invalidate();
        });

        this.cornerWidth = Utils.dpToPx(getContext(), 8);

        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.whiteHigh));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(10);

        this.drawRect = new RectF(0, 0, 0, 0);

        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data != null && data.map != null) {
            canvas.drawBitmap(data.map, matrix, null);
        }

        // Border the view
        float width = getWidth();
        float height = getHeight();

        // Do the canvas drawing
        drawRect.set(0, 0, width, height);

        canvas.drawRoundRect(drawRect, cornerWidth, cornerWidth, paint);
    }

    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setData(BaseData newData) {
        this.data = (GridMapData) newData;
        this.invalidate();
    }

}