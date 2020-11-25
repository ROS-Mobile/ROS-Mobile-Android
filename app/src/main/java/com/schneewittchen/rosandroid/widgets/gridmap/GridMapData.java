package com.schneewittchen.rosandroid.widgets.gridmap;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.jboss.netty.buffer.ChannelBuffer;

import nav_msgs.MapMetaData;
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
    public static final int[] gradient = getGradient();

    public Bitmap map;


    public GridMapData(OccupancyGrid grid) {
        map = createGrayMap(grid);
    }


    private Bitmap createGrayMap(OccupancyGrid grid) {
        MapMetaData info = grid.getInfo();
        ChannelBuffer buffer = grid.getData();
        int width = info.getWidth();
        int height = info.getHeight();
        int offset = buffer.arrayOffset();

        Bitmap newMap = Bitmap.createBitmap(info.getWidth(), info.getHeight(), Bitmap.Config.ALPHA_8);

        int[] pixels;
        byte bytePixel;

        for (int y = 0; y < height; y++) {
            pixels = new int[info.getWidth()];

            for (int x = 0; x < width; x++) {
                // Pixels are ARGB packed ints.
                bytePixel = buffer.readByte();

                if (bytePixel == -1) {
                    pixels[x] = gradient[101];
                } else {
                    pixels[x] = gradient[bytePixel];
                }
            }

            newMap.setPixels(pixels, 0, width, 0, y, width, 1);
        }

        return newMap;
    }


    private static int[] getGradient() {
        int[] grad = new int[102];

        for (int i = 0; i <= 101; i++) {
            int color;

            if (i == 101) {
                color = Color.argb(128, 0, 0, 0);
            }else{
                color = Color.argb((int)(255/100f * (100 - i)), 0, 0, 0);
            }

            grad[i] = color;
        }

        return grad;
    }
}
