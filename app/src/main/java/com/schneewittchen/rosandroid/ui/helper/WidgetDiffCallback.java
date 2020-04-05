package com.schneewittchen.rosandroid.ui.helper;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 05.02.20
 * @updated on 02.04.20
 * @modified by
 */
public class WidgetDiffCallback extends DiffUtil.Callback{

    public static String TAG = WidgetDiffCallback.class.getSimpleName();

    List<BaseEntity> oldWidgets;
    List<BaseEntity> newWidgets;

    public WidgetDiffCallback(List<BaseEntity> newWidgets, List<BaseEntity> oldWidgets) {
        this.newWidgets = newWidgets;
        this.oldWidgets = oldWidgets;
    }

    @Override
    public int getOldListSize() {
        return oldWidgets.size();
    }

    @Override
    public int getNewListSize() {
        return newWidgets.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        BaseEntity oldWidget = oldWidgets.get(oldItemPosition);
        BaseEntity newWidget = newWidgets.get(newItemPosition);
        boolean itemsSame = oldWidget.id == newWidget.id;

        Log.i(TAG, "Items same (" + oldItemPosition + " " + newItemPosition + "): " + itemsSame);
        return itemsSame;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseEntity oldWidget = oldWidgets.get(oldItemPosition);
        BaseEntity newWidget = newWidgets.get(newItemPosition);

        boolean objectEquals = oldWidget.equals(newWidget);
        boolean contentSame = oldWidget.equalContent(newWidget);

        Log.i(TAG, "(" + oldItemPosition + " " + newItemPosition + "): Objects equals " + objectEquals + "  Content same " + contentSame);

        return  objectEquals && contentSame;
    }

}
