package com.schneewittchen.rosandroid.model.entities;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class SubPubNoteEntity {

    public String topic;
    public Class<? extends Message> messageType;
}
