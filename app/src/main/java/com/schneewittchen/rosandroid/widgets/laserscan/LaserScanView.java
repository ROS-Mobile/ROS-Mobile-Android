package com.schneewittchen.rosandroid.widgets.laserscan;

import android.content.Context;
import android.util.Log;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.Vertices;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberLayerView;

import org.ros.internal.message.Message;
import org.ros.namespace.GraphName;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.PoseStamped;
import nav_msgs.Path;
import sensor_msgs.LaserScan;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.05.21
 */
public class LaserScanView extends SubscriberLayerView {

    public static final String TAG = LaserScanView.class.getSimpleName();
    private static final ROSColor FREE_SPACE_COLOR = ROSColor.fromHexAndAlpha("377dfa", 0.1f);
    private static final ROSColor OCCUPIED_SPACE_COLOR = ROSColor.fromHexAndAlpha("377dfa", 0.6f);
    private static final float LASER_SCAN_POINT_SIZE = 10.f;
    private static final int LASER_SCAN_STRIDE = 15;

    private final Object mutex;
    private FloatBuffer vertexFrontBuffer;
    private FloatBuffer vertexBackBuffer;


    public LaserScanView(Context context) {
        super(context);
        mutex = new Object();
    }


    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);

        LaserScanEntity entity = (LaserScanEntity) widgetEntity;

    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (vertexFrontBuffer == null) return;

        synchronized (mutex) {
            Vertices.drawTriangleFan(gl, vertexFrontBuffer, FREE_SPACE_COLOR);
            FloatBuffer pointVertices = vertexFrontBuffer.duplicate();
            pointVertices.position(3);
            Vertices.drawPoints(gl, pointVertices, OCCUPIED_SPACE_COLOR, LASER_SCAN_POINT_SIZE);
        }
    }

    @Override
    public void onNewMessage(Message message) {
        LaserScan scan = (LaserScan) message;

        frame = GraphName.of(scan.getHeader().getFrameId());
        this.updateVertexBuffer2(scan);
    }

    private void updateVertexBuffer2(LaserScan laserScan) {
        int vertexCount = 0;
        float[] ranges = laserScan.getRanges();
        int size = (ranges.length+2) * 3;

        if (vertexBackBuffer == null || vertexBackBuffer.capacity() < size) {
            vertexBackBuffer = Vertices.allocateBuffer(size);
        }

        // Clear vertices and fill in first vertex
        vertexBackBuffer.clear();
        for (int i = 0; i < 3; i++) {
            vertexBackBuffer.put(0);
        }
        vertexCount++;

        float minimumRange = laserScan.getRangeMin();
        float maximumRange = laserScan.getRangeMax();
        float angle = laserScan.getAngleMin();
        float angleIncrement = laserScan.getAngleIncrement();

        // Calculate coordinates of laser range values
        for (int i = 0; i < ranges.length; i++) {
            float range = ranges[i];

            if (minimumRange < range && range < maximumRange) {
                vertexBackBuffer.put((float) (range * Math.cos(angle)));
                vertexBackBuffer.put((float) (range * Math.sin(angle)));
                vertexBackBuffer.put(0);
                vertexCount++;
            }

            angle += angleIncrement;
        }

        vertexBackBuffer.position(0);
        vertexBackBuffer.limit(vertexCount * 3);

        synchronized (mutex) {
            FloatBuffer tmp = vertexFrontBuffer;
            vertexFrontBuffer = vertexBackBuffer;
            vertexBackBuffer = tmp;
        }
    }

    private void updateVertexBuffer(LaserScan laserScan, int stride) {
        int vertexCount = 0;
        float[] ranges = laserScan.getRanges();
        int size = ((ranges.length / stride) + 2) * 3;

        if (vertexBackBuffer == null || vertexBackBuffer.capacity() < size) {
            vertexBackBuffer = Vertices.allocateBuffer(size);
        }

        // Clear vertices and fill in first vertex
        vertexBackBuffer.clear();
        for (int i = 0; i < 3; i++) {
            vertexBackBuffer.put(0);
        }
        vertexCount++;

        float minimumRange = laserScan.getRangeMin();
        float maximumRange = laserScan.getRangeMax();
        float angle = laserScan.getAngleMin();
        float angleIncrement = laserScan.getAngleIncrement();

        // Calculate coordinates of laser range values
        for (int i = 0; i < ranges.length; i += stride) {
            float range = ranges[i];

            if (minimumRange < range && range < maximumRange) {
                vertexBackBuffer.put((float) (range * Math.cos(angle)));
                vertexBackBuffer.put((float) (range * Math.sin(angle)));
                vertexBackBuffer.put(0);
                vertexCount++;
            }

            angle += angleIncrement * stride;
        }

        vertexBackBuffer.position(0);
        vertexBackBuffer.limit(vertexCount * 3);

        synchronized (mutex) {
            FloatBuffer tmp = vertexFrontBuffer;
            vertexFrontBuffer = vertexBackBuffer;
            vertexBackBuffer = tmp;
        }
    }
}