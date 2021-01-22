package com.schneewittchen.rosandroid.widgets.logger;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import std_msgs.String;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LoggerData extends BaseData {

    private String Data;

    public LoggerData(String data) {
        Data = data;
    }
}
