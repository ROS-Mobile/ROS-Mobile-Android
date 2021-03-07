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

package com.schneewittchen.rosandroid.widgets.gltest.layer;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.core.view.GestureDetectorCompat;

import com.google.common.base.Preconditions;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.RotateGestureDetector;

import org.ros.android.view.visualization.VisualizationView;
import org.ros.concurrent.ListenerGroup;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMainExecutor;

/**
 * Provides gesture control of the camera for translate, rotate, and zoom.
 *
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class CameraControlLayer extends DefaultLayer {


    private ListenerGroup<CameraControlListener> listeners;
    private GestureDetectorCompat translateGestureDetector;
    private RotateGestureDetector rotateGestureDetector;
    private ScaleGestureDetector zoomGestureDetector;

    @Override
    public void init(NodeMainExecutor nodeMainExecutor) {
        listeners =
                new ListenerGroup<>(nodeMainExecutor.getScheduledExecutorService());
    }

    public void addListener(CameraControlListener listener) {
        Preconditions.checkNotNull(listeners);
        listeners.add(listener);
    }

    @Override
    public boolean onTouchEvent(VisualizationView view, MotionEvent event) {
        if (translateGestureDetector == null || rotateGestureDetector == null
                || zoomGestureDetector == null) {
            return false;
        }
        final boolean translateGestureHandled = translateGestureDetector.onTouchEvent(event);
        final boolean rotateGestureHandled = rotateGestureDetector.onTouchEvent(event);
        final boolean zoomGestureHandled = zoomGestureDetector.onTouchEvent(event);
        return translateGestureHandled || rotateGestureHandled || zoomGestureHandled
                || super.onTouchEvent(view, event);
    }

    @Override
    public void onStart(final VisualizationView view, ConnectedNode connectedNode) {
        view.post(() -> {

            translateGestureDetector =
                    new GestureDetectorCompat(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            // This must return true in order for onScroll() to trigger.
                            return true;
                        }

                        @Override
                        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                                final float distanceX, final float distanceY) {
                            view.getCamera().translate(-distanceX, distanceY);
                            listeners.signal(listener -> listener.onTranslate(-distanceX, distanceY));
                            return true;
                        }

                        @Override
                        public boolean onDoubleTap(final MotionEvent e) {
                            listeners.signal(listener -> listener.onDoubleTap(e.getX(), e.getY()));
                            return true;
                        }
                    });

            rotateGestureDetector =
                    new RotateGestureDetector((event1, event2, deltaAngle) -> {
                        final float focusX = (event1.getX(0) + event1.getX(1)) / 2;
                        final float focusY = (event1.getY(0) + event1.getY(1)) / 2;
                        view.getCamera().rotate(focusX, focusY, deltaAngle);
                        listeners.signal(listener -> listener.onRotate(focusX, focusY, deltaAngle));
                        return true;
                    });

            zoomGestureDetector =
                    new ScaleGestureDetector(view.getContext(),
                            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                                @Override
                                public boolean onScale(ScaleGestureDetector detector) {
                                    if (!detector.isInProgress()) {
                                        return false;
                                    }
                                    final float focusX = detector.getFocusX();
                                    final float focusY = detector.getFocusY();
                                    final float factor = detector.getScaleFactor();
                                    view.getCamera().zoom(focusX, focusY, factor);
                                    listeners.signal(listener -> listener.onZoom(focusX, focusY, factor));
                                    return true;
                                }
                            });
        });
    }
}