/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.schneewittchen.rosandroid.widgets.gltest.layer;

import com.google.common.base.Preconditions;

import org.ros.android.view.visualization.TextureBitmap;
import org.ros.android.view.visualization.VisualizationView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.rosjava_geometry.Transform;

import javax.microedition.khronos.opengles.GL10;

import nav_msgs.OccupancyGrid;


/**
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class CompressedOccupancyGridLayer extends SubscriberLayer<OccupancyGrid> implements
        TfLayer {

    /**
     * Color of occupied cells in the map.
     */
    private static final int COLOR_OCCUPIED = 0xdfffffff;

    /**
     * Color of free cells in the map.
     */
    private static final int COLOR_FREE = 0xff8d8d8d;

    /**
     * Color of unknown cells in the map.
     */
    private static final int COLOR_UNKNOWN = 0xff000000;

    private final TextureBitmap textureBitmap;

    private boolean ready;
    private GraphName frame;

    public CompressedOccupancyGridLayer(String topic) {
        this(GraphName.of(topic));
    }

    public CompressedOccupancyGridLayer(GraphName topic) {
        super(topic, OccupancyGrid._TYPE);
        textureBitmap = new TextureBitmap();
        ready = false;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (ready) {
            textureBitmap.draw(view, gl);
        }
    }

    @Override
    public GraphName getFrame() {
        return frame;
    }

    @Override
    public void onStart(VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        getSubscriber().addMessageListener(new MessageListener<OccupancyGrid>() {
            @Override
            public void onNewMessage(OccupancyGrid message) {
                update(message);
            }
        });
    }

    void update(OccupancyGrid message) {
        ChannelBuffer buffer = message.getData();
        Bitmap bitmap =
                BitmapFactory.decodeByteArray(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
        int stride = bitmap.getWidth();
        int height = bitmap.getHeight();
        Preconditions.checkArgument(stride <= 1024);
        Preconditions.checkArgument(height <= 1024);
        int[] pixels = new int[stride * height];
        bitmap.getPixels(pixels, 0, stride, 0, 0, stride, height);
        for (int i = 0; i < pixels.length; i++) {
            // Pixels are ARGB packed ints.
            if (pixels[i] == 0xffffffff) {
                pixels[i] = COLOR_UNKNOWN;
            } else if (pixels[i] == 0xff000000) {
                pixels[i] = COLOR_FREE;
            } else {
                pixels[i] = COLOR_OCCUPIED;
            }
        }
        float resolution = message.getInfo().getResolution();
        Transform origin = Transform.fromPoseMessage(message.getInfo().getOrigin());
        textureBitmap.updateFromPixelArray(pixels, stride, resolution, origin, COLOR_UNKNOWN);
        frame = GraphName.of(message.getHeader().getFrameId());
        ready = true;
    }
}
