package com.schneewittchen.rosandroid.widgets.camera;

import android.graphics.Bitmap;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 27.04.20
 * @updated on
 * @modified by
 */

public class CameraData extends BaseData {

    public Bitmap map;

    public CameraData(int height, int width, String encoding, byte bigEndian, int step, byte[] data) {
        // Get the starting point of the data
        int dataStart = data.length - (height*step);
        int pixelBytesNum = step/width;

        // Generating the desired integer array, TODO: Check encoding and bigEndian, right now we are using RGB8, but there are other ROS encoding types also
        int dataLength = height*width;
        int[] intArray = new int[dataLength];;
        for (int i=0; i < height; i++) {
            for (int j=0;  j < width; j++) {
                int A = 255;
                int R = data[dataStart + ((i)*step + (j*pixelBytesNum + 0))];
                int G = data[dataStart + ((i)*step + (j*pixelBytesNum + 1))];
                int B = data[dataStart + ((i)*step + (j*pixelBytesNum + 2))];
                int color = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                intArray[i*width + j] = color;
            }
        }
        map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
    }
}
