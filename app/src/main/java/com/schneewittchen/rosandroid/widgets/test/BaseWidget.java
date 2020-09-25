package com.schneewittchen.rosandroid.widgets.test;

import com.schneewittchen.rosandroid.model.rosRepo.message.Topic;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 */
public abstract class BaseWidget {

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


    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        else if (o == this)
            return true;

        else if (o.getClass() != this.getClass())
            return false;

        BaseWidget other = (BaseWidget) o;

        return other.id == this.id
                && other.name.equals(this.name)
                && other.type.equals(this.type)
                && other.configId == this.configId
                && other.creationTime == this.creationTime
                && other.topic.equals(topic)
                && other.posX == this.posX
                && other.posY == this.posY
                && other.width == this.width
                && other.height == this.height;
    }
}
