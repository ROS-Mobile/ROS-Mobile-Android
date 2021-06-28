package com.schneewittchen.rosandroid.widgets.touchgoal;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.XYOrthographicCamera;

import org.ros.internal.message.Message;
import org.ros.namespace.GraphName;
import org.ros.node.topic.Publisher;
import org.ros.rosjava_geometry.Quaternion;
import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import geometry_msgs.Point;
import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 26.05.2021
 */
public class TouchGoalData extends BaseData {

    public static final String TAG = TouchGoalData.class.getSimpleName();

    public float startX, startY, endX, endY;
    public Transform start;
    public Transform end;
    public GraphName frame;
    private XYOrthographicCamera camera;


    public TouchGoalData(XYOrthographicCamera camera) {
        this.camera = camera;
        this.frame = camera.getFrame();
    }

    public void setStart(float x, float y) {
        startX = x;
        startY = y;
        start = camera.toFrame(x, y);
    }

    public void setEnd(float x, float y) {
        endX = x;
        endY = y;
        end = camera.toFrame(x, y);
    }

    public boolean isValid() {
        return start != null && end != null && frame != null;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        TouchGoalEntity entity = (TouchGoalEntity) widget;

        // Create message
        PoseStamped message = (PoseStamped) publisher.newMessage();
        message.getHeader().setFrameId(frame.toString());

        Pose pose = message.getPose();

        // Set position
        Point pos = pose.getPosition();
        start.getTranslation().toPointMessage(pos);

        // Set orientation
        Vector3 diff = end.getTranslation().add(start.getTranslation().invert());
        float angle = (float) Math.atan2(diff.getY(), diff.getX());
        Quaternion rotation = Quaternion.fromAxisAngle(new Vector3(0, 0, 1), angle);

        geometry_msgs.Quaternion poseRot = pose.getOrientation();
        poseRot.setW(rotation.getW());
        poseRot.setX(rotation.getX());
        poseRot.setY(rotation.getY());
        poseRot.setZ(rotation.getZ());

        return message;
    }
}
