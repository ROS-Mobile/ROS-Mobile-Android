package com.schneewittchen.rosandroid.widgets.base;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.20
 * @updated on 15.03.20
 * @modified by
 */
public interface Interactable {

    void informDataChange(WidgetData data);

    void setData(WidgetData data);

    void setDataListener(DataListener listener);

    void removeDataListener();

    void setDataId(long id);

    long getDataId();
}
