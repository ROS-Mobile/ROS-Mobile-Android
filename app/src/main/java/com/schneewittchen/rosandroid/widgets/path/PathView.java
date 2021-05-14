package com.schneewittchen.rosandroid.widgets.path;

import android.content.Context;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
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


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class PathView extends SubscriberLayerView {

    public static final String TAG = PathView.class.getSimpleName();

    private ROSColor lineColor;
    private float lineWidth;
    private int numPoints;
    private FloatBuffer vertexBuffer;


    public PathView(Context context) {
        super(context);
    }


    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);

        PathEntity entity = (PathEntity)widgetEntity;
        lineColor = ROSColor.fromHex(entity.lineColor);
        lineWidth = entity.lineWidth;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (numPoints < 2) return;

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        lineColor.apply(gl);
        gl.glLineWidth(lineWidth);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numPoints);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    @Override
    public void onNewMessage(Message message) {
        Path path = (Path)message;

        ByteBuffer pufferBuffer = ByteBuffer.allocateDirect(path.getPoses().size() * 3 * Float.SIZE);
        pufferBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = pufferBuffer.asFloatBuffer();
        int i = 0;

        if (path.getPoses().size() > 0) {
            frame = GraphName.of(path.getPoses().get(0).getHeader().getFrameId());

            for (PoseStamped pose : path.getPoses()) {
                vertexBuffer.put((float) pose.getPose().getPosition().getX());
                vertexBuffer.put((float) pose.getPose().getPosition().getY());
                vertexBuffer.put((float) pose.getPose().getPosition().getZ());
                i++;
            }
        }

        vertexBuffer.position(0);
        numPoints = i;
    }

}