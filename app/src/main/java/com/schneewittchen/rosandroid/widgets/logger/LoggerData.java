package com.schneewittchen.rosandroid.widgets.logger;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;


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

    public String data;

    public LoggerData(std_msgs.String message) {
        this.data = message.getData();
    }
}
