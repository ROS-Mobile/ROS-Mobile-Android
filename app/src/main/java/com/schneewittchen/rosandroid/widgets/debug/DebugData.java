package com.schneewittchen.rosandroid.widgets.debug;

import android.util.Log;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.apache.commons.lang.StringUtils;
import org.ros.internal.message.Message;
import org.ros.internal.message.field.Field;
import org.ros.internal.message.field.ListField;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.2020
 * @updated on 07.09.2020
 * @modified by Nico Studt
 */
public class DebugData extends BaseData {

    public static final String TAG = DebugData.class.getSimpleName();
    public String value;
    private ArrayList<String> content;


    public DebugData(Message message) {
        content = new ArrayList<>();
        msgToString(message, 0);

        content.add("---------");
        value = joinContent("\n", content);
    }


    private void msgToString(Message message, int level) {
        List<Field> fields = message.toRawMessage().getFields();

        for(Field field: fields) {
            fieldToString(field, level);
        }
    }

    private void fieldToString(Field field, int level) {
        String fieldString = StringUtils.repeat("\t", level) + field.getName() + ":";
        content.add(fieldString);

        Object value = field.getValue();

        if (field instanceof ListField) {
            for (Object o: ((ListField) field).getValue()) {
                String listPrefix = StringUtils.repeat("\t", level+1) + "-";
                content.add(listPrefix);

                if (o instanceof String) {
                    content.add((String) o);
                } else if (o instanceof Message) {
                    msgToString((Message) o, level + 2);
                }
            }

        }else if (value instanceof Field) {
            fieldToString(field, level + 1);

        } else if (value instanceof Message) {
            msgToString((Message)value, level + 1);

        } else {
            String valueStr;

            if (value.getClass().isArray()) {
                // Value is a type of list
                int length = Array.getLength(value);
                valueStr = "[";

                for(int i = 0; i < length; i++){
                    if (i > 0)
                        valueStr += ", ";

                    valueStr += Array.get(value, i);
                }

                valueStr += "]";

            } else {
                // Only single value
                valueStr = String.valueOf(value);
            }

            String last = content.get(content.size() -1);
            content.set(content.size() -1, last + " " + valueStr);
        }

    }

    private String joinContent(String delimiter, List<String> content) {
        String loopDelim = "";
        StringBuilder out = new StringBuilder();

        for(String s : content) {
            out.append(loopDelim);
            out.append(s);

            loopDelim = delimiter;
        }

        return out.toString();
    }
}