package com.schneewittchen.rosandroid.widgets.logger;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

public class LoggerData extends BaseData {
    public  String Data;
    public LoggerData(String data, long id) {
        setId(id);
        Data = data;
    }
    public LoggerData() {
        Data = "";
    }
}
