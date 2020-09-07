package com.schneewittchen.rosandroid.widgets.debug;

import android.renderscript.Sampler;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.internal.message.Message;
import org.ros.internal.message.field.Field;
import org.ros.internal.message.field.ListField;
import org.ros.internal.node.response.Response;
import org.ros.master.client.TopicType;

import java.lang.reflect.Proxy;
import java.lang.reflect.WildcardType;
import java.util.List;

import nav_msgs.Odometry;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on
 * @modified by
 */

public class DebugData extends BaseData {

    // The data
    Message data;

    // The constructor
    public DebugData(Message message) {
        List<Field> fieldList = message.toRawMessage().getFields();
        Object value = fieldList.get(0).getValue();
        toString((Field) value);
        /* Odometry odometry = (Odometry) message;
        String channel = "";
        message.toRawMessage().getChannelBuffer(channel); */
        /* System.out.println(message.toRawMessage().getDefinition());
        System.out.println(message.toRawMessage().getFields().toString());
        System.out.println(message.toRawMessage().getFields().get(0).getValue().toString());
        System.out.println(message.toRawMessage().getFields().get(1).getValue().toString());
        System.out.println(message.toRawMessage().getFields().get(2).getValue().toString());*/



        //System.out.println(odometry.getPose().getPose().getPosition().getX());
        //System.out.println(odometry.getPose().getPose().getPosition().getY());
        //System.out.println(odometry.getPose().getPose().getPosition().getZ());



        this.data = message;
    }

    void toString(Field field) {
        Object value = field.getValue();
        System.out.println(value + "");
        if(value instanceof Field) {
            toString((Field) value);
        }
    }
}
