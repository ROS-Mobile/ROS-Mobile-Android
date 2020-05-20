package com.schneewittchen.rosandroid.widgets.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

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
        int iStep, iWidth, dataStep, iColor;
        long lColor;
        // Storage capacities
        int[] intArray = new int[height * width];
        long[] longArray = new long[height * width];
        switch (image.getEncoding()) {
            case "rgb8":
                for (int i = 0; i < height; i++) {
                    iStep = i * step;
                    iWidth = i * width;
                    for (int j = 0; j < width; j++) {
                        dataStep = dataStart + iStep + j*pixelBytesNum;
                        int R = data[dataStep];
                        int G = data[dataStep+1];
                        int B = data[dataStep+2];
                        int A = 255;
                        iColor = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[iWidth + j] = iColor;
                    }
                }
                this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
                break;
            case "rgba8":
                for (int i = 0; i < height; i++) {
                    iStep = i * step;
                    iWidth = i * width;
                    for (int j = 0; j < width; j++) {
                        dataStep = dataStart + iStep + j*pixelBytesNum;
                        int R = data[dataStep];
                        int G = data[dataStep+1];
                        int B = data[dataStep+2];
                        int A = data[dataStep+3];
                        iColor = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[iWidth + j] = iColor;
                    }
                }
                this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
                break;
            case "rgb16":
                if(image.getIsBigendian() == 0) {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long R = data[dataStep] | data[dataStep+1] << 8;
                            long G = data[dataStep+2] | data[dataStep+3] << 8;
                            long B = data[dataStep+4] | data[dataStep+5] << 8;
                            long A = 65535;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long R = data[dataStep+1] | data[dataStep] << 8;
                            long G = data[dataStep+3] | data[dataStep+2] << 8;
                            long B = data[dataStep+5] | data[dataStep+4] << 8;
                            long A = 65535;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGBA_F16);
                }
                break;
            case "rgba16":
                if(image.getIsBigendian() == 0) {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long R = data[dataStep] | data[dataStep+1] << 8;
                            long G = data[dataStep+2] | data[dataStep+3] << 8;
                            long B = data[dataStep+4] | data[dataStep+5] << 8;
                            long A = data[dataStep+6] | data[dataStep+7] << 8;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long R = data[dataStep+1] | data[dataStep] << 8;
                            long G = data[dataStep+3] | data[dataStep+2] << 8;
                            long B = data[dataStep+5] | data[dataStep+4] << 8;
                            long A = data[dataStep+7] | data[dataStep+6] << 8;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGBA_F16);
                }
                break;
            case "bgr8":
                for (int i = 0; i < height; i++) {
                    iStep = i * step;
                    iWidth = i * width;
                    for (int j = 0; j < width; j++) {
                        dataStep = dataStart + iStep + j*pixelBytesNum;
                        int B = data[dataStep];
                        int G = data[dataStep+1];
                        int R = data[dataStep+2];
                        int A = 255;
                        iColor = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[iWidth + j] = iColor;
                    }
                }
                this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
                break;
            case "bgra8":
                for (int i = 0; i < height; i++) {
                    iStep = i * step;
                    iWidth = i * width;
                    for (int j = 0; j < width; j++) {
                        dataStep = dataStart + iStep + j*pixelBytesNum;
                        int B = data[dataStep];
                        int G = data[dataStep+1];
                        int R = data[dataStep+2];
                        int A = data[dataStep+3];
                        iColor = ((A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff));
                        intArray[iWidth + j] = iColor;
                    }
                }
                this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
                break;
            case "bgr16":
                if(image.getIsBigendian() == 0) {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long B = data[dataStep] | data[dataStep+1] << 8;
                            long G = data[dataStep+2] | data[dataStep+3] << 8;
                            long R = data[dataStep+4] | data[dataStep+5] << 8;
                            long A = 65535;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long B = data[dataStep+1] | data[dataStep] << 8;
                            long G = data[dataStep+3] | data[dataStep+2] << 8;
                            long R = data[dataStep+5] | data[dataStep+4] << 8;
                            long A = 65535;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGBA_F16);
                }
                break;
            case "bgra16":
                if(image.getIsBigendian() == 0) {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long B = data[dataStep] | data[dataStep+1] << 8;
                            long G = data[dataStep+2] | data[dataStep+3] << 8;
                            long R = data[dataStep+4] | data[dataStep+5] << 8;
                            long A = data[dataStep+6] | data[dataStep+7] << 8;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long B = data[dataStep+1] | data[dataStep] << 8;
                            long G = data[dataStep+3] | data[dataStep+2] << 8;
                            long R = data[dataStep+5] | data[dataStep+4] << 8;
                            long A = data[dataStep+7] | data[dataStep+6] << 8;
                            lColor = ((R & 0xffff) << 48 | (G & 0xffff) << 32 | (B & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGBA_F16);
                }
                break;
            case "mono8":
                for (int i = 0; i < height; i++) {
                    iStep = i * step;
                    iWidth = i * width;
                    for (int j = 0; j < width; j++) {
                        dataStep = dataStart + iStep + j*pixelBytesNum;
                        int mono = data[dataStep];
                        int A = 255;
                        iColor = ((A & 0xff) << 24 | (mono & 0xff) << 16 | (mono & 0xff) << 8 | (mono & 0xff));
                        intArray[iWidth + j] = iColor;
                    }
                }
                this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.ARGB_8888);
                break;
            case "mono16":
                if(image.getIsBigendian() == 0) {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long mono = data[dataStep] | data[dataStep+1] << 8;
                            long A = 65535;
                            lColor = ((mono & 0xffff) << 48 | (mono & 0xffff) << 32 | (mono & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                } else {
                    for (int i = 0; i < height; i++) {
                        iStep = i * step;
                        iWidth = i * width;
                        for (int j = 0; j < width; j++) {
                            dataStep = dataStart + iStep + j*pixelBytesNum;
                            long mono = data[dataStep+1] | data[dataStep] << 8;
                            long A = 65535;
                            lColor = ((mono & 0xffff) << 48 | (mono & 0xffff) << 32 | (mono & 0xffff) << 16 | (A & 0xffff));
                            longArray[iWidth + j] = lColor;
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.map = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGBA_F16);
                }
                break;
            default:
                Log.i(TAG, "No compatible encoding!");
        }
    }
}
