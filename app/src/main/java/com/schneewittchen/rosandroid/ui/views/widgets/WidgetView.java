package com.schneewittchen.rosandroid.ui.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.IPositionEntity;
import com.schneewittchen.rosandroid.ui.general.Position;
import com.schneewittchen.rosandroid.ui.general.WidgetEditListener;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public abstract class WidgetView extends ViewGroup implements IBaseView {

    public static String TAG = WidgetView.class.getSimpleName();

    protected Position position;
    protected BaseEntity widgetEntity;
    protected GestureDetector editModeGestureDetector;
    protected ScaleGestureDetector editModeScaleGestureDetector;
    protected boolean editMode = false;
    protected WidgetEditListener onWidgetEditListener;
    private float tileWidth;
    private Paint highlightPaint;
    private boolean shouldHighlight = false;


    public WidgetView(Context context) {
        super(context);
        baseInit();
    }

    public WidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        baseInit();
    }


    private void baseInit() {
        setWillNotDraw(false);

        highlightPaint = new Paint();
        highlightPaint.setColor(getResources().getColor(R.color.colorPrimary));
        highlightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        highlightPaint.setAlpha(100);

        editModeGestureDetector = new GestureDetector(getContext(), new EditModeOnGestureListener());
        editModeScaleGestureDetector = new ScaleGestureDetector(getContext(), new EditModeOnScaleGestureListener());
    }


    public void updatePosition() {
        this.position = ((IPositionEntity)widgetEntity).getPosition();
    }

    public Position getPosition() {
        return this.position;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (editMode && shouldHighlight) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), highlightPaint);
        }
    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        this.widgetEntity = widgetEntity;
        this.updatePosition();
    }

    @Override
    public BaseEntity getWidgetEntity() {
        return this.widgetEntity;
    }


    @Override
    public boolean sameWidgetEntity(BaseEntity other) {
        return other.id == this.widgetEntity.id;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setOnScaleListener(float tileWidth, WidgetEditListener onWidgetEditListener) {
        this.tileWidth = tileWidth;
        this.onWidgetEditListener = onWidgetEditListener;
    }

    // GESTURES

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (editMode) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                shouldHighlight = false;
                invalidate();
            }
            this.editModeGestureDetector.onTouchEvent(event);
            this.editModeScaleGestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class EditModeOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            shouldHighlight = true;
            invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            DragShadowBuilder myShadow = new DragShadowBuilder(WidgetView.this);
            WidgetView.this.startDrag(null, myShadow, WidgetView.this, 0);
            shouldHighlight = false;
            invalidate();
        }
    }


    private class EditModeOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float scaleFactorX;
        private float scaleFactorY;

        private void setScaleFactor(ScaleGestureDetector detector) {
            scaleFactorX *= detector.getPreviousSpanX() > 0 ? detector.getCurrentSpanX() / detector.getPreviousSpanX() : 1;
            scaleFactorY *= detector.getPreviousSpanY() > 0 ? detector.getCurrentSpanY() / detector.getPreviousSpanY() : 1;
        }

        private BaseEntity prepareScale() {
            IPositionEntity posEntity = (IPositionEntity) widgetEntity.copy();
            Position position = posEntity.getPosition();
            position.width = Math.max(1, Math.round((getWidth() * scaleFactorX) / tileWidth));
            position.height = Math.max(1, Math.round((getHeight() * scaleFactorY) / tileWidth));
            posEntity.setPosition(position);
            return (BaseEntity) posEntity;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            scaleFactorX = 1f;
            scaleFactorY = 1f;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            setScaleFactor(detector);
            WidgetView.this.onWidgetEditListener.onWidgetEdited(prepareScale(), false);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            setScaleFactor(detector);
            WidgetView.this.onWidgetEditListener.onWidgetEdited(prepareScale(), true);
        }
    }
}
