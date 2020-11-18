package com.schneewittchen.rosandroid.widgets.label;

import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import org.ros.node.topic.Subscriber;

import std_msgs.Empty;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LabelEntity extends BaseEntity {

    public String text;
    public int rotation;

    public LabelEntity() {
        this.width=3;
        this.height=1;
        this.topic = new Topic("label", Empty._TYPE);
        this.text = "A label";
        this.rotation = 0;
    }
}
