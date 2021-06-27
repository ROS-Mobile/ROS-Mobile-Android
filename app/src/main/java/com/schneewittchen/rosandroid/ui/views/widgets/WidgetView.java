package com.schneewittchen.rosandroid.ui.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.IPositionEntity;
import com.schneewittchen.rosandroid.ui.general.Position;


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

        this.setOnLongClickListener(view -> {
            DragShadowBuilder myShadow = new DragShadowBuilder(this);
            this.startDrag(null, myShadow, this, 0);
            return true;
        });
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
}
