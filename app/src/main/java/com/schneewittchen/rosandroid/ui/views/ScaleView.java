package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.04.20
 * @updated on 17.04.20
 * @modified by
 */
public class ScaleView extends View {

    Paint smallLinePaint;
    Paint bigLinePaint;
    int scaleColor;
    float bigLineWidth;
    float smallLineWidth;
    float middleH;
    int segments;
    float segmentWidth;
    private float lineStart;
    private float lineEnd;
    private float firstQuarterHeight;
    private float thirdQuarterHeight;


    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.init();
    }


    private void init() {
        bigLineWidth = Utils.dpToPx(this.getContext(), 2);
        smallLineWidth = Utils.dpToPx(this.getContext(), 1);

        bigLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.whiteMedium));
        bigLinePaint.setStrokeWidth(bigLineWidth);

        smallLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.whiteMedium));
        smallLinePaint.setStrokeWidth(smallLineWidth);

        segments = 8;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        lineStart = bigLineWidth/2;
        lineEnd = w - bigLineWidth/2;
        middleH = h / 2f;
        segmentWidth = (lineEnd -lineStart) / segments;
        firstQuarterHeight = h /4f;
        thirdQuarterHeight = h /4f * 3f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw horizontal line
        canvas.drawLine(lineStart, middleH, lineEnd, middleH, smallLinePaint);

        // Draw vertical lines
        for (int i = 0; i <= segments; i++) {
            Paint paint = bigLinePaint;
            float x = lineStart + i * segmentWidth;
            float startY = 0;
            float endY = getHeight();

            if (i % (segments/2) != 0) {
                startY = firstQuarterHeight;
                endY = thirdQuarterHeight;
                paint = smallLinePaint;
            }

            canvas.drawLine(x, startY, x, endY, paint);
        }
    }
}
