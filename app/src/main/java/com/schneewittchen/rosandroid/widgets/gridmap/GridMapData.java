package com.schneewittchen.rosandroid.widgets.gridmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

import java.nio.ByteBuffer;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 20.04.20
 * @updated on 20.04.20
 * @modified by
 */
public class GridMapData extends BaseData {

    public Bitmap map;

    public float res;
    public float x0;
    public float y0;

    public int dataWidth;
    public int dataHeight;

    public GridMapData(int width, int height, byte[] data, float res, float x0, float y0) {
        this.res = res;
        this.x0 = x0;
        this.y0 = y0;

        this.dataWidth = width;
        this.dataHeight = height;

        int dataLength = width*height;
        int dataStart = data.length - dataLength;

        this.map = getBitMap(-10,-10,20,20,data,dataStart);
    }

    private Bitmap getBitMap(float x0, float y0, float width, float height, byte[] data, int dataStart) {
        // Start with defining the integer starting point nad integer width and height
        int x0Idx = (int) ((x0 - this.x0) / this.res);
        int y0Idx = (int) ((y0 - this.y0) / this.res);
        int widthIdx = (int) (width / this.res);
        int heightIdx = (int) (height / this.res);

        // Generating the desired integer array
        int dataLength = widthIdx * heightIdx;
        int[] intArray = new int[dataLength];;
        for (int i=0; i < widthIdx; i++) {
            for (int j=0;  j < heightIdx; j++) {
                intArray[i*widthIdx + j] = data[(i+x0Idx)*this.dataWidth + (j+y0Idx)];
            }
        }
        return Bitmap.createBitmap(intArray, widthIdx, heightIdx, Bitmap.Config.ARGB_8888);
    }
}
