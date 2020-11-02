package com.schneewittchen.rosandroid.model.entities;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 */
public abstract class BaseEntity {

    public long id;
    public String name;
    public String type;
    public long configId;
    public long creationTime;
    public Topic topic;
    public int posX;
    public int posY;
    public int width;
    public int height;
    public boolean validMessage;


    public boolean equalRosState(BaseEntity other) {
        return this.topic.equals(other.topic);
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
        for(Field f: this.getClass().getFields()) {
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

            for(Field f: this.getClass().getFields()) {
                if (f.getType().equals(Topic.class)) {
                    Topic topicObj = (Topic) f.get(this);
                    f.set(newObj, new Topic(topicObj));

                } else {
                    f.set(newObj, f.get(this));
                }
            }

            return (BaseEntity)newObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
