package com.schneewittchen.rosandroid.widgets.switchbutton;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import std_msgs.Bool;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.05.2022
 */
public class SwitchButtonEntity extends PublisherWidgetEntity {

    public String onText;
    public String offText;


    public SwitchButtonEntity() {
        this.width = 2;
        this.height = 1;
        this.topic = new Topic("switch_state", Bool._TYPE);
        this.immediatePublish = true;
        this.onText = "ON";
        this.offText = "OFF";
    }

}
