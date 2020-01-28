package com.schneewittchen.rosandroidlib.model.entities;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 25.01.20
 * @updated on 25.01.20
 * @modified by
 */
public class SubPubNode {

    public String topic;
    public Class<? extends Message> messageType;

}
