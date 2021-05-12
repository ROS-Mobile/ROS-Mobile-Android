package com.schneewittchen.rosandroid.model.entities.widgets;

import android.util.Log;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1
 * @created on 23.09.20
 * @updated on 10.03.21
 * @modified by Nico Studt
 */
public abstract class BaseEntity {

    public long id;
    public String name;
    public String type;
    public long configId;
    public long creationTime;
    public Topic topic;
    public boolean validMessage;
    public ArrayList<BaseEntity> childEntities;


    public BaseEntity() {
        childEntities = new ArrayList<>();
    }


    public boolean equalRosState(BaseEntity other) {
        return this.topic.equals(other.topic);
    }

    public void addEntity(BaseEntity entity) {
        childEntities.add(entity);
    }

    public BaseEntity getChildById(long id) {
        for (BaseEntity child : childEntities) {
            if (child.id == id) return child;
        }

        return null;
    }

    public void removeChild(BaseEntity entity) {
        ListIterator<BaseEntity> iter = childEntities.listIterator();
        while (iter.hasNext()) {
            if (iter.next().id == entity.id) {
                iter.remove();
            }
        }
    }

    public void replaceChild(BaseEntity entity) {
        for (int i = 0; i < childEntities.size(); i++) {
            if (childEntities.get(i).id == entity.id) {
                childEntities.set(i, entity);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        else if (o == this)
            return true;

        else if (o.getClass() != this.getClass())
            return false;

        // Check if other object has equal field values
        for (Field f : this.getClass().getFields()) {
            try {
                boolean equalValues = Objects.equals(f.get(this), f.get(o));

                if (!equalValues) {
                    return false;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public BaseEntity copy() {
        try {
            Constructor constructor = this.getClass().getConstructor();
            Object newObj = constructor.newInstance();

            for (Field f : this.getClass().getFields()) {
                Object value = f.get(this);

                // Check if children available and if object type is matching
                if (value instanceof List) {
                    ArrayList<BaseEntity> children = new ArrayList<>();
                    for (BaseEntity child : this.childEntities) {
                        children.add(child.copy());
                    }
                    f.set(newObj, children);

                } else if (f.getType().equals(Topic.class)) {
                    Topic topicObj = (Topic) f.get(this);
                    f.set(newObj, new Topic(topicObj));

                } else {
                    f.set(newObj, f.get(this));
                }
            }

            return (BaseEntity) newObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        String output = "[Widget: ";

        int i = 0;
        for (Field f : this.getClass().getFields()) {

            if (i > 0) {
                output += ", ";
            }

            try {
                output += f.getName() + ": ";

                if (f.get(this) == null) {
                    output += "null";
                } else {
                    output += f.get(this).toString();
                }

            } catch (IllegalAccessException e) {
            }

            i++;
        }

        output += "]";
        return output;
    }
}
