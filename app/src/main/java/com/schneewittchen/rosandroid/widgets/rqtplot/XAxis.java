package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 03.06.21
 */
public class XAxis {

    private static final float MAX_SCALE = 70f;
    private static final float MIN_SCALE = 1.1f;
    private static final double LN2 = 1.4426950408889634073599246810019;

    TextPaint textPaint;
    Paint axisPaint;
    Paint linePaint;
    List<Float> scales;
    float scaleFactor = 1.1f;
    int scaleAlpha;
    float textSpace;
    float textWidth, textHeight;
    float axisSpaceX, axisSpaceY;


    public XAxis(float textSize) {
        axisPaint = new Paint();
        axisPaint.setColor(Color.WHITE);
        axisPaint.setStrokeWidth(3);
        axisPaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 10}, 0);
        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(dashPathEffect);

        Rect bounds = new Rect();
        String textText = "WWW";
        textPaint.getTextBounds(textText, 0, textText.length(), bounds);
        textWidth = bounds.width();
        textHeight = bounds.height();

        axisSpaceX = textHeight + 20;
        axisSpaceY = textWidth + 20;
        textSpace = (axisSpaceX -textHeight) /2f;

        this.updateScales();
    }


    public void draw(Canvas canvas) {

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        float plotW = w - axisSpaceY;
        float plotH = h - axisSpaceX;

        // Draw axis line
        canvas.drawLine(0, plotH, w, plotH, axisPaint);

        // Draw scales
        for(int i = 0; i < scales.size(); i++) {
            float scale = scales.get(i);
            int alpha = (i % 2 == 0)? scaleAlpha : 255;

            textPaint.setAlpha(alpha);
            linePaint.setAlpha(alpha);

            float x = plotW * (1 - (scale/scaleFactor));
            canvas.drawLine(x, 0, x, plotH, linePaint);
            canvas.drawText("-"+scale, x, h - textSpace, textPaint);
        }
    }


    public void scale(float factor) {
        scaleFactor /= factor;
        scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE));
        this.updateScales();
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

    public float getScale() {
        return scaleFactor;
    }

    public float getPos(PlotDataList.PlotData point, float w) {
        return (w - axisSpaceY) * (1 - (point.secsToLatest()/scaleFactor));
    }
}
