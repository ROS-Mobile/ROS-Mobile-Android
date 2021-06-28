/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.schneewittchen.rosandroid.ui.opengl.visualisation;

import com.google.common.base.Preconditions;

import javax.microedition.khronos.opengles.GL10;

/**
 * Defines a color based on RGBA values in the range [0, 1].
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class ROSColor {

    private float red;
    private float green;
    private float blue;
    private float alpha;


    public ROSColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }


    public ROSColor interpolate(ROSColor other, float fraction) {
        return new ROSColor(
                (other.red - red) * fraction + red,
                (other.green - green) * fraction + green,
                (other.blue - blue) * fraction + blue,
                (other.alpha - alpha) * fraction + alpha
        );
    }

    public int toInt() {
        int A = (int)(255 * alpha);
        int R = (int)(255 * red);
        int G = (int)(255 * green);
        int B = (int)(255 * blue);

        A = (A << 24) & 0xFF000000;
        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return A | R | G | B;
    }

    @Override
    public String toString() {
        return "Color = R:" + red + " B:" + blue + " G:" + green + " A:" + alpha;
    }

    public void apply(GL10 gl) {
        gl.glColor4f(red, green, blue, alpha);
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    public static ROSColor copyOf(ROSColor color) {
        return new ROSColor(color.red, color.green, color.blue, color.alpha);
    }

    public static ROSColor fromInt(int color) {
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        return new ROSColor(r, g, b, a);
    }

    public static ROSColor fromHex(String hex) {
        int length = hex.length();

        if (length == 6) {
            float red = Integer.parseInt(hex.substring(0, 2), 16) / 255.0f;
            float green = Integer.parseInt(hex.substring(2, 4), 16) / 255.0f;
            float blue = Integer.parseInt(hex.substring(4), 16) / 255.0f;
            return new ROSColor(red, green, blue, 1);

        } else if (length == 8) {
            float alpha = Integer.parseInt(hex.substring(0, 2), 16) / 255.0f;
            float red = Integer.parseInt(hex.substring(2, 4), 16) / 255.0f;
            float green = Integer.parseInt(hex.substring(4, 6), 16) / 255.0f;
            float blue = Integer.parseInt(hex.substring(6), 16) / 255.0f;
            return new ROSColor(red, green, blue, alpha);
        } else {
            return new ROSColor(0, 0, 0, 0);
        }
    }

    public static ROSColor fromHexAndAlpha(String hex, float alpha) {
        Preconditions.checkArgument(hex.length() == 6);
        float red = Integer.parseInt(hex.substring(0, 2), 16) / 255.0f;
        float green = Integer.parseInt(hex.substring(2, 4), 16) / 255.0f;
        float blue = Integer.parseInt(hex.substring(4), 16) / 255.0f;
        return new ROSColor(red, green, blue, alpha);
    }
}
