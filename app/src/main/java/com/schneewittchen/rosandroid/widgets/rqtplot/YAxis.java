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
public class YAxis {

    TextPaint textPaint;
    Paint axisPaint;
    Paint linePaint;
    List<Float> scales;
    float textSpace;
    float textWidth, textHeight;
    float axisSpaceX, axisSpaceY;
    double limitMin, limitMax;
    boolean hasLimits;


    public YAxis(float textSize) {
        axisPaint = new Paint();
        axisPaint.setColor(Color.WHITE);
        axisPaint.setStrokeWidth(3);
        axisPaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);

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

        this.hasLimits = false;

        this.updateScales();
    }


    public void draw(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        float plotW = w - axisSpaceY;
        float plotH = h - axisSpaceX;

        // Draw axis line
        canvas.drawLine(plotW, 0, plotW, h, axisPaint);

        float textX = plotW + 10;
        double maxScale = (hasLimits)? limitMax - limitMin : 1;

        // Draw scales
        for(int i = 0; i < scales.size(); i++) {
            float scale = scales.get(i);

            float y = (float) ((plotH-80) * (1 - ((scale-limitMin)/maxScale)) +40);
            canvas.drawLine(0, y, plotW, y, linePaint);
            canvas.drawText(""+scale , textX, y+textHeight/2, textPaint);
        }
    }

    private void updateScales() {
        int n = 5;
        float start = (float) limitMin;
        float end = (float) limitMax;
        float df = (float) ((limitMax - limitMin) / n);

        if (!hasLimits) {
            start = 0;
            end = 1;
            df = 1 / 5f;
        }

        scales = new ArrayList<>(n+1);
        for (float factor = start; factor <= end; factor += df ) {
            scales.add(factor);
        }
    }

    public float getPos(PlotDataList.PlotData point, float h) {
        return (float) (((h - axisSpaceX) - 80) * (1 - ((point.value-limitMin)/(limitMax-limitMin))) +40);
    }

    public void setLimits(double minValue, double maxValue) {
        this.limitMin = minValue;
        this.limitMax = maxValue;
        this.hasLimits = maxValue - minValue > 0.001;
        this.updateScales();
    }
}
