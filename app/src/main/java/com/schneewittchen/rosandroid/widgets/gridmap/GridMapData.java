package com.schneewittchen.rosandroid.widgets.gridmap;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

import org.jboss.netty.buffer.ChannelBuffer;

import nav_msgs.OccupancyGrid;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 18.10.19
 * @updated on 10.09.20
 * @modified by Nico Studt
 */
public class GridMapData extends BaseData {

    public static final String TAG = GridMapData.class.getSimpleName();


    // Color of occupied cells in the map, it A = 255, R = G = B = 255
    private static final int COLOR_FREE = 0xdfffffff;
    // Color of free cells in the map, it is A = 255, R = G = B = 0
    private static final int COLOR_OCCUPIED = 0xff000000;
    // Color of unknown cells in the map, it is A = 127, R = G = B = 100
    private static final int COLOR_UNKNOWN = 0x7F646464;

    public Bitmap map;


    public GridMapData(OccupancyGrid grid) {
        Log.i(TAG, "New Data");
        this.map = getGrayBitMap(grid);
    }

    private Bitmap getGrayBitMap(OccupancyGrid grid) {
        ChannelBuffer buffer = grid.getData();
        byte[] data = buffer.array();
        int offset = buffer.arrayOffset();
        int dataLength = buffer.readableBytes();
        int end = offset + dataLength;
        int step = 0;
        int[] pixels = new int[dataLength];

        for (int i = offset; i < end; i++) {
            // Pixels are ARGB packed ints.
            if (data[i] == -1) {
                pixels[step++] = COLOR_UNKNOWN;
            } else if (data[i] < 50) {
                pixels[step++] = COLOR_FREE;
            } else {
                pixels[step++] = COLOR_OCCUPIED;
            }
        }

        return Bitmap.createBitmap(pixels, grid.getInfo().getWidth(),
                grid.getInfo().getHeight(), Bitmap.Config.ARGB_8888);
    }

    private Bitmap getBitMap(OccupancyGrid grid) {
        // Get the data from the occupancy grid message
        ChannelBuffer buffer = grid.getData();
        byte[] dataAll = buffer.array();
        int dataOffset = buffer.arrayOffset();
        int dataLength = buffer.readableBytes();
        byte[] data = new byte[dataLength];

        for (int i=0; i<dataLength; i++) {
            data[i] = dataAll[dataOffset + i];
        }

        // Get the pixel color, TODO: Maybe different grayscales depending on thee occupancy value
        int[] pixels = new int[dataLength];
        for (int i = 0; i < dataLength; i++) {
            // Pixels are ARGB packed ints.
            if (data[i] == -1) {
                pixels[i] = COLOR_UNKNOWN;
            } else if (data[i] < 50) {
                pixels[i] = COLOR_FREE;
            } else {
                pixels[i] = COLOR_OCCUPIED;
            }
        }

        // Generate Bitmap
        Bitmap mapFlipped = Bitmap.createBitmap(pixels, grid.getInfo().getWidth(),
                                grid.getInfo().getHeight(), Bitmap.Config.ARGB_8888);
        // Flip Bitmap
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);

        return Bitmap.createBitmap(mapFlipped, 0, 0, mapFlipped.getWidth(), mapFlipped.getHeight(), matrix, true);
    }
}
