package com.schneewittchen.rosandroid.widgets.gridmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.text.SymbolTable;
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
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class GridMapView extends BaseView {

    public static final String TAG = "GridmapView";

    // Rectangle Surrounding
    Paint paint;
    float cornerWidth;

    // Grid Map Information
    GridMapData data;

    // Zoom Parameters, TODO: Add the parameters to details?
    private static float MIN_ZOOM = 1f;         // min. and max. zoom
    private static float MAX_ZOOM = 20f;
    private float scaleFactor = 1.f;
    private int posX = 0;
    private int posY = 0;
    private float dragSensitivity = 0.1f;
    private ScaleGestureDetector detector;

    private static int NONE = 0;                // mode
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;

    private float startX = 0f;                  // finger position tracker
    private float startY = 0f;

    private float translateX = 0f;              // Amount of translation
    private float translateY = 0f;

    private RectF drawRect;


    public GridMapView(Context context) {
        super(context);
        init();
    }

    public GridMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.cornerWidth = Utils.dpToPx(getContext(), 8);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        // Initialize the rectangular for drawing
        drawRect = new RectF(0, 0, 0, 0);
        // Initialize thee gesture detector for zooming in and out
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean dragged = false;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // Activated if the user first touches the screen
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                startX = event.getX();
                startY = event.getY();
                break;
            // If finger moved
            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
                double distance = Math.sqrt(Math.pow(event.getX() - startX, 2) +
                                            Math.pow(event.getY() - startY, 2)
                );
                if(distance > 0) {
                    dragged = true;
                }
                break;
            // Zooming
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;
                break;
        }
        // Activate the gesture detector (for zoom)
        detector.onTouchEvent(event);
        // We draw the canvas if required
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
        float top = 0F;
        float width = getWidth();
        float height = getHeight();
        // Do the canvas drawing
        drawRect.set(left, top, width, height);
        int scaledWidth;
        int scaledHeight;
        if (data != null) {
            // Zoom
            scaledWidth = (int) (data.map.getWidth()/scaleFactor);
            scaledHeight = (int) (data.map.getHeight()/scaleFactor);
            // Translate
            posX = posX - (int) ((translateX / scaleFactor) * dragSensitivity);
            posY = posY - (int) ((translateY / scaleFactor) * dragSensitivity);
            if (posX + scaledWidth > data.map.getWidth()) {
                posX = data.map.getWidth() - scaledWidth;
            } else if (posX < 0) {
                posX = 0;
            }
            if (posY + scaledHeight > data.map.getHeight()) {
                posY = data.map.getHeight() - scaledHeight;
            } else if (posY < 0) {
                posY = 0;
            }
            // Get the Submap from the Bitmap
            Bitmap subMap = Bitmap.createBitmap(data.map, posX, posY,
                                                scaledWidth, scaledHeight);
            // Scale the Submap to the Viz size for optimal displaying
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(subMap, (int) width, (int) height, true);
            canvas.drawBitmap(scaledBitmap, 0, 0, null);
        } else {
            canvas.drawRoundRect(drawRect, cornerWidth, cornerWidth, paint);
        }
        // Apply the changes
        canvas.restore();
        // Put a rectangle around
        canvas.drawRoundRect(drawRect, cornerWidth, cornerWidth, paint);
    }

    @Override
    public void setData(BaseData data) {
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