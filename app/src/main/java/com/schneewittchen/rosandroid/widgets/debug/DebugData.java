package com.schneewittchen.rosandroid.widgets.debug;

import android.os.Message;

import com.schneewittchen.rosandroid.widgets.base.BaseData;



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

    public static final String TAG = "DebugData";

    public DebugData(Message message) {
        System.out.println(message.arg1);
    }
}
