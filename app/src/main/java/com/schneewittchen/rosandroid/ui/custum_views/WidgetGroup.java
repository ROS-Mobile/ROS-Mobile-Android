package com.schneewittchen.rosandroid.ui.custum_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.ui.helper.WidgetDiffCallback;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.DataListener;
import com.schneewittchen.rosandroid.widgets.base.WidgetData;

import java.util.ArrayList;
import java.util.List;


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

    public static final String TAG = "WidgetGroup";

    Paint crossPaint;
    int tilesX;
    int tilesY;
    float tileWidth;
    List<WidgetEntity> widgetList;
    DataListener widgetDataListener;
    DataListener dataListener;


    public WidgetGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.widgetList = new ArrayList<>();
        this.widgetDataListener = new WidgetDataListener();
        //this.setWillNotDraw(true);

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
        Log.i(TAG, "On Layout");
        calculateTiles();

        for (int i = 0; i < getChildCount(); i++) {
            positionChild(i);
        }
    }

    private void calculateTiles() {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();

        tilesX = 8;
        tileWidth = width/tilesX;
        tilesY = (int)(height/tileWidth);
    }

    private void positionChild(int i) {
        float lowestPos = getHeight() - getPaddingBottom(); //- tilesY * tileWidth;
        final View child = getChildAt(i);

        // Check if view is visible
        if(child.getVisibility() == GONE)
            return;

        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        // Y pos from bottom up
        int w = (int) (lp.tilesWidth * tileWidth);
        int h = (int) (lp.tilesHeight * tileWidth);
        int x = (int) (getPaddingLeft() + lp.tilesX * tileWidth);
        int y = (int) (lowestPos - lp.tilesY * tileWidth - h);


        Log.i(TAG, "place child " + x + " " + y+ " " + (x + w)+ " " + (y + h));
        // Place the child.
        child.layout(x, y, x + w, y + h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "On Draw");
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

        /*
        for (int i = 0; i < getChildCount(); i++) {
            Log.w(TAG, "Child draw " + i);
            getChildAt(i).draw(canvas);
        }

         */

        //super.onDraw(canvas);
    }

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    public void removeDataListener() {
        this.dataListener = null;
    }

    public void informDataChange(WidgetData data) {
        if(dataListener != null) {
            dataListener.onNewData(data);
        }
    }

    private void registerWidgetListener(BaseView view) {
        view.setDataListener(widgetDataListener);
    }

    public void setWidgets(List<WidgetEntity> newWidgets) {
        WidgetDiffCallback diffCallback = new WidgetDiffCallback(newWidgets, this.widgetList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);

        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                Log.i(TAG, "Inserted: Position " + position + " count " + count);
                //getChildAt(position).
            }

            @Override
            public void onRemoved(int position, int count) {
                Log.i(TAG, "Removed: Position " + position + " count " + count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                Log.i(TAG, "Moved: From " + fromPosition + " to " + toPosition);
            }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                Log.i(TAG, "Changed: From " + position + " count " + count);
            }
        });
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

    public List<WidgetEntity> getWidgets() {
        return this.widgetList;
    }


    class WidgetDataListener implements DataListener {

        @Override
        public void onNewData(WidgetData data) {
            informDataChange(data);
        }
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