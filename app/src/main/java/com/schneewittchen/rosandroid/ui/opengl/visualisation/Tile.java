package com.schneewittchen.rosandroid.ui.opengl.visualisation;

/**
 * In order to draw maps with a size outside the maximum size of a texture,
 * we split the map into multiple tiles and draw one texture per tile.
 *
 * @author moesenle@google.com (Lorenz Moesenlechner)
 * @version 2.0
 * @updated on 08.03.21
 */

import com.google.common.base.Preconditions;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.internal.message.MessageBuffers;
import org.ros.rosjava_geometry.Transform;

import javax.microedition.khronos.opengles.GL10;

import nav_msgs.OccupancyGrid;


public class Tile {

    /**
     * Color of transparent cells in the map.
     */
    private static final int COLOR_TRANSPARENT = 0x00000000;

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
     * Height of the {@link Tile}.
     */
    private int height;

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
        textureBitmap.updateFromPixelBuffer(pixelBuffer, stride, height, resolution, origin, COLOR_TRANSPARENT);
        pixelBuffer.clear();
        ready = true;
    }

    public void setOrigin(Transform origin) {
        this.origin = origin;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
