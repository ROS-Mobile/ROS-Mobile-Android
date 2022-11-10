package com.schneewittchen.rosandroid.widgets.label;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.widgets.WidgetView;
import com.schneewittchen.rosandroid.utility.Utils;

import javax.annotation.Nullable;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.1.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 * @updated on 10.11.2022
 * @modified by Nico Studt
 */

public class LabelView extends WidgetView {

    public static final String TAG = LabelView.class.getSimpleName();

    TextPaint textPaint;
    Paint linePaint;
    float lineWidth;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lineWidth = Utils.dpToPx(getContext(), 2);

        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextSize(20 * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LabelEntity entity = (LabelEntity) widgetEntity;
        float width = getWidth();
        float height = getHeight();

        float baseline = (textPaint.descent() + textPaint.ascent()) / 2f;
        float xPos = width / 2;
        float yPos = height / 2f - baseline;

        canvas.save();
        canvas.rotate(entity.rotation, width / 2, height / 2);
        canvas.drawText(entity.text, xPos, yPos, textPaint);
        canvas.drawLine(0, height - lineWidth / 2, width, height - lineWidth / 2, linePaint);
        canvas.restore();
    }
}
