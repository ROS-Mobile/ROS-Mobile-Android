package com.schneewittchen.rosandroid.model.repositories.rosRepo;

import org.ros.rosjava_geometry.FrameTransformTree;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 20.05.21
 */
public class TransformProvider {

    private static TransformProvider instance;

    private FrameTransformTree frameTransformTree;


    public TransformProvider() {
        this.reset();
    }


    public static TransformProvider getInstance() {
        if (instance == null)  {
            instance = new TransformProvider();
        }

        return instance;
    }

    public FrameTransformTree getTree() {
        return frameTransformTree;
    }

    public void reset() {
        this.frameTransformTree = new FrameTransformTree();
    }
}
