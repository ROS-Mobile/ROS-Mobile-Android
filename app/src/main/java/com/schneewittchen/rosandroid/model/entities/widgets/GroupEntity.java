package com.schneewittchen.rosandroid.model.entities.widgets;

import com.schneewittchen.rosandroid.ui.general.Position;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 * @updated on
 * @modified by
 */
public class GroupEntity extends BaseEntity
        implements IPositionEntity,
        ISubscriberEntity,
        IPublisherEntity{

    public int posX;
    public int posY;
    public int width;
    public int height;

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
