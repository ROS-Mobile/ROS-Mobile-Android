package com.schneewittchen.rosandroid.ui.opengl.shape;

import android.graphics.Typeface;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import javax.microedition.khronos.opengles.GL10;

import uk.co.blogspot.fractiousg.texample.GLText;


public class TextShapeFactory {

    private final GLText glText;


    public TextShapeFactory(final VisualizationView view, final GL10 gl) {
        glText = new GLText(gl, view.getContext().getAssets());
    }

    public void loadFont(final Typeface typeface, final int size, final int padX, final int padY) {
        glText.load(typeface, size, padX, padY);
    }

    public void loadFont(final String file, final int size, final int padX, final int padY) {
        glText.load(file, size, padX, padY);
    }

    public TextShape newTextShape(final String text) {
        return new TextShape(glText, text);
    }
}
