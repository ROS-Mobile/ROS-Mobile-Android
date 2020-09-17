package com.schneewittchen.rosandroid.widgets.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseView;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.1
 * @created on 27.04.19
 * @updated on 10.09.2020
 * @modified by Nico Studt
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class CameraView extends BaseView {

    public static final String TAG = CameraView.class.getSimpleName();

    Paint paint;
    float cornerWidth;
    CameraData data;
    RectF imageRect = new RectF();


    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    public void onDraw(Canvas canvas) {
        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        float width = widthViz;
        float height = heightViz;
        float left = leftViz;
        float top = topViz;

        if (data != null) {
            float mapRatio = (float)data.map.getHeight() / data.map.getWidth();
            float vizRatio = heightViz/widthViz;

            if (mapRatio > vizRatio) {
                height = heightViz;
                width = (vizRatio/mapRatio) * widthViz;
                left = 0.5F * (widthViz - width);

            } else if (vizRatio > mapRatio) {
                width = widthViz;
                height = (mapRatio/vizRatio) * heightViz;
                top = 0.5F * (heightViz -height);
            }

            imageRect.set(left, top, left + width, top + height);
            canvas.drawBitmap(data.map, null, imageRect, paint);
        }

        // Draw Border
        canvas.drawRoundRect(leftViz, topViz, width, height, cornerWidth, cornerWidth, paint);
    }

    @Override
    public void setData(BaseData data) {
        this.data = (CameraData) data;
        this.invalidate();
    }

}