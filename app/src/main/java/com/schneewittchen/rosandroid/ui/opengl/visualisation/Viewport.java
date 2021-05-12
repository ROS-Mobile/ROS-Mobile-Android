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

import javax.microedition.khronos.opengles.GL10;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class Viewport {

    private final int width;
    private final int height;


    public Viewport(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public void apply(GL10 gl) {
        gl.glViewport(0, 0, width, height);
        // Set the perspective projection to be orthographic.
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        // This corrects for the aspect ratio of the viewport. The viewport can now
        // be reasoned about in pixels. The zNear and zFar only need to be
        // sufficiently large to avoid clipping. The z-buffer is not otherwise used.
        gl.glOrthof(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1e4f, 1e4f);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
