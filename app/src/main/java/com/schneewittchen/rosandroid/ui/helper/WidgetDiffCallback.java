package com.schneewittchen.rosandroid.ui.helper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 05.02.20
 * @updated on 05.02.20
 * @modified by
 */
public class WidgetDiffCallback extends DiffUtil.Callback{

    List<WidgetEntity> oldWidgets;
    List<WidgetEntity> newWidgets;

    public WidgetDiffCallback(List<WidgetEntity> newWidgets, List<WidgetEntity> oldWidgets) {
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
        return oldWidgets.get(oldItemPosition).id == newWidgets.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        WidgetEntity oldWidget = oldWidgets.get(oldItemPosition);
        WidgetEntity newWidget = newWidgets.get(newItemPosition);

        return oldWidget.id == newWidget.id;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
