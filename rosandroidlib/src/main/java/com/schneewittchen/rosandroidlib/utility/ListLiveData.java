package com.schneewittchen.rosandroidlib.utility;

import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 27.01.20
 * @updated on 27.01.20
 * @modified by
 */
public class ListLiveData<T> extends LiveData<List<T>> {


    public ListLiveData() {
        this.setValue(new ArrayList<T>());
    }


    public void add(T item) {
        Preconditions.checkNotNull(this.getValue());

        this.getValue().add(item);
        this.setValue(this.getValue());
    }

    public void addAll(Collection<? extends T> list) {
        Preconditions.checkNotNull(this.getValue());

        this.getValue().addAll(list);
        this.setValue(getValue());
    }

    public T getItem(int index){
        Preconditions.checkNotNull(this.getValue());

        return this.getValue().get(index);
    }

    public void remove(int index) {
        Preconditions.checkNotNull(this.getValue());

        this.getValue().remove(index);
        this.setValue(getValue());
    }

    public void remove(T item) {
        Preconditions.checkNotNull(this.getValue());

        this.getValue().remove(item);
        this.setValue(getValue());
    }

    public void clear() {
        Preconditions.checkNotNull(this.getValue());

        this.getValue().clear();
        this.setValue(getValue());
    }


    @Override
    public void setValue(List<T> value) {
        super.setValue(value);
    }

    @Nullable
    @Override
    public List<T> getValue() {
        return super.getValue();
    }
}
