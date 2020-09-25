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

import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.WidgetDiffCallback;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.DataListener;
import com.schneewittchen.rosandroid.widgets.base.Position;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.2
 * @created on 18.10.19
 * @updated on 22.04.20
 * @modified by Nils Rottmann
 * @updated on 25.09.20
 * @modified by Nils Rottmann
 */
public class WidgetViewGroup extends ViewGroup {

    public static final String TAG = WidgetViewGroup.class.getSimpleName();

    Paint crossPaint;
    int tilesX;
    int tilesY;
    float tileWidth;
    List<BaseWidget> widgetList;
    DataListener widgetDataListener;
    DataListener dataListener;


    public WidgetViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.widgetList = new ArrayList<>();
        this.widgetDataListener = new WidgetDataListener();
        this.setWillNotDraw(false);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                        R.styleable.WidgetViewGroup, 0, 0);

        int crossColor = a.getColor(R.styleable.WidgetViewGroup_crossColor,
                getResources().getColor(R.color.colorAccent));

        a.recycle();

        float stroke = Utils.dpToPx(getContext(), 1);

        crossPaint = new Paint();
        crossPaint.setColor(crossColor);
        crossPaint.setStrokeWidth(stroke);
    }


    private void calculateTiles() {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();

        tilesX = 8;
        tileWidth = width/tilesX;
        tilesY = (int)(height/tileWidth);
    }

    /**
     * Position all children within this layout.
     */
    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "On Layout");
        calculateTiles();

        for (int i = 0; i < getChildCount(); i++) {
            positionChild(i);
        }
    }

    private void positionChild(int i) {
        float lowestPos = getHeight() - getPaddingBottom(); //- tilesY * tileWidth;
        final View child = getChildAt(i);

        // Check if view is visible
        if(child.getVisibility() == GONE)
            return;


        Position position = ((BaseView) child).getPosition();

        // Y pos from bottom up
        int w = (int) (position.width * tileWidth);
        int h = (int) (position.height * tileWidth);
        int x = (int) (getPaddingLeft() + position.x * tileWidth);
        int y = (int) (lowestPos - position.y * tileWidth - h);

        // Place the child.
        child.layout(x, y, x + w, y + h);
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


    public void informDataChange(BaseData data) {
        if (dataListener != null) {
            dataListener.onNewData(data);
        }
    }

    public void setWidgets(List<BaseWidget> newWidgets) {
        WidgetDiffCallback diffCallback = new WidgetDiffCallback(newWidgets, this.widgetList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                for (int i=position; i<position+count; i++) {
                    addViewFor(newWidgets.get(i));
                }
            }

            @Override
            public void onRemoved(int position, int count) {
                for (int i=position; i<position+count; i++) {
                    removeViewFor(widgetList.get(i));
                }
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) { }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                for(int i = position; i < position +count; i++) {
                    changeViewFor(newWidgets.get(i));
                }

                requestLayout();
            }
        });

        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);
    }



    private void addViewFor(BaseWidget entity) {
        // Create actual widget view object
        String classPath = BuildConfig.APPLICATION_ID
                + String.format(Constants.VIEW_FORMAT, entity.type.toLowerCase(), entity.type);
        Object object;

        try {
            Class<?> clazz = Class.forName(classPath);
            Constructor<?> constructor = clazz.getConstructor(Context.class);
            object = constructor.newInstance(this.getContext());

            if (!(object instanceof BaseView)) {
                Log.i(TAG, "View can not be created from: " + classPath);
                return;
            }

        } catch (Exception e) {
            return;
        }

        // Init widget view
        BaseView widgetView = (BaseView) object;
        widgetView.setWidgetEntity(entity);
        widgetView.setDataListener(widgetDataListener);

        this.addView(widgetView);
    }

    private void changeViewFor(BaseWidget entity) {
        for(int i = 0; i < this.getChildCount(); i++) {
            BaseView view = (BaseView) this.getChildAt(i);

            if (view.sameWidget(entity)) {
                Position position = new Position(entity.posX, entity.posY,
                        entity.width, entity.height);

                view.setPosition(position);
                return;
            }
        }
    }

    private void removeViewFor(BaseWidget entity) {
        for(int i = 0; i < this.getChildCount(); i++) {
            BaseView view = (BaseView) this.getChildAt(i);

            if (view.sameWidget(entity)) {
                this.removeView(view);
                return;
            }
        }
    }

    private void registerWidgetListener(BaseView view) {
        view.setDataListener(widgetDataListener);
    }

    public List<BaseWidget> getWidgets() {
        return this.widgetList;
    }

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    public void removeDataListener() {
        this.dataListener = null;
    }

    public void setData(BaseData data) {
        for(int i = 0; i < this.getChildCount(); i++) {
            BaseView view = (BaseView) this.getChildAt(i);

            if (view.getDataId() == data.getId()) {
                view.setData(data);
            }
        }
    }


    class WidgetDataListener implements DataListener {

        @Override
        public void onNewData(BaseData data) {
            informDataChange(data);
        }
    }


}