package com.schneewittchen.rosandroid.widgets.touchgoal;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.OpenGlTransform;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherLayerView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 26.05.2021
 */
public class TouchGoalView extends PublisherLayerView {

    public static final String TAG = TouchGoalView.class.getSimpleName();

    private enum State {NORMAL, DOUBLETAPPED};
    private State state = State.NORMAL;
    private TouchGoalData data;
    private GestureDetectorCompat detector;
    private FloatBuffer circleBuffer;
    private FloatBuffer lineBuffer;
    private int numPoints = 51;
    private ROSColor color;


    public TouchGoalView(Context context) {
        super(context);
        detector = new GestureDetectorCompat(context, new DoubleTapListener());
        this.initCircle();
        this.color = ROSColor.fromInt(context.getResources().getColor(R.color.colorPrimary));
    }

    private void initCircle() {
        ByteBuffer pufferBuffer = ByteBuffer.allocateDirect(numPoints * 3 * Float.SIZE);
        pufferBuffer.order(ByteOrder.nativeOrder());
        circleBuffer = pufferBuffer.asFloatBuffer();

        float angDiff = (float) (Math.PI * 2 / (numPoints-1));

        for (int i = 0; i < numPoints; i++) {
            float angle = angDiff * i;
            circleBuffer.put((float) Math.cos(angle));
            circleBuffer.put((float) Math.sin(angle));
            circleBuffer.put(0);
        }

        circleBuffer.position(0);
    }

    private void initLine() {
        ByteBuffer pufferBuffer = ByteBuffer.allocateDirect(2 * 3 * Float.SIZE);
        pufferBuffer.order(ByteOrder.nativeOrder());
        lineBuffer = pufferBuffer.asFloatBuffer();

        lineBuffer.put((float) data.start.getTranslation().getX());
        lineBuffer.put((float) data.start.getTranslation().getY());
        lineBuffer.put(0);
        lineBuffer.put((float) data.end.getTranslation().getX());
        lineBuffer.put((float) data.end.getTranslation().getY());
        lineBuffer.put(0);

        lineBuffer.position(0);
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (state != State.DOUBLETAPPED)
            return;

        gl.glPushMatrix();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        color.apply(gl);
        gl.glLineWidth(10);

        // Draw line from goal start to end
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);

        // Adjust frame
        OpenGlTransform.apply(gl, data.start);
        gl.glScalef(50, 50, 1.f);
        // Counter adjust for the camera zoom.
        float counterZoom = 1 / (float) view.getCamera().getZoom();
        gl.glScalef(counterZoom,counterZoom,1);

        // Draw circle around goal start
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, circleBuffer);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numPoints);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
    }

    @Override
    public boolean onTouchEvent(VisualizationView visualizationView, MotionEvent event) {
        this.detector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (state == State.DOUBLETAPPED) {
                data.setEnd(event.getX(), event.getY());
                initLine();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (state == State.DOUBLETAPPED) {
                if (data.isValid())
                    publishViewData(data);
            }

            state = State.NORMAL;
        }

        return state == State.DOUBLETAPPED;
    }

    class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            state = State.DOUBLETAPPED;

            data = new TouchGoalData(parentView.getCamera());
            data.setStart(e.getX(), e.getY());
            data.setEnd(e.getX(), e.getY());

            initLine();

            return true;
        }

    }

}