package com.schneewittchen.rosandroid.utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.09.20
 * @updated on
 * @modified by
 */
public  class TiledBitmap {

    private final Bitmap[][] mArray;    // array where chunks is stored
    private final int mWidth;           // original (full) width of source image
    private final int mHeight;          // original (full) height of source image
    private final int mChunkWidth;      // default width of a chunk
    private final int mChunkHeight;     // default height of a chunk

    public TiledBitmap(Bitmap src) {
        this(new Options(src, 100, 100));
    }

    public TiledBitmap(Options options) {
        mArray = divideBitmap(options);

        mWidth = options.source.getWidth();
        mHeight = options.source.getHeight();
        mChunkWidth = options.chunkWidth;
        mChunkHeight = options.chunkHeight;
    }


    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public Bitmap getChunk(int x, int y) {
        if (mArray.length < x && x > 0 && mArray[x].length < y && y > 0) {
            return mArray[x][y];
        }

        return null;
    }

    /**
     *  x, y are viewport coords on the image itself;
     *  w, h are viewport's width and height.
     */
    public void draw(Canvas canvas, int x, int y, int w, int h, Paint paint) {
        if (x >= getWidth() || y >= getHeight() || x + w <= 0 || y + h <= 0)
            return;

        int i1 = x / mChunkWidth;           // i1 and j1 are indices of visible chunk that is
        int j1 = y / mChunkHeight;          // on the top-left corner of the screen
        int i2 = (x + w) / mChunkWidth;     // i2 and j2 are indices of visible chunk that is
        int j2 = (y + h) / mChunkHeight;    // on the right-bottom corner of the screen

        i2 = i2 >= mArray.length ? mArray.length - 1 : i2;
        j2 = j2 >= mArray[i2].length ? mArray[i2].length - 1 : j2;

        int offsetX = x - i1 * mChunkWidth;
        int offsetY = y - j1 * mChunkHeight;

        for (int i = i1; i <= i2; i++) {
            for (int j = j1; j <= j2; j++) {
                canvas.drawBitmap(
                        mArray[i][j],
                        (i - i1) * mChunkWidth - offsetX,
                        (j - j1) * mChunkHeight - offsetY,
                        paint
                );
            }
        }
    }


    public static Bitmap[][] divideBitmap(Bitmap bitmap) {
        return divideBitmap(new Options(bitmap, 100, 100));
    }

    public static Bitmap[][] divideBitmap(Options options) {
        Bitmap[][] arr = new Bitmap[options.xCount][options.yCount];

        for (int x = 0; x < options.xCount; ++x) {
            for (int y = 0; y < options.yCount; ++y) {
                int w = Math.min(options.chunkWidth, options.source.getWidth() - (x * options.chunkWidth));
                int h = Math.min(options.chunkHeight, options.source.getHeight() - (y * options.chunkHeight));
                arr[x][y] = Bitmap.createBitmap(options.source, x * options.chunkWidth, y * options.chunkHeight, w, h);
            }
        }

        return arr;
    }


    public static final class Options {
        final int chunkWidth;
        final int chunkHeight;
        final int xCount;
        final int yCount;
        final Bitmap source;

        public Options(Bitmap src, int chunkW, int chunkH) {
            chunkWidth = chunkW;
            chunkHeight = chunkH;

            xCount = ((src.getWidth() - 1) / chunkW) + 1;
            yCount = ((src.getHeight() - 1) / chunkH) + 1;

            source = src;
        }

        public Options(int xc, int yc, Bitmap src) {
            xCount = xc;
            yCount = yc;

            chunkWidth = src.getWidth() / xCount;
            chunkHeight = src.getHeight() / yCount;

            source = src;
        }
    }
}