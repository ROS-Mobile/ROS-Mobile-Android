package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

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

    private static final String TAG = YAxis.class.getSimpleName();

    TextPaint textPaint;
    Paint axisPaint;
    Paint linePaint;
    List<Float> scales;
    float textSpace;
    float textWidth, textHeight;
    float axisSpaceX, axisSpaceY;
    double limitMin, limitMax;
    float lowerBound, upperBound;
    boolean hasLimits;
    int tickSteps;

    public YAxis(float textSize) {
        this.tickSteps = 8;

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
        textSpace = (axisSpaceX - textHeight) / 2f;

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
        double minValue = lowerBound;
        double maxValue = upperBound;

        if (!hasLimits) {
            minValue = 0;
            maxValue = 1;
        }

        double maxScale = maxValue - minValue;

        // Draw scales
        for (int i = 0; i < scales.size(); i++) {
            float scale = scales.get(i);

            float y = (float) ((plotH - 80) * (1 - ((scale - minValue) / maxScale)) + 40);
            canvas.drawLine(0, y, plotW, y, linePaint);

            String scaleText = "" + scale;

            if (Math.abs(scale) <= 1) {
                scaleText = String.format("%.2f", scale);
            } else if (Math.abs(scale) <= 10) {
                scaleText = String.format("%.1f", scale);
            } else {
                scaleText = String.format("%d", (int) scale);
            }

            canvas.drawText(scaleText, textX, y + textHeight / 2, textPaint);
        }
    }

    private void updateScales() {
        float min = (float) limitMin;
        float max = (float) limitMax;
        if (!hasLimits) {
            min = 0;
            max = 1;
        }

        float range = max - min;
        double roughStep = range / (tickSteps - 1);
        //double[] normalizedSteps = {1, 1.5, 2, 2.5, 5, 7.5, 10}; // keep the 10 at the end
        double[] normalizedSteps = { 1, 2, 5, 10 };

        double powX = Math.pow(10, -Math.floor(Math.log10(Math.abs(roughStep))));
        double normalizedStep = roughStep * powX;

        double goodPowX = 0;
        for (double n : normalizedSteps) {
            if (n >= normalizedStep) {
                goodPowX = n;
                break;
            }
        }

        double dist = goodPowX / powX;

        // Determine the scale limits based on the chosen step.
        upperBound = (float) (Math.ceil(max / dist) * dist);
        lowerBound = (float) (Math.floor(min / dist) * dist);

        scales = new ArrayList<>(tickSteps + 1);
        for (double factor = lowerBound; upperBound - factor > -0.000001; factor += dist) {
            scales.add((float) factor);
        }
    }

    public float getPos(PlotDataList.PlotData point, float h) {
        return (float) (((h - axisSpaceX) - 80)
                * (1 - ((point.value - lowerBound) / (upperBound - lowerBound))) + 40);
    }

    public void setLimits(double minValue, double maxValue) {
        this.limitMin = minValue;
        this.limitMax = maxValue;
        this.hasLimits = maxValue - minValue > 0.001;
        this.updateScales();
    }

    public void setTickSteps(int steps) {
        this.tickSteps = Math.max(2, steps);
    }
}
