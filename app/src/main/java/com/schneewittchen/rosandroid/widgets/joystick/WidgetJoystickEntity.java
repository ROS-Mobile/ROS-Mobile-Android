package com.schneewittchen.rosandroid.widgets.joystick;


import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.WidgetNode;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 13.03.20
 * @modified by
 */
public class WidgetJoystickEntity extends WidgetEntity {

    public WidgetJoystickEntity() {
        this.setType(WidgetEntity.JOYSTICK);
    }


    @Override
    public String getName() {
        return "Joystick";
    }

    public Class<? extends WidgetNode> getNodeType() {
        return JoystickNode.class;
    }

    public Class<? extends BaseView> getViewType() {
        return JoystickView.class;
    }

    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return JoystickDetailVH.class;
    }

    public int getDetailViewLayoutId() {
        return R.layout.widget_detail_joystick;
    }

}
