package com.schneewittchen.rosandroid.widgets.debug;

import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

import org.apache.commons.lang.StringUtils;
import org.ros.internal.message.Message;
import org.ros.internal.message.field.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.2020
 * @updated on 07.10.2020
 * @modified by Nico Studt
 */

public class DebugData extends BaseData {

    // The data
    Message data;

    public String value;


    public DebugData(Message message) {
        value = msgToString(message, 0);
        value += "---------";
        Log.d(TAG, value);
    }


    private String msgToString(Message message, int level) {
        List<Field> fields = message.toRawMessage().getFields();
        StringBuilder out = new StringBuilder();

        for(Field field: fields) {
            out.append(fieldToString(field, level));
        }

        return out.toString();
    }

    private String fieldToString(Field field, int level) {
        String out = StringUtils.repeat("\t", level);
        out += field.getName();
        out += ": ";

        Object value = field.getValue();

        if (value instanceof Field) {
            out = fieldToString(field, level + 1);

        } else if (value instanceof Message) {
            out += "\n" + msgToString((Message)value, level + 1);

        } else if (value instanceof Collection) {
            out += "[";

            for (Object o: (Collection<?>)value) {
                out += o + ", ";
            }

            out += "]";

        }  else {
            out += value;
        }

        return out + "\n";
    }
}
