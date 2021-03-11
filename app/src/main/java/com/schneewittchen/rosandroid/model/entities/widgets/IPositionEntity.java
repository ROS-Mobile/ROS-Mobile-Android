package com.schneewittchen.rosandroid.model.entities.widgets;

import com.schneewittchen.rosandroid.ui.general.Position;

/**
 * Entity with positional information to be able to display it
 * in the visualisation view as a stand-alone widget.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public interface IPositionEntity {

    Position getPosition();
    void setPosition(Position position);
}
