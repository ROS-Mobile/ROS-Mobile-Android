package com.schneewittchen.rosandroid.model.entities.widgets;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.09.20
 */
public abstract class PublisherLayerEntity extends BaseEntity
        implements I2DLayerEntity, IPublisherEntity {

    public float publishRate = 1f;
    public boolean immediatePublish = false;


    @Override
    public boolean equalRosState(BaseEntity other) {
        if (!super.equalRosState(other)) {
            return false;
        }

        if (!(other instanceof PublisherLayerEntity)) {
            return false;
        }

        PublisherLayerEntity otherPub = (PublisherLayerEntity) other;

        return this.publishRate == otherPub.publishRate
                && this.immediatePublish == otherPub.immediatePublish;
    }
}
