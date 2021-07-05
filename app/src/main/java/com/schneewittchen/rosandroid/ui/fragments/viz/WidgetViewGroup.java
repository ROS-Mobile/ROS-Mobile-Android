package com.schneewittchen.rosandroid.ui.fragments.viz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.schneewittchen.rosandroid.BuildConfig;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.widgets.WidgetGroupView;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.views.widgets.IBaseView;
import com.schneewittchen.rosandroid.ui.views.widgets.IPublisherView;
import com.schneewittchen.rosandroid.ui.views.widgets.ISubscriberView;
import com.schneewittchen.rosandroid.ui.views.widgets.LayerView;
import com.schneewittchen.rosandroid.ui.views.widgets.WidgetView;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.ui.general.DataListener;
import com.schneewittchen.rosandroid.ui.general.Position;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.IPositionEntity;

import org.ros.internal.message.Message;

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
    Paint scaleShadowPaint;
    int tilesX;
    int tilesY;
    float tileWidth;
    List<BaseEntity> widgetList;
    DataListener dataListener;
    WidgetChangeListener widgetDetailsChangedListener;
    boolean vizEditMode = false;
    boolean drawWidgetScaleShadow = false;
    Position widgetScaleShadowPosition = null;


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

        scaleShadowPaint = new Paint();
        scaleShadowPaint.setColor(getResources().getColor(R.color.colorPrimary));
        scaleShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scaleShadowPaint.setAlpha(100);

        this.setWillNotDraw(false);

        this.setOnDragListener((view, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP && this.vizEditMode) {
                WidgetView widget = (WidgetView) event.getLocalState();
                IPositionEntity entity = (IPositionEntity)widget.getWidgetEntity().copy();

                Position position = entity.getPosition();
                position.x = Math.round((event.getX() - widget.getWidth() / 2f) / tileWidth);
                position.y = tilesY - Math.round((event.getY() + widget.getHeight() / 2f) / tileWidth);
                entity.setPosition(position);

                this.widgetDetailsChangedListener.onWidgetDetailsChanged((BaseEntity) entity);
            }
            return true;
        });
    }


    private void calculateTiles() {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();

        if (width < height) { // Portrait
            tilesX = TILES_X;
            tileWidth = width / tilesX;
            tilesY = (int) (height / tileWidth);
        } else { // Landscape
            tilesY = TILES_X;
            tileWidth = height / tilesY;
            tilesX = (int) ( width / tileWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View v = getChildAt(i);
            // this works because you set the dimensions of the ImageView to FILL_PARENT
            v.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight(), MeasureSpec.EXACTLY));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Position all children within this layout.
     */
    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        calculateTiles();

        for (int i = 0; i < getChildCount(); i++) {
            positionChild(i);
        }
    }

    private void positionChild(int i) {
        final View child = getChildAt(i);

        // Check if view is visible
        if(child.getVisibility() == GONE)
            return;

        Position position = ((WidgetView) child).getPosition();

        // Y pos from bottom up
        int w = (int) (position.width * tileWidth);
        int h = (int) (position.height * tileWidth);
        int x = (int) (getPaddingLeft() + position.x * tileWidth);
        int y = (int) (getPaddingTop() + (tilesY - (position.height + position.y)) * tileWidth);

        // Place the child.
        child.layout(x, y, x + w, y + h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float startX = getPaddingLeft();
        float endX = getWidth() - this.getPaddingRight();
        float startY = getPaddingTop();
        float endY = getHeight() - this.getPaddingBottom();

        // Draw x's
        float lineLen = Utils.dpToPx(getContext(), 5)/2;

        for(float drawY = startY; drawY <= endY; drawY += tileWidth){
            for(float drawX = startX; drawX <= endX; drawX += tileWidth){
                canvas.drawLine(drawX-lineLen, drawY, drawX+lineLen, drawY, crossPaint);
                canvas.drawLine(drawX, drawY-lineLen, drawX, drawY+lineLen, crossPaint);
            }
        }

        if (drawWidgetScaleShadow && widgetScaleShadowPosition != null) {
            int w = (int) (widgetScaleShadowPosition.width * tileWidth);
            int h = (int) (widgetScaleShadowPosition.height * tileWidth);
            int x = (int) (getPaddingLeft() + widgetScaleShadowPosition.x * tileWidth);
            int y = (int) (getPaddingTop() + (tilesY - (widgetScaleShadowPosition.height + widgetScaleShadowPosition.y)) * tileWidth);
            canvas.drawRect(x, y, x + w, y + h, scaleShadowPaint);
        }
    }


    public void informDataChange(BaseData data) {
        if (dataListener != null) {
            dataListener.onNewWidgetData(data);
        }
    }

    public void onNewData(RosData data) {
        Message message = data.getMessage();
        Topic topic = data.getTopic();

        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(!(view instanceof ISubscriberView)) continue;

            if(view instanceof WidgetGroupView) {
                ((WidgetGroupView)view).onNewData(data);

            } else {
                IBaseView baseView = (IBaseView) view;

                if (baseView.getWidgetEntity().topic.equals(topic)){
                    ((ISubscriberView)view).onNewMessage(message);
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
                    changeViewFor(newWidget);
                    changes = true;
                }

            } else{
                addViewFor(newWidget);
                changes = true;
            }
        }

        // Delete unused widgets
        for (Long id: widgetCheckMap.keySet()) {
            if (!widgetCheckMap.get(id)) {
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
        Log.i(TAG, "Add view for " + entity.name);

        IBaseView baseView = createViewFrom(entity);

        if (baseView == null) return;

        baseView.setWidgetEntity(entity);

        // Check if view is a group view and register the sub layers
        if (baseView instanceof WidgetGroupView) {
            WidgetGroupView groupView = (WidgetGroupView) baseView;

            for (BaseEntity subEntity: entity.childEntities)  {
                IBaseView subView = createViewFrom(subEntity);
                subView.setWidgetEntity(subEntity);

                if (!(subView instanceof LayerView))
                    return;

                groupView.addLayer((LayerView)subView);
            }

        }

        // Set data listener if view is a publisher
        if (baseView instanceof IPublisherView) {
            ((IPublisherView)baseView).setDataListener(this::informDataChange);
        }

        // Add as subview if the view is a widget view
        if (baseView instanceof WidgetView) {
            this.addView((WidgetView)baseView);
        }

    }

    private IBaseView createViewFrom(BaseEntity entity) {
        // Create actual widget view object
        String classPath = BuildConfig.APPLICATION_ID
                + String.format(Constants.VIEW_FORMAT, entity.type.toLowerCase(), entity.type);
        Object object;

        try {
            Class<?> clazz = Class.forName(classPath);
            Constructor<?> constructor = clazz.getConstructor(Context.class);
            object = constructor.newInstance(this.getContext());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Log.i(TAG, "object is a : " + object.getClass().getCanonicalName());


        if (!(object instanceof IBaseView)) {
            Log.i(TAG, "View can not be created from: " + classPath);
            return null;
        }

        return (IBaseView) object;
    }

    private void changeViewFor(BaseEntity entity) {
        Log.i(TAG, "Change view for " + entity.name);

        for(int i = 0; i < this.getChildCount(); i++) {
            IBaseView view = (IBaseView) this.getChildAt(i);

            if (view.sameWidgetEntity(entity)) {
                view.setWidgetEntity(entity);
                return;
            }
        }
    }

    private void removeViewFor(BaseEntity entity) {
        Log.i(TAG, "Remove view for " + entity.name);

        for(int i = 0; i < this.getChildCount(); i++) {
            IBaseView view = (IBaseView) this.getChildAt(i);

            if (view.sameWidgetEntity(entity)) {
                this.removeView((WidgetView)view);
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

    public void setOnWidgetDetailsChanged(WidgetChangeListener listener) {
        this.widgetDetailsChangedListener = listener;
    }

    public void setVizEditMode(boolean enabled) {
        this.vizEditMode = enabled;
        for (int i = 0; i < getChildCount(); ++i) {
            WidgetView widgetView = (WidgetView)getChildAt(i);
            widgetView.setOnScaleListener(tileWidth, (baseEntity, updateConfig) -> {
                if (vizEditMode) {
                    widgetScaleShadowPosition = ((IPositionEntity)baseEntity).getPosition();
                    widgetScaleShadowPosition.height = Math.max(0, Math.min(widgetScaleShadowPosition.height, tilesY));
                    widgetScaleShadowPosition.width = Math.max(0, Math.min(widgetScaleShadowPosition.width, tilesX));
                    ((IPositionEntity)baseEntity).setPosition(widgetScaleShadowPosition);
                    drawWidgetScaleShadow = !updateConfig;
                    invalidate();
                    if (updateConfig) {
                        this.widgetDetailsChangedListener.onWidgetDetailsChanged(baseEntity);
                    }
                }
            });
            widgetView.setEditMode(enabled);
        }
    }
}