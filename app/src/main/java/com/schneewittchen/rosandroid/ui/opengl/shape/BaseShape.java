package com.schneewittchen.rosandroid.ui.opengl.shape;

import com.google.common.base.Preconditions;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.OpenGlTransform;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import org.ros.rosjava_geometry.Transform;

import javax.microedition.khronos.opengles.GL10;

/**
 * Defines the getters and setters that are required for all {@link Shape}
 * implementors.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
abstract class BaseShape implements Shape {

    private ROSColor color;
    private Transform transform;

    public BaseShape() {
        setTransform(Transform.identity());
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        gl.glPushMatrix();
        OpenGlTransform.apply(gl, getTransform());
        scale(view, gl);
        drawShape(view, gl);
        gl.glPopMatrix();
    }

    /**
     * To be implemented by children. Draws the shape after the shape's
     * transform and scaling have been applied.
     */
    abstract protected void drawShape(VisualizationView view, GL10 gl);

    /**
     * Scales the coordinate system.
     * <p>
     * This is called after transforming the surface according to
     * {@link #transform}.
     */
    protected void scale(VisualizationView view, GL10 gl) {
        // The default scale is in metric space.
    }

    @Override
    public ROSColor getColor() {
        Preconditions.checkNotNull(color);
        return color;
    }

    @Override
    public void setColor(ROSColor color) {
        this.color = color;
    }

    @Override
    public Transform getTransform() {
        Preconditions.checkNotNull(transform);
        return transform;
    }

    @Override
    public void setTransform(Transform pose) {
        this.transform = pose;
    }
}
