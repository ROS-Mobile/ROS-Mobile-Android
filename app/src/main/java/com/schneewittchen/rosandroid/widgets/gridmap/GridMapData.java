package com.schneewittchen.rosandroid.widgets.gridmap;

import android.graphics.Bitmap;
import com.schneewittchen.rosandroid.widgets.base.BaseData;


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

        // TODO: Define size in the Details
        this.map = getBitMap(-10,-10,20,20,data,dataStart);
    }

    private Bitmap getBitMap(float x0, float y0, float width, float height, byte[] data, int dataStart) {
        // Start with defining the integer starting point and integer width and height
        int x0Idx = (int) ((x0 - this.x0) / this.res);
        int y0Idx = (int) ((y0 - this.y0) / this.res);
        int widthIdx = (int) (width / this.res);
        int heightIdx = (int) (height / this.res);

        // Generating the desired integer array, TODO: Check if height, width are correctly set
        int dataLength = widthIdx * heightIdx;
        int[] intArray = new int[dataLength];;
        for (int i=0; i < widthIdx; i++) {
            for (int j=0;  j < heightIdx; j++) {
                intArray[i*heightIdx + j] = getColor(data[dataStart + ((i+x0Idx)*this.dataHeight + (j+y0Idx))]);
            }
        }
        return Bitmap.createBitmap(intArray, widthIdx, heightIdx, Bitmap.Config.ARGB_8888);
    }

    // TODO: generate own color class
    private int getColor(byte data) {
        int A = 255; int R = 255; int G = 255; int B = 255;
        if (data == -1) {
            A = 127; B = 0; G = 0;
        } else {
            // Define gray scale map
            B = 255 - (255 * (data/100));
            G = B; R = B;
        }
        return ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
    }
}
