package com.schneewittchen.rosandroid.ui.fragments.viz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;
import com.schneewittchen.rosandroid.ui.views.BaseView;
import com.schneewittchen.rosandroid.ui.views.PublisherView;
import com.schneewittchen.rosandroid.ui.views.SubscriberView;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.ui.general.DataListener;
import com.schneewittchen.rosandroid.ui.general.Position;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static final int TILES_X = 8;

    Paint crossPaint;
    int tilesX;
    int tilesY;
    float tileWidth;
    List<BaseEntity> widgetList;
    DataListener dataListener;


    public WidgetViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.widgetList = new ArrayList<>();

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                        R.styleable.WidgetViewGroup, 0, 0);

        int crossColor = a.getColor(R.styleable.WidgetViewGroup_crossColor,
                getResources().getColor(R.color.colorAccent));

        a.recycle();

        float stroke = Utils.dpToPx(getContext(), 1);

        crossPaint = new Paint();
        crossPaint.setColor(crossColor);
        crossPaint.setStrokeWidth(stroke);

        this.setWillNotDraw(false);
    }


    private void calculateTiles() {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();

        tilesX = TILES_X;
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
            dataListener.onNewWidgetData(data);
        }
    }

    public void onNewData(RosData message) {
        for(int i = 0; i < this.getChildCount(); i++) {
            Object view = this.getChildAt(i);

            if (view instanceof SubscriberView) {
                SubscriberView subView = (SubscriberView) view;

                if (subView.getTopic().equals(message.getTopic())){
                    subView.onNewMessage(message.getMessage());
                }
            }
        }
    }

    public void setWidgets(List<BaseEntity> newWidgets) {
        boolean changes = false;

        // Create widget check with ids
        HashMap<Long, Boolean> widgetCheckMap = new HashMap<>();
        HashMap<Long, BaseEntity> widgetEntryMap = new HashMap<>();

        for (BaseEntity oldWidget: this.widgetList) {
            widgetCheckMap.put(oldWidget.id, false);
            widgetEntryMap.put(oldWidget.id, oldWidget);
        }

        for (BaseEntity newWidget: newWidgets) {
            if (widgetCheckMap.containsKey(newWidget.id)) {
                widgetCheckMap.put(newWidget.id, true);

                // Check if widget has changed
                BaseEntity oldWidget = widgetEntryMap.get(newWidget.id);

                if (!oldWidget.equals(newWidget)){
                    Log.i(TAG, "Update widget " + oldWidget.id);
                    changeViewFor(newWidget);
                    changes = true;
                }

            } else{
                Log.i(TAG, "Add widget " + newWidget.id);
                addViewFor(newWidget);
                changes = true;
            }
        }

        // Delete unused widgets
        for (Long id: widgetCheckMap.keySet()) {
            if (!widgetCheckMap.get(id)) {
                Log.i(TAG, "Remove widget " + id);
                removeViewFor(widgetEntryMap.get(id));
                changes = true;
            }
        }

        this.widgetList.clear();
        this.widgetList.addAll(newWidgets);

        if (changes) {
            requestLayout();
        }
    }



    private void addViewFor(BaseEntity entity) {
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
        widgetView.updatePosition();

        if (widgetView instanceof PublisherView){
            ((PublisherView)widgetView).setDataListener(this::informDataChange);
        }

        this.addView(widgetView);
    }

    private void changeViewFor(BaseEntity entity) {
        for(int i = 0; i < this.getChildCount(); i++) {
            BaseView view = (BaseView) this.getChildAt(i);

            if (view.sameWidget(entity)) {
                view.setWidgetEntity(entity);
                view.updatePosition();
                return;
            }
        }
    }

    private void removeViewFor(BaseEntity entity) {
        for(int i = 0; i < this.getChildCount(); i++) {
            BaseView view = (BaseView) this.getChildAt(i);

            if (view.sameWidget(entity)) {
                this.removeView(view);
                return;
            }
        }
    }

    public List<BaseEntity> getWidgets() {
        return this.widgetList;
    }

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }


}