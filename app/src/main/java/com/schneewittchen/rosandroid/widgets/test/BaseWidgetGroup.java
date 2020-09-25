package com.schneewittchen.rosandroid.widgets.test;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 * @updated on
 * @modified by
 */
public abstract class BaseWidgetGroup extends BaseWidget {

    public List<BaseWidget> widgets;


    public BaseWidgetGroup() {
        widgets = new ArrayList<>();
    }
}
