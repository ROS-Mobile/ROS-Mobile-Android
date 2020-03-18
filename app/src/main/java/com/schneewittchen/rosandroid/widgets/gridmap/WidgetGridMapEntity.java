package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 16.02.20
 * @modified by
 */
public class WidgetGridMapEntity extends WidgetEntity {

    String topic;
    int colorScheme;
    boolean drawBehind;
    boolean useTimeStamp;


    public WidgetGridMapEntity() {
        this.setType(WidgetEntity.MAP);
    }

    public Class<? extends BaseView> getViewType() {
        return GridMapView.class;
    }

    @Override
    public String getName() {
        return "Map";
    }
}
