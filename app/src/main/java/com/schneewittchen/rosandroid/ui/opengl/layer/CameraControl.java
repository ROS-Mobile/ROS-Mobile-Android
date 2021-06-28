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

package com.schneewittchen.rosandroid.ui.opengl.layer;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.core.view.GestureDetectorCompat;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.RotateGestureDetector;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;


/**
 * Provides gesture control of the camera for translate, rotate, and zoom.
 *
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 * @updated on 14.06.2021
 * @modified by Nico Studt
 */
public class CameraControl {

    private static final String TAG = CameraControl.class.getSimpleName();

    private final VisualizationView vizView;
    private GestureDetectorCompat translateGestureDetector;
    private RotateGestureDetector rotateGestureDetector;
    private ScaleGestureDetector zoomGestureDetector;


    public CameraControl(VisualizationView vizView) {
        this.vizView = vizView;
    }


    public void init(boolean translate, boolean rotate, boolean scale) {
        if(translate) {
            translateGestureDetector =
                    new GestureDetectorCompat(vizView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            // This must return true in order for onScroll() to trigger.
                            return true;
                        }

                        @Override
                        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                                final float distanceX, final float distanceY) {
                            vizView.getCamera().translate(-distanceX, distanceY);
                            return true;
                        }

                        @Override
                        public boolean onDoubleTap(final MotionEvent e) {
                            return true;
                        }
                    });
        }

        if(rotate) {
            rotateGestureDetector =
                    new RotateGestureDetector((event1, event2, deltaAngle) -> {
                        final float focusX = (event1.getX(0) + event1.getX(1)) / 2;
                        final float focusY = (event1.getY(0) + event1.getY(1)) / 2;
                        vizView.getCamera().rotate(focusX, focusY, deltaAngle);
                        return true;
                    });
        }

        if(scale) {
            zoomGestureDetector =
                    new ScaleGestureDetector(vizView.getContext(),
                            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                                @Override
                                public boolean onScale(ScaleGestureDetector detector) {
                                    if (!detector.isInProgress()) {
                                        return false;
                                    }
                                    final float focusX = detector.getFocusX();
                                    final float focusY = detector.getFocusY();
                                    final float factor = detector.getScaleFactor();
                                    vizView.getCamera().zoom(focusX, focusY, factor);
                                    return true;
                                }
                            });
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;

        if (translateGestureDetector != null)
            handled = handled || translateGestureDetector.onTouchEvent(event);

        if (rotateGestureDetector != null)
            handled = handled || rotateGestureDetector.onTouchEvent(event);

        if (zoomGestureDetector != null)
            handled = handled || zoomGestureDetector.onTouchEvent(event);

        return handled;
    }
}