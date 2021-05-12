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

package com.schneewittchen.rosandroid.ui.opengl.shape;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;

/**
 * Represents the robot's current goal pose.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class GoalShape extends MetricSpacePoseShape {

    private static final ROSColor COLOR = ROSColor.fromHexAndAlpha("ff0000", 1f);

    public GoalShape() {
        super();
        setColor(COLOR);
    }
}
