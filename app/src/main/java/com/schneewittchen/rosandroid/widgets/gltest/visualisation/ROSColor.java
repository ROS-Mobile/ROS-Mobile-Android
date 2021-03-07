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

package com.schneewittchen.rosandroid.widgets.gltest.visualisation;

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

  public static ROSColor copyOf(ROSColor color) {
    return new ROSColor(color.red, color.green, color.blue, color.alpha);
  }

  public static ROSColor fromHexAndAlpha(String hex, float alpha) {
    Preconditions.checkArgument(hex.length() == 6);
    float red = Integer.parseInt(hex.substring(0, 2), 16) / 255.0f;
    float green = Integer.parseInt(hex.substring(2, 4), 16) / 255.0f;
    float blue = Integer.parseInt(hex.substring(4), 16) / 255.0f;
    return new ROSColor(red, green, blue, alpha);
  }

  public ROSColor(float red, float green, float blue, float alpha) {
    Preconditions.checkArgument(0.0f <= red && red <= 1.0f);
    Preconditions.checkArgument(0.0f <= green && green <= 1.0f);
    Preconditions.checkArgument(0.0f <= blue && blue <= 1.0f);
    Preconditions.checkArgument(0.0f <= alpha && alpha <= 1.0f);
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
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
}
