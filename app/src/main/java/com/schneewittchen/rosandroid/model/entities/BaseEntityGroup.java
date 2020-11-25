package com.schneewittchen.rosandroid.model.entities;

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
public abstract class BaseEntityGroup extends BaseEntity {

    public List<BaseEntity> widgets;


    public BaseEntityGroup() {
        widgets = new ArrayList<>();
    }
}
