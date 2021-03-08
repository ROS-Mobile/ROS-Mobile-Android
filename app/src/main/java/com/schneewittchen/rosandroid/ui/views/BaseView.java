package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.ui.general.Position;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.20
 * @updated on 21.04.20
 * @modified by Nils Rottmann
 * @updated on 02.10.20
 * @modified by Nico Studt
 */
public abstract class BaseView extends ViewGroup {

    public static String TAG = BaseView.class.getSimpleName();

    protected Position position;
    protected BaseEntity widgetEntity;


    public BaseView(Context context) {
        super(context);
        baseInit();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        baseInit();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        baseInit();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { }


    private void baseInit() {
        setWillNotDraw(false);
    }


    public void updatePosition() {
        this.position = new Position(widgetEntity.posX, widgetEntity.posY,
                                     widgetEntity.width, widgetEntity.height);
    }

    public Position getPosition() {
        return this.position;
    }

    public void setWidgetEntity(BaseEntity widgetEntity) {
        this.widgetEntity = widgetEntity;
    }

    public Topic getTopic() {
        return this.widgetEntity.topic;
    }
    
    public boolean sameWidget(BaseEntity otherWidget) {
        return otherWidget.id == this.widgetEntity.id;
    }
}
