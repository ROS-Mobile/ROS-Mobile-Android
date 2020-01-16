package com.schneewittchen.rosandroidlib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.schneewittchen.rosandroidlib.R;
import com.schneewittchen.rosandroidlib.Utils;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 18.10.19
 * @updated on 9.01.20
 * @modified by
 */
public class WidgetGroup extends ViewGroup {

    Paint crossPaint;
    int tilesX;
    int tilesY;
    float tileWidth;


    public WidgetGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setWillNotDraw(false);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                        R.styleable.WidgetGroup, 0, 0);

        int crossColor = a.getColor(R.styleable.WidgetGroup_crossColor,
                getResources().getColor(R.color.colorAccent));

        a.recycle();


        float stroke = Utils.dpToPx(getContext(), 1);

        crossPaint = new Paint();
        crossPaint.setColor(crossColor);
        crossPaint.setStrokeWidth(stroke);
    }


    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        calculateTiles();

        float startH = getHeight() - tilesY * tileWidth;
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            // Check if view is visible
            if(child.getVisibility() == GONE)
                continue;

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int x = (int) (getPaddingLeft() + lp.tilesX * tileWidth);
            int y = (int) (getPaddingTop() + startH + lp.tilesY * tileWidth);
            int w = (int) (lp.tilesWidth * tileWidth);
            int h = (int) (lp.tilesHeight * tileWidth);

            // Place the child.
            child.layout(x, y, x + w, y + h);
        }
    }

    private int getBestTilesX(float usedLenght){
        if(usedLenght == 0)
            return 1;

        float bestError = Float.MAX_VALUE;
        int bestTileX = 1;

        for(int i = 1 ; i < 100; i++){
            float tileWidth = usedLenght/i;
            float cm = Utils.pxToCm(getContext(), tileWidth);
            float error = Math.abs(cm-1);

            if(error < bestError){
                bestError = error;
                bestTileX = i;
            }else{
                break;
            }
        }

        return bestTileX;
    }

    @Override
    public void onDraw(Canvas canvas) {
        float startX = getPaddingTop();
        float endX = getHeight() - this.getPaddingBottom();
        float startY = getPaddingTop();
        float endY = getHeight() - this.getPaddingBottom();

        // Draw x's
        float lineLen = Utils.dpToPx(getContext(), 5)/2;

        for(float drawY = endY; drawY > startY; drawY -= tileWidth){

            for(float drawX = startX; drawX < endX; drawX += tileWidth){

                canvas.drawLine(drawX-lineLen, drawY, drawX+lineLen, drawY, crossPaint);
                canvas.drawLine(drawX, drawY-lineLen, drawX, drawY+lineLen, crossPaint);
            }
        }
    }

    public void calculateTiles() {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();

        tilesX = 8;
        tileWidth = width/tilesX;
        tilesY = (int)(height/tileWidth);
        /*
        tilesX = getBestTilesX(width);

        tileWidth = width / tilesX;

        tilesY = (int) (height / tileWidth);

        while (true) {
            float pxSpace = height - tilesY * tileWidth;

            if (Utils.pxToCm(getContext(), pxSpace) < 0.8) {
                tilesY--;
            } else {
                break;
            }
        }
        */
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WidgetGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    public static class LayoutParams extends MarginLayoutParams {

        int tilesX = 0;
        int tilesY = 0;
        int tilesWidth = 1;
        int tilesHeight = 1;

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LayoutParams);
            tilesX = a.getInt(R.styleable.LayoutParams_tilesX, tilesX);
            tilesY = a.getInt(R.styleable.LayoutParams_tilesY, tilesY);
            tilesWidth = a.getInt(R.styleable.LayoutParams_tilesWidth, tilesWidth);
            tilesHeight = a.getInt(R.styleable.LayoutParams_tilesHeight, tilesHeight);

            a.recycle();
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }

        LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

}