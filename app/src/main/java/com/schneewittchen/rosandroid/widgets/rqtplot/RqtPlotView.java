package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;

import org.ros.internal.message.Message;
import org.ros.message.Time;

import java.util.ArrayList;
import java.util.List;

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
    public static final double LN2 = 1.4426950408889634073599246810019;

    Paint backgroundPaint;
    Paint axisPaint;
    Paint linePaint;
    Paint dataPaint;
    TextPaint textPaintX, textPaintY;
    float axisSpaceX, axisSpaceY;
    float xTextSpace;
    float textSize;
    float textWidth, textHeight;
    PlotDataList data;
    ScaleGestureDetector scaleGestureDetector;
    float scaleFactor = 1.1f;
    List<Float> scales;
    int scaleAlpha;


    public RqtPlotView(Context context) {
        super(context);
        init();
    }

    public RqtPlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }


    private void init() {
        data = new PlotDataList();
        data.setMaxTime(scaleFactor*1.5f);

        scaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#121212"));
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        axisPaint = new Paint();
        axisPaint.setColor(Color.WHITE);
        axisPaint.setStrokeWidth(3);
        axisPaint.setStyle(Paint.Style.STROKE);

        dataPaint = new Paint();
        dataPaint.setColor(Color.YELLOW);
        dataPaint.setStrokeWidth(4);
        dataPaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);

        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 10}, 0);
        linePaint.setPathEffect(dashPathEffect);

        textSize = 12 * getResources().getDisplayMetrics().density;

        textPaintX = new TextPaint();
        textPaintX.setColor(Color.WHITE);
        textPaintX.setTextSize(textSize);
        textPaintX.setTextAlign(Paint.Align.CENTER);

        textPaintY = new TextPaint();
        textPaintY.setColor(Color.WHITE);
        textPaintY.setTextSize(textSize);
        textPaintY.setTextAlign(Paint.Align.LEFT);

        Rect bounds = new Rect();
        String textText = "WWW";
        textPaintX.getTextBounds(textText, 0, textText.length(), bounds);
        textWidth = bounds.width();
        textHeight = bounds.height();

        axisSpaceX = textHeight + 20;
        axisSpaceY = textWidth + 20;
        xTextSpace = (axisSpaceX -textHeight) /2f;
    }

    @Override
    public void onNewMessage(Message message) {
        super.onNewMessage(message);

        float value = message.toRawMessage().getFloat32("percentage");
        Header header = message.toRawMessage().getMessage("header");
        data.add(value, header.getStamp());
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        float plotW = w-axisSpaceY;
        float plotH = h-axisSpaceX;

        canvas.drawRect(0, 0, w, h, backgroundPaint);

        // Draw axis
        canvas.drawLine(plotW, 0, plotW, h, axisPaint);
        canvas.drawLine(0, plotH, w, plotH, axisPaint);

        // Draw lines
        updateScales();

        for(int i = 0; i < scales.size(); i++) {
            float scale = scales.get(i);
            int alpha = (i % 2 == 0)? scaleAlpha : 255;

            textPaintX.setAlpha(alpha);
            linePaint.setAlpha(alpha);

            float x = plotW * (1 - (scale/scaleFactor));
            canvas.drawLine(x, 0, x, plotH, linePaint);
            canvas.drawText("-"+scale, x, h -xTextSpace, textPaintX);
        }

        textPaintX.setAlpha(255);
        linePaint.setAlpha(255);

        float dy = plotH/5;
        float textX = plotW + 10;

        double dV = (data.maxValue -data.minValue) / 4f;

        for (int i=1; i < 5; i++) {
            float y = dy * i;
            canvas.drawLine(0, y, plotW, y, linePaint);
            String text = ""+(data.minValue + dV * (5-i));
            canvas.drawText(text , textX, y+textHeight/2, textPaintY);
        }

        // Draw path
        if (data.size() < 2) return;

        for (int i = 0; i < data.size()-1; i++) {
            PlotDataList.PlotData now = data.get(i);
            PlotDataList.PlotData next = data.get(i+1);

            float x1 = plotW * (1 - (now.secsToLatest()/scaleFactor));
            float x2 = plotW * (1 - (next.secsToLatest()/scaleFactor));
            float y1 = (float) (plotH * (1 -now.value));
            float y2 = (float) (plotH* (1 -next.value));

            canvas.drawLine(x1, y1, x2, y2, dataPaint);
        }
    }

    private void updateScales() {
        double powerNow = getPowerOf2(scaleFactor, 0);
        double powerNext = getPowerOf2(scaleFactor, 1);
        double powerHalf = (powerNext + powerNow) / 2f;
        double toHalf = powerHalf - powerNow;

        int n = 4;
        double max = powerNext;
        scaleAlpha = 255;

        if (scaleFactor >= powerNow && scaleFactor < powerHalf) {
            scaleAlpha = (int) (((powerHalf - scaleFactor) / toHalf) * 255);
            n = 6;
            max = powerHalf;
        }

        scales = new ArrayList<>(n);
        float df = (float) (max/n);
        for (float factor = df; factor < max; factor += df ) {
            scales.add(factor);
        }
    }

    private double getPowerOf2(float value, int next) {
        double pow = Math.floor(Math.log(value) * LN2)  + next;
        return Math.pow(2, pow);
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor /= detector.getScaleFactor();
            scaleFactor = Math.max(1.1f, Math.min(scaleFactor, 70));
            data.setMaxTime(scaleFactor*1.5f);
            invalidate();
            return true;
        }
    }
}
