package com.schneewittchen.rosandroid.ui.opengl.shape;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.Vertices;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import uk.co.blogspot.fractiousg.texample.GLText;


public class TextShape extends BaseShape {

    private final GLText glText;
    private final String text;

    private float x;
    private float y;
    private FloatBuffer lines;


    public TextShape(GLText glText, String text) {
        this.glText = glText;
        this.text = text;
        lines = Vertices.allocateBuffer(4 * 3);
    }


    public void setOffset(float x, float y) {
        this.x = x;
        this.y = y;
        lines.put(0.f);
        lines.put(0.f);
        lines.put(0.f);

        lines.put(x);
        lines.put(y);
        lines.put(0.f);

        lines.put(x);
        lines.put(y);
        lines.put(0.f);

        lines.put(x + glText.getLength(text));
        lines.put(y);
        lines.put(0.f);

        lines.flip();
    }

    @Override
    protected void scale(VisualizationView view, GL10 gl) {
        // Counter adjust for the camera zoom.
        gl.glScalef(1 / (float) view.getCamera().getZoom(), 1 / (float) view.getCamera().getZoom(),
                1.0f);
    }

    @Override
    protected void drawShape(VisualizationView view, GL10 gl) {
        Vertices.drawLines(gl, lines, getColor(), 3.f);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        glText.begin(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), getColor()
                .getAlpha());
        glText.draw(text, x, y);
        glText.end();
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
