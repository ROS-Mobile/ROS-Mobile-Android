package com.schneewittchen.rosandroid.widgets.gridmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.helper.RecyclerItemTouchHelper;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 18.10.19
 * @updated on 27.04.20
 * @modified by Nils Rottmann
 */
public class GridMapView extends BaseView {

    public static final String TAG = "GridmapView";

    // Rectangle Surrounding
    Paint paint;
    float cornerWidth;

    // Grid Map Information
    GridMapData data;

    // Zoom Parameters
    private static float MIN_ZOOM = 1f;         // min. and max. zoom
    private static float MAX_ZOOM = 10f;
    private float scaleFactor = 1.f;
    private ScaleGestureDetector detector;

    private static int NONE = 0;                // mode
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;

    private float startX = 0f;                  // finger position tracker
    private float startY = 0f;

    private float translateX = 0f;              // Amount of translation
    private float translateY = 0f;

    private float previousTranslateX = 0f;      // Past amount of translation
    private float previousTranslateY = 0f;

    public GridMapView(Context context) {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    public GridMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    private void init() {
        this.cornerWidth = Utils.dpToPx(getContext(), 8);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean dragged = false;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;
                break;

            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                //We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
                //This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                                            Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if(distance > 0) {
                    dragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;

                //All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and
                //previousTranslate
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                //This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
                //and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);

        //We redraw the canvas only in the following cases:
        //
        // o The mode is ZOOM
        //        OR
        // o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
        //   set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            this.invalidate();
        }

        return true;
    }



    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "On Draw");
        super.onDraw(canvas);
        canvas.save();

        // Get vizualization size
        float left = 0F;
        float right = 0F;
        float width = getWidth();
        float height = getHeight();

        // Scale the X and Y cordinates
        canvas.scale(scaleFactor, scaleFactor);
        // Take care of the bounds
        if((translateX * -1) < 0) {
            translateX = 0;
        } else if((translateX * -1) > (scaleFactor - 1) * width) {
            translateX = (1 - scaleFactor) * width;
        }
        if(translateY * -1 < 0) {
            translateY = 0;
        } else if((translateY * -1) > (scaleFactor - 1) * height) {
            translateY = (1 - scaleFactor) * height;
        }
        // Divide by scale factor to avoid panning
        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);

        // Do the canvas drawing
        RectF rect = new RectF(left, right, width, height);
        if (data != null) {
            canvas.drawBitmap(data.map, 0, 0, null);
            // canvas.drawBitmap(data.map, null, rect, paint);
            // canvas.drawRoundRect(left, right, width, height, cornerWidth, cornerWidth, paint);
        } else {
            canvas.drawRoundRect(left, right, width, height, cornerWidth, cornerWidth, paint);
        }
        // Apply the changes
        canvas.restore();
        // Put a rectangle around
        canvas.drawRoundRect(left, right, width, height, cornerWidth, cornerWidth, paint);
    }

    @Override
    public void setData(BaseData data) {
        System.out.println("GridMapView: SetData!");
        this.data = (GridMapData) data;
        this.invalidate();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;
        }
    }

}