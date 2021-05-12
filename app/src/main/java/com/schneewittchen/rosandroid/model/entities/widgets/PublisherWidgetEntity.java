package com.schneewittchen.rosandroid.model.entities.widgets;


import com.schneewittchen.rosandroid.ui.general.Position;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.09.20
 */
public abstract class PublisherWidgetEntity
        extends BaseEntity
        implements IPositionEntity, IPublisherEntity{

    public float publishRate = 1f;
    public boolean immediatePublish = false;
    public int posX;
    public int posY;
    public int width;
    public int height;


    @Override
    public boolean equalRosState(BaseEntity other) {
        if (!super.equalRosState(other)) {
            return false;
        }

        if (!(other instanceof PublisherWidgetEntity)) {
            return false;
        }

        PublisherWidgetEntity otherPub = (PublisherWidgetEntity) other;

        return this.publishRate == otherPub.publishRate
                && this.immediatePublish == otherPub.immediatePublish;
    }

    @Override
    public Position getPosition() {
        return new Position(posX, posY, width, height);
    }

    @Override
    public void setPosition(Position position) {
        this.posX = position.x;
        this.posY = position.y;
        this.width = position.width;
        this.height = position.height;
    }
}
