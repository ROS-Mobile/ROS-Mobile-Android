package com.schneewittchen.rosandroidlib.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroidlib.R;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 18.10.19
 * @updated on 10.01.20
 * @modified by
 */
public class OccupancyGridWidget extends View {

    Paint paint;


    public OccupancyGridWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }


    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        canvas.drawRect(5, 5, width-5, height-5,  paint);
    }

}