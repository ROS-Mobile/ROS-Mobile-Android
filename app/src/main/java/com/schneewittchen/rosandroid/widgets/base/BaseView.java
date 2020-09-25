package com.schneewittchen.rosandroid.widgets.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.widgets.test.BaseWidget;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.20
 * @updated on 21.04.20
 * @modified by Nils Rottmann
 */
public class BaseView extends View implements Interactable {

    DataListener dataListener;
    long dataId;
    Position position;
    BaseWidget widgetEntity;


    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void informDataChange(BaseData data) {
        if(dataListener != null) {
            data.setId(getDataId());
            dataListener.onNewData(data);
        }
    }

    @Override
    public void setData(BaseData data) {
        // Default data set, but nothing to see here
    }

    @Override
    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    @Override
    public void removeDataListener() {
        this.dataListener = null;
    }

    @Override
    public void setDataId(long id) {
        this.dataId = id;
    }

    @Override
    public long getDataId() {
        return this.dataId;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setWidgetEntity(BaseWidget widgetEntity) {
        Position position = new Position(widgetEntity.posX, widgetEntity.posY,
                                        widgetEntity.width, widgetEntity.height);
        this.widgetEntity = widgetEntity;
        this.setDataId(widgetEntity.id);
        this.setPosition(position);
    }
    
    public boolean sameWidget(BaseWidget other) {
        return this.dataId == other.id;
    }
}
