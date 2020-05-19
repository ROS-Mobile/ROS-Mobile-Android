package com.schneewittchen.rosandroid.widgets.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.schneewittchen.rosandroid.widgets.base.BaseData;

import org.jboss.netty.buffer.ChannelBuffer;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 27.04.20
 * @updated on 13.05.20
 * @modified by Nico Studt
 */

public class CameraData extends BaseData {

    public static final String TAG = "CameraData";

    public Bitmap map;

    public CameraData(CompressedImage image) {
        ChannelBuffer buffer = image.getData();
        this.map = BitmapFactory.decodeByteArray(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
    }

    public CameraData(Image image) {
        // Get the data
        byte[] data = image.getData().array();
        int height = image.getHeight();
        int width = image.getWidth();
        int step = image.getStep();
        // Get the starting point of the data
        int dataStart = data.length - (height * step);
        int pixelBytesNum = step / width;
        // Encode Byte and transform to image
        int[] intArray = new int[height * width];
        switch (image.getEncoding()) {
            case "rgb8":
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int A = 255;
                        int R = data[dataStart + (i * step + (j * pixelBytesNum))];
                        int G = data[dataStart + (i * step + (j * pixelBytesNum + 1))];
                        int B = data[dataStart + (i * step + (j * pixelBytesNum + 2))];
                        int color = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[i * width + j] = color;
                    }
                }
                break;
            case "rgba8":
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int R = data[dataStart + (i * step + (j * pixelBytesNum))];
                        int G = data[dataStart + (i * step + (j * pixelBytesNum + 1))];
                        int B = data[dataStart + (i * step + (j * pixelBytesNum + 2))];
                        int A = data[dataStart + (i * step + (j * pixelBytesNum + 3))];
                        int color = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[i * width + j] = color;
                    }
                }
            case "rgb16":
                if(image.getIsBigendian() == 0) {
                } else {}
            case "rgba16":
                if(image.getIsBigendian() == 0) {
                } else {}
            case "bgr8":
            case "bgra8":
            case "bgr16":
                if(image.getIsBigendian() == 0) {
                } else {}
            case "bgra16":
                if(image.getIsBigendian() == 0) {
                } else {}
            case "mono8":
            case "mono16":
                if(image.getIsBigendian() == 0) {
                } else {}

            default:
                Log.i(TAG, "No compatible encoding!");
        }
        // Create the Bitmap for displaying the image
        this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
    }
}
