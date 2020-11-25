package com.schneewittchen.rosandroid.widgets.costmap;

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
 * @version 1.0.0
 * @created on 14.09.2020
 * @updated
 * @modified
 */
public class CostMapData extends BaseData {

    public static final String TAG = CostMapData.class.getSimpleName();
    public static final int[] gradient = getGradient();

    public Bitmap map;


    public CostMapData(OccupancyGrid grid) {
        map = createGrayMap(grid);
    }


    private Bitmap createGrayMap(OccupancyGrid grid) {
        MapMetaData info = grid.getInfo();
        ChannelBuffer buffer = grid.getData();
        int width = info.getWidth();
        int height = info.getHeight();

        Bitmap newMap = Bitmap.createBitmap(info.getWidth(), info.getHeight(), Bitmap.Config.ARGB_8888);

        int[] pixels;
        byte bytePixel;

        for (int y = 0; y < height; y++) {
            pixels = new int[width];

            for (int x = 0; x < width; x++) {
                bytePixel = buffer.readByte();
                pixels[x] = gradient[bytePixel];
            }

            newMap.setPixels(pixels, 0, width, 0, y, width, 1);
        }

        return newMap;
    }


    private static int[] getGradient() {
        int[] grad = new int[101];

        for (int i = 0; i < 101; i++) {
            int color;

            if (i == 0) {
                color = Color.argb(0, 0, 0, 0);
            } else if (i < 50) {
                int a = (int) (255/50f * i);
                color = Color.argb(a, 0, 0, 255);
            } else {
                int a = (int) (255/50f * (i - 50));
                color = Color.argb(255, a, 0, 255-a);
            }

            grad[i] = color;
        }

        return grad;
    }
}
