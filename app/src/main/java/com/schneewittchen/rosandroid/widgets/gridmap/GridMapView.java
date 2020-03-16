package com.schneewittchen.rosandroid.widgets.gridmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 18.10.19
 * @updated on 10.01.20
 * @modified by
 */
public class GridMapView extends View {

    public static final String TAG = "GridmapView";

    Paint paint;
    float cornerWidth;

    public GridMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.cornerWidth = Utils.dpToPx(getContext(), 8);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }


    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "On draw");
        float width = getWidth();
        float height = getHeight();
        canvas.drawRoundRect(2, 2, width-2, height-2,cornerWidth, cornerWidth, paint);
    }

}