package com.schneewittchen.rosandroid.widgets.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.ros.internal.message.Message;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.1
 * @created on 27.04.19
 * @updated on 20.10.2020
 * @modified by Nico Studt
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class CameraView extends SubscriberWidgetView {

    public static final String TAG = CameraView.class.getSimpleName();

    private Paint borderPaint;
    private Paint paintBackground;
    private float cornerWidth;
    private CameraData data;
    private RectF imageRect = new RectF();


    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    
    private void init() {
        this.cornerWidth = 0; //Utils.dpToPx(getContext(), 8);

        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(R.color.borderColor));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);

        // Background color
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(100, 0, 0, 0));
        paintBackground.setStyle(Paint.Style.FILL);
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

        float width = widthViz;
        float height = heightViz;
        float left = leftViz;
        float top = topViz;

        if (data != null) {
            float mapRatio = (float)data.map.getHeight() / data.map.getWidth();
            float vizRatio = heightViz/widthViz;

            if (mapRatio >= vizRatio) {
                height = heightViz;
                width = (vizRatio/mapRatio) * widthViz;
                left = 0.5F * (widthViz - width);

            } else if (vizRatio > mapRatio) {
                width = widthViz;
                height = (mapRatio/vizRatio) * heightViz;
                top = 0.5F * (heightViz -height);
            }

            imageRect.set(left, top, left + width, top + height);
            canvas.drawBitmap(data.map, null, imageRect, borderPaint);
        }

        // Draw Border
        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, borderPaint);
    }

    @Override
    public void onNewMessage(Message message) {
        this.data = null;

        if(message instanceof CompressedImage) {
            this.data = new CameraData((CompressedImage) message);
        } else if (message instanceof Image) {
            this.data = new CameraData((Image) message);
        } else {
            return;
        }
        
        this.invalidate();
    }
    
}