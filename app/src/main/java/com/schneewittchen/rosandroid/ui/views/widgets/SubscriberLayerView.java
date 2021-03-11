package com.schneewittchen.rosandroid.ui.views.widgets;

import android.content.Context;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import org.ros.internal.message.Message;

import javax.microedition.khronos.opengles.GL10;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public abstract class SubscriberLayerView extends LayerView implements ISubscriberView{

    public SubscriberLayerView(Context context) {
        super(context);
    }


    @Override
    public void onNewMessage(Message message) {
        return;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {}
}
