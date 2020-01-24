package com.schneewittchen.rosandroidlib.configuration;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
public class Master {

    public String ip;
    public int port;
    public String notificationTitle;
    public String notificationTickerTitle;


    public Master(){
        this.ip = "masterIP";
        this.port = 11311;
        this.notificationTitle = "title";
        this.notificationTickerTitle = "tickerTitle";
    }
}
