package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;

import org.ros.internal.message.Message;
import org.ros.internal.message.field.Field;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import std_msgs.Header;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 29.05.21
 */
public class RqtPlotView extends SubscriberWidgetView {

    public static final String TAG = RqtPlotView.class.getSimpleName();

    Paint backgroundPaint;
    Paint dataPaint;
    PlotDataList data;
    ScaleGestureDetector scaleGestureDetector;
    XAxis xAxis;
    YAxis yAxis;
    ArrayList<String> subPaths;


    public RqtPlotView(Context context) {
        super(context);
        init();
    }

    public RqtPlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);

        RqtPlotEntity entity = (RqtPlotEntity) widgetEntity;

        subPaths = new ArrayList<>();
        for (String subPath: entity.fieldPath.split("/")) {
            if (!subPath.isEmpty()) {
                subPaths.add(subPath.trim().toLowerCase());
            }
        }

        yAxis.setTickSteps(entity.height);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }


    private void init() {
        float textSize = 12 * getResources().getDisplayMetrics().density;

        xAxis = new XAxis(textSize);
        yAxis = new YAxis(textSize);

        data = new PlotDataList();
        data.setMaxTime(xAxis.getScale()*1.5f);

        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#121212"));
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        dataPaint = new Paint();
        dataPaint.setColor(Color.YELLOW);
        dataPaint.setStrokeWidth(4);
        dataPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onNewMessage(Message message) {
        super.onNewMessage(message);

        // Try to extract value by traversing message path;
        try {
            Header header = message.toRawMessage().getMessage("header");

            for (int i = 0; i < subPaths.size(); i++) {
                String path = subPaths.get(i);

                // Check if last subPath
                if (i == subPaths.size() -1) {
                    Float value = null;

                    // Find value and cast to float
                    for (Field field: message.toRawMessage().getFields()) {
                        if (field.getName().equals(path)) {
                            value = ((Number) field.getValue()).floatValue();
                            break;
                        }
                    }

                    // Add value to data
                    if (value != null) {
                        data.add(value, header.getStamp());
                        yAxis.setLimits(data.getMinValue(), data.getMaxValue());

                    } else {
                        Log.i(TAG, "Field couldnt be resolved. Unknown type.");
                    }

                } else {
                    message = message.toRawMessage().getMessage(path);
                }
            }


        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        // Draw background
        canvas.drawRect(0, 0, w,  h, backgroundPaint);

        xAxis.draw(canvas);
        yAxis.draw(canvas);

        // Draw path
        if (data.size() < 2) return;

        for (int i = 0; i < data.size()-1; i++) {
            PlotDataList.PlotData now = data.get(i);
            PlotDataList.PlotData next = data.get(i+1);

            float xNow = xAxis.getPos(now, w);
            float yNow = yAxis.getPos(now, h);
            float xNext = xAxis.getPos(next, w);
            float yNext = yAxis.getPos(next, h);

            canvas.drawLine(xNow, yNow, xNext, yNext, dataPaint);
        }
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            xAxis.scale(detector.getScaleFactor());
            data.setMaxTime(xAxis.getScale()*1.5f);
            invalidate();
            return true;
        }
    }
}
