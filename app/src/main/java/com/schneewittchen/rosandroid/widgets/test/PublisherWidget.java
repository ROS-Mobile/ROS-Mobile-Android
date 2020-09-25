package com.schneewittchen.rosandroid.widgets.test;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.09.20
 */
public abstract class PublisherWidget extends BaseWidget {

    public float publishRate = 1f;
    public boolean immediatePublish = false;


    @Override
    public boolean equals(Object o) {
        if(super.equals(o))
            return false;

        PublisherWidget other = (PublisherWidget) o;

         return other.publishRate == this.publishRate
                 && other.immediatePublish == this.immediatePublish;
    }
}
