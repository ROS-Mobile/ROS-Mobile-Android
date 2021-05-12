/*
 * Copyright (C) 2012 Google Inc.
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


import android.view.MotionEvent;

import org.ros.math.RosMath;


/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RotateGestureDetector {


    public interface OnRotateGestureListener {
        boolean onRotate(MotionEvent event1, MotionEvent event2, double deltaAngle);
    }


    private static final double MAX_DELTA_ANGLE = 1e-1;

    private final OnRotateGestureListener listener;

    private MotionEvent previousMotionEvent;


    public RotateGestureDetector(OnRotateGestureListener listener) {
        this.listener = listener;
    }


    private double angle(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        return Math.atan2(deltaY, deltaX);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            return false;
        }

        if (previousMotionEvent == null) {
            previousMotionEvent = MotionEvent.obtain(event);
            return false;
        }

        double deltaAngle =
                RosMath.clamp(angle(previousMotionEvent) - angle(event), -MAX_DELTA_ANGLE, MAX_DELTA_ANGLE);
        boolean result = listener.onRotate(previousMotionEvent, event, deltaAngle);

        previousMotionEvent.recycle();
        previousMotionEvent = MotionEvent.obtain(event);

        return result;
    }
}
