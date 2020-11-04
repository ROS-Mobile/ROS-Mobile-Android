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

package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

/**
 * TODO: Description
 *
 * @author Damon Kohler
 * @version 1.0.0
 * @created on 15.04.20
 * @updated on 15.04.20
 * @modified by Nico Studt
 */
public interface NodeMainExecutorServiceListener {

    /**
     * @param nodeMainExecutorService the {@link NodeMainExecutorService} that was shut down
     */
    void onShutdown(NodeMainExecutorService nodeMainExecutorService);
}
