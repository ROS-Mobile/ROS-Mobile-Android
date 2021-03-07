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

import org.ros.namespace.GraphName;

/**
 * Interface for layers that are positioned by using Tf.
 * 
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public interface TfLayer {

  /**
   * @return the {@link Layer}'s reference frame
   */
  GraphName getFrame();
}
