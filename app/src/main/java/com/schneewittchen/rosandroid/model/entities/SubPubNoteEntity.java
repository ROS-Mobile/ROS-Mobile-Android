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
    public String messageType;


    public SubPubNoteEntity() {}

    public SubPubNoteEntity(SubPubNoteEntity other) {
        this.topic = other.topic;
        this.messageType = other.messageType;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof SubPubNoteEntity) {
            SubPubNoteEntity other = (SubPubNoteEntity) o;

            return other.topic.equals(topic)
                    && other.messageType.equals(messageType);
        }

        return false;
    }
}
