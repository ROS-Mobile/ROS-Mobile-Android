package com.schneewittchen.rosandroid.model.entities;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.09.20
 */
public abstract class PublisherEntity extends BaseEntity {

    public float publishRate = 1f;
    public boolean immediatePublish = false;


    @Override
    public boolean equalRosState(BaseEntity other) {
        if (!super.equalRosState(other)) {
            return false;
        }

        if (!(other instanceof PublisherEntity)) {
            return false;
        }

        PublisherEntity otherPub = (PublisherEntity) other;

        return this.publishRate == otherPub.publishRate
                && this.immediatePublish == otherPub.immediatePublish;
    }
}
