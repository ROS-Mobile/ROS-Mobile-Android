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

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.TextureBitmap;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.internal.message.Message;
import org.ros.internal.message.MessageBuffers;
import org.ros.namespace.GraphName;
import org.ros.rosjava_geometry.Quaternion;
import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import nav_msgs.OccupancyGrid;


/**
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class OccupancyGridLayer extends SubscriberLayer<OccupancyGrid> implements TfLayer {

    public static String TAG = OccupancyGridLayer.class.getSimpleName();

    /**
     * Color of occupied cells in the map.
     */
    private static final int COLOR_OCCUPIED = 0xff111111;

    /**
     * Color of free cells in the map.
     */
    private static final int COLOR_FREE = 0xddffffff;

    /**
     * Color of unknown cells in the map.
     */
    private static final int COLOR_UNKNOWN = 0x88ffffff;

    /**
     * Color of transparent cells in the map.
     */
    private static final int COLOR_TRANSPARENT = 0x00000000;


    /**
     * In order to draw maps with a size outside the maximum size of a texture,
     * we split the map into multiple tiles and draw one texture per tile.
     */
    private class Tile {

        private final ChannelBuffer pixelBuffer = MessageBuffers.dynamicBuffer();
        private final TextureBitmap textureBitmap = new TextureBitmap();

        /**
         * Resolution of the {@link OccupancyGrid}.
         */
        private final float resolution;

        /**
         * Points to the top left of the {@link Tile}.
         */
        private Transform origin;

        /**
         * Width of the {@link Tile}.
         */
        private int stride;

        /**
         * {@code true} when the {@link Tile} is ready to be drawn.
         */
        private boolean ready;

        public Tile(float resolution) {
            this.resolution = resolution;
            ready = false;
        }

        public void draw(VisualizationView view, GL10 gl) {
            if (ready) {
                textureBitmap.draw(view, gl);
            }
        }

        public void clearHandle() {
            textureBitmap.clearHandle();
        }

        public void writeInt(int value) {
            pixelBuffer.writeInt(value);
        }

        public void update() {
            Preconditions.checkNotNull(origin);
            textureBitmap.updateFromPixelBuffer(pixelBuffer, stride, resolution, origin, COLOR_TRANSPARENT);
            pixelBuffer.clear();
            ready = true;
        }

        public void setOrigin(Transform origin) {
            this.origin = origin;
        }

        public void setStride(int stride) {
            this.stride = stride;
        }
    }

    private final List<Tile> tiles;

    private boolean ready;
    private GraphName frame;
    private GL10 previousGl;

    public OccupancyGridLayer(String topic) {
        this(GraphName.of(topic));
    }

    public OccupancyGridLayer(GraphName topic) {
        super(topic, OccupancyGrid._TYPE);
        tiles = Lists.newCopyOnWriteArrayList();
        ready = false;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        Log.i(TAG, "DRAW");

        if (previousGl != gl) {
            for (Tile tile : tiles) {
                tile.clearHandle();
            }
            previousGl = gl;
        }
        if (ready) {
            for (Tile tile : tiles) {
                tile.draw(view, gl);
            }
        }
    }

    @Override
    public boolean reactOnMessage(VisualizationView view, Message message) {
        if (!(message instanceof OccupancyGrid)) return false;

        Log.i(TAG, "Got map");
        update((OccupancyGrid)message);
        return true;
    }


    @Override
    public GraphName getFrame() {
        return frame;
    }


    private void update(OccupancyGrid message) {
        final float resolution = message.getInfo().getResolution();
        final int width = message.getInfo().getWidth();
        final int height = message.getInfo().getHeight();

        final int numTilesWide = (int) Math.ceil(width / (float) TextureBitmap.STRIDE);
        final int numTilesHigh = (int) Math.ceil(height / (float) TextureBitmap.STRIDE);

        Log.i(TAG, "Resolution: " + resolution + " Width: " + width + " Height: " + height + " " + numTilesWide + " " + numTilesHigh);

        final int numTiles = numTilesWide * numTilesHigh;
        final Transform origin = Transform.fromPoseMessage(message.getInfo().getOrigin());

        while (tiles.size() < numTiles) {
            tiles.add(new Tile(resolution));
        }

        for (int y = 0; y < numTilesHigh; ++y) {
            for (int x = 0; x < numTilesWide; ++x) {
                final int tileIndex = y * numTilesWide + x;
                tiles.get(tileIndex).setOrigin(origin.multiply(new Transform(new Vector3(x *
                        resolution * 60,
                        y * resolution * 60, 0.), Quaternion.identity())));
                tiles.get(tileIndex).setStride(60);

                /*
                tiles.get(tileIndex).setOrigin(origin.multiply(new Transform(new Vector3(x *
                        resolution * TextureBitmap.STRIDE,
                        y * resolution * TextureBitmap.HEIGHT, 0.), Quaternion.identity())));
                tiles.get(tileIndex).setStride(TextureBitmap.STRIDE);
                */

            }
        }

        int x = 0;
        int y = 0;
        final ChannelBuffer buffer = message.getData();
        while (buffer.readable()) {
            Preconditions.checkState(y < height);
            final int tileIndex = (y / TextureBitmap.STRIDE) * numTilesWide + x / TextureBitmap.STRIDE;
            final byte pixel = buffer.readByte();
            if (pixel == -1) {
                tiles.get(tileIndex).writeInt(COLOR_UNKNOWN);
            } else if (pixel < 50) {
                tiles.get(tileIndex).writeInt(COLOR_FREE);
            } else {
                tiles.get(tileIndex).writeInt(COLOR_OCCUPIED);
            }

            ++x;
            if (x == width) {
                x = 0;
                ++y;
            }
        }

        for (Tile tile : tiles) {
            tile.update();
        }

        frame = GraphName.of(message.getHeader().getFrameId());
        ready = true;
    }
}
