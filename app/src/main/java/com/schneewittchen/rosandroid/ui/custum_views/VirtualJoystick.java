package com.schneewittchen.rosandroid.ui.custum_views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ros.android.android_core_components.R;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import java.util.Timer;
import java.util.TimerTask;

import nav_msgs.Odometry;

/**
 * VirtualJoystickView creates a virtual joystick view that publishes velocity
 * as (geometry_msgs.Twist) messages. The current version contains the following
 * features: snap to axes, turn in place, and resume previous velocity.
 *
 * @author munjaldesai@google.com (Munjal Desai)
 */
public class VirtualJoystick extends RelativeLayout implements Animation.AnimationListener,
        MessageListener<Odometry>, NodeMain {

    /**
     * BOX_TO_CIRCLE_RATIO The dimensions of the square box that contains the
     * circle, rings, etc are 300x300. The circles, rings, etc have a diameter of
     * 220. The ratio of the box to the circles is 300/220 = 1.363636. This ratio
     * stays the same regardless of the size of the virtual joystick.
     */
    private static final float BOX_TO_CIRCLE_RATIO = 1.363636f;
    /**
     * MAGNET_THETA The number of degrees before and after the major axes (0, 90,
     * 180, and 270) where the orientation is automatically adjusted to 0, 90,
     * 180, or 270. For example, if the contactTheta is 85 degrees and
     * MAGNET_THETA is 10, the contactTheta will be changed to 90.
     */
    private float magnetTheta = 10.0f;
    /**
     * ORIENTATION_TACK_FADE_RANGE The range in degrees around the current
     * orientation where the {@link #orientationWidget}s will be visible.
     */
    private static final float ORIENTATION_TACK_FADE_RANGE = 40.0f;
    /**
     * TURN_IN_PLACE_CONFIRMATION_DELAY Time (in milliseconds) to wait before
     * visually changing to turn-in-place mode.
     */
    private static final long TURN_IN_PLACE_CONFIRMATION_DELAY = 200L;
    /**
     * FLOAT_EPSILON Used for comparing float values.
     */
    private static final float FLOAT_EPSILON = 0.001f;
    /**
     * THUMB_DIVET_RADIUS The radius of the {@link #thumbDivet}. This is also the
     * distance threshold around the {@link #contactUpLocation} that triggers the
     * previous-velocity-mode {@link #previousVelocityMode}. This radius is also
     * the same for the {@link #lastVelocityDivet}.
     */
    private static final float THUMB_DIVET_RADIUS = 16.5f;
    /**
     * POST_LOCK_MAGNET_THETA Replaces {@link #magnetTheta} once the contact has
     * been magnetized/snapped around 90/270.
     */
    private static final float POST_LOCK_MAGNET_THETA = 20.0f;
    private static final int INVALID_POINTER_ID = -1;
    private Publisher<geometry_msgs.Twist> publisher;
    /**
     * mainLayout The parent layout that contains all the elements of the virtual
     * joystick.
     */
    private RelativeLayout mainLayout;
    /**
     * intensity The intensity circle that is used to show the current magnitude.
     */
    private ImageView intensity;
    /**
     * thumbDivet The divet that is underneath the user's thumb. When there is no
     * contact it moves to the center (over the center divet). An arrow inside it
     * is used to indicate the orientation.
     */
    private ImageView thumbDivet;
    /**
     * lastVelocityDivet The divet that shows the location of last contact. If a
     * contact is placed within ({@link #THUMB_DIVET_RADIUS}) to this, the
     * {@link #previousVelocityMode} is triggered.
     */
    private ImageView lastVelocityDivet;
    /**
     * orientationWidget 4 long tacks on the major axes and 20 small tacks off of
     * the major axes at 15 degree increments. These fade in and fade out to
     * collectively indicate the current orientation.
     */
    private ImageView[] orientationWidget;
    /**
     * magnitudeIndicator Shows the current linear velocity as a percent value.
     * This TextView will be on the opposite side of the contact to ensure that is
     * it visible most of the time. The font size and distance from the center of
     * the widget are automatically computed based on the size of parent
     * container.
     */
    private TextView magnitudeText;
    /** contactTheta The current orientation of the virtual joystick in degrees. */
    private float contactTheta;
    /**
     * normalizedMagnitude This is the distance between the center divet and the
     * point of contact normalized between 0 and 1. The linear velocity is based
     * on this.
     */
    private float normalizedMagnitude;
    /**
     * contactRadius This is the distance between the center of the widget and the
     * point of contact normalized between 0 and 1. This is mostly used for
     * animation/display calculations.
     *
     * TODO(munjaldesai): Omnigraffle this for better documentation.
     */
    private float contactRadius;
    /**
     * deadZoneRatio ...
     *
     * TODO(munjaldesai): Write a simple explanation for this. Currently not easy
     * to immediately comprehend it's meaning.
     *
     * TODO(munjaldesai): Omnigraffle this for better documentation.
     */
    private float deadZoneRatio = Float.NaN;
    /**
     * joystickRadius The center coordinates of the parent layout holding all the
     * elements of the virtual joystick. The coordinates are relative to the
     * immediate parent (mainLayout). Since the parent must be a square centerX =
     * centerY = radius.
     */
    private float joystickRadius = Float.NaN;
    /**
     * parentSize The length (width==height ideally) of a side of the parent
     * container that holds the virtual joystick.
     */
    private float parentSize = Float.NaN;
    /**
     * normalizingMultiplier Used to convert any distance from pixels to a
     * normalized value between 0 and 1. 0 is the center of widget and 1 is the
     * normalized distance to the  from the center of the
     * widget.
     */
    private float normalizingMultiplier;
    /**
     * currentRotationRange The (15 degree) green slice/arc used to indicate
     * turn-in-place behavior.
     */
    private ImageView currentRotationRange;
    /**
     * previousRotationRange The (30 degree) green slice/arc used to indicate
     * turn-in-place behavior.
     */
    private ImageView previousRotationRange;
    /**
     * turnInPlaceMode True when the virtual joystick is in turn-in-place mode.
     * False otherwise.
     */
    private volatile boolean turnInPlaceMode;
    /**
     * turnInPlaceStartTheta The orientation of the robot when the turn-in-place
     * mode is initiated.
     */
    private float turnInPlaceStartTheta = Float.NaN;
    /**
     * rightTurnOffset The rotation offset in degrees applied to
     * {@link #currentRotationRange} and {@link #previousRotationRange} when the
     * robot is turning clockwise (right) in turn-in-place mode.
     */
    private float rightTurnOffset;
    /**
     * currentOrientation The orientation of the robot in degrees.
     */
    private volatile float currentOrientation;
    /**
     * pointerId Used to keep track of the contact that initiated the interaction
     * with the virtual joystick. All other contacts are ignored.
     */
    private int pointerId = INVALID_POINTER_ID;
    /**
     * contactUpLocation The location of the primary contact when it is lifted off
     * of the screen.
     */
    private Point contactUpLocation;
    /**
     * previousVelocityMode True when the new contact position is within
     */
    private boolean previousVelocityMode;
    /**
     * magnetizedXAxis True when the contact has been snapped to the x-axis, false
     * otherwise.
     */
    private boolean magnetizedXAxis;
    /**
     * {@code true} if the joystick should publish linear velocities along the Y
     * axis instead of angular velocities along the Z axis, {@code false}
     * otherwise.
     */
    private boolean holonomic;
    /**
     * Velocity commands are published when this is true. Not published otherwise.
     * This is to prevent spamming velocity commands.
     */
    private volatile boolean publishVelocity;
    /**
     * Used to publish velocity commands at a specific rate.
     */
    private Timer publisherTimer;
    private geometry_msgs.Twist currentVelocityCommand;
    private String topicName;

    public VirtualJoystick(Context context) {
        super(context);
        initVirtualJoystick(context);
        topicName = "~cmd_vel";
    }

    public VirtualJoystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVirtualJoystick(context);
        topicName = "~cmd_vel";
    }

    public VirtualJoystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        topicName = "~cmd_vel";
    }

    /**
     * @param enabled
     *          {@code true} if this joystick should publish linear velocities
     *          along the Y axis instead of angular velocities along the Z axis,
     *          {@code false} otherwise
     */
    public void setHolonomic(boolean enabled) {
        holonomic = enabled;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        contactRadius = 0f;
        normalizedMagnitude = 0f;
        updateMagnitudeText();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onNewMessage(final nav_msgs.Odometry message) {
        double heading;
        // For some reason the values of z and y seem to be interchanged. If they
        // are not swapped then heading is always incorrect.
        double w = message.getPose().getPose().getOrientation().getW();
        double x = message.getPose().getPose().getOrientation().getX();
        double y = message.getPose().getPose().getOrientation().getZ();
        double z = message.getPose().getPose().getOrientation().getY();
        heading = Math.atan2(2 * y * w - 2 * x * z, x * x - y * y - z * z + w * w) * 180 / Math.PI;
        // Negating the orientation to make the math for rotation in
        // turn-in-place mode easy. Since the actual heading is irrelevant it does
        // no harm.
        currentOrientation = (float) -heading;
        // Only updateConfig the orientation images if the turn-in-place mode is active.
        if (turnInPlaceMode) {
            post(new Runnable() {
                @Override
                public void run() {
                    updateTurnInPlaceRotation();
                }
            });
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                // If the primary contact point is no longer on the screen then ignore
                // the event.
                if (pointerId != INVALID_POINTER_ID) {
                    // If the virtual joystick is in resume-previous-velocity mode.
                    if (previousVelocityMode) {
                        // And the current contact is close to the contact location prior to
                        // ContactUp.
                        if (inLastContactRange(event.getX(event.getActionIndex()),
                                event.getY(event.getActionIndex()))) {
                            // Then use the previous velocity.
                            onContactMove(contactUpLocation.x + joystickRadius, contactUpLocation.y
                                    + joystickRadius);
                        }
                        // Since the current contact is not close to the prior location.
                        else {
                            // Exit the resume-previous-velocity mode.
                            previousVelocityMode = false;
                        }
                    }
                    // Since the resume-previous-velocity mode is not active generate
                    // velocities based on current contact position.
                    else {
                        onContactMove(event.getX(event.findPointerIndex(pointerId)),
                                event.getY(event.findPointerIndex(pointerId)));
                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                // Get the coordinates of the pointer that is initiating the
                // interaction.
                pointerId = event.getPointerId(event.getActionIndex());
                onContactDown();
                // If the current contact is close to the location of the contact prior
                // to contactUp.
                if (inLastContactRange(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()))) {
                    // Trigger resume-previous-velocity mode.
                    previousVelocityMode = true;
                    // The animation calculations/operations are performed in
                    // onContactMove(). If this is not called and the user's finger stays
                    // perfectly still after the down event, no operation is performed.
                    // Calling onContactMove avoids this.
                    onContactMove(contactUpLocation.x + joystickRadius, contactUpLocation.y + joystickRadius);
                } else {
                    onContactMove(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP: {
                // Check if the contact that initiated the interaction is up.
                if ((action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT == pointerId) {
                    onContactUp();
                }
                break;
            }
        }
        return true;
    }

    /**
     * Allows the user the option to turn on the auto-snap feature.
     */
    public void EnableSnapping() {
        magnetTheta = 10;
    }

    /**
     * Allows the user the option to turn off the auto-snap feature.
     */
    public void DisableSnapping() {
        magnetTheta = 1;
    }

    /**
     * Initialize the fields with values that can only be determined once the
     * layout for the views has been determined.
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Call the parent's onLayout to setup the views.
        super.onLayout(changed, l, t, r, b);
        // The parent container must be a square. A square container simplifies the
        // code. A non-square container does not provide any benefit over a
        // square.
        if (mainLayout.getWidth() != mainLayout.getHeight()) {
            // TODO(munjaldesai): Need to throw an exception/error. For now the
            // touch events will not be processed.
            this.setOnTouchListener(null);
        }
        parentSize = mainLayout.getWidth();
        if (parentSize < 200 || parentSize > 400) {
            // TODO: Need to throw an exception for attempting to create
            // a virtual joystick that is either too small or too big. For now the
            // touch events will be processed.
            this.setOnTouchListener(null);
        }
        // Calculate the center coordinates (radius) of parent container
        // (mainLayout).
        joystickRadius = mainLayout.getWidth() / 2;
        normalizingMultiplier = BOX_TO_CIRCLE_RATIO / (parentSize / 2);
        // Calculate the radius of the center divet as a normalize value.
        deadZoneRatio = THUMB_DIVET_RADIUS * normalizingMultiplier;
        // Determine the font size for the text view showing linear velocity. 8.3%
        // of the overall size seems to work well.
        magnitudeText.setTextSize(parentSize / 12);
    }

    /**
     * Scale and rotate the intensity circle instantaneously. The key difference
     * between this method and {@link #animateIntensityCircle(float, long)} is
     * that this method does not attach an animation listener and the animation is
     * instantaneous.
     *
     * @param endScale
     *          The scale factor that must be attained at the end of the
     *          animation.
     */
    private void animateIntensityCircle(float endScale) {
        AnimationSet intensityCircleAnimation = new AnimationSet(true);
        intensityCircleAnimation.setInterpolator(new LinearInterpolator());
        intensityCircleAnimation.setFillAfter(true);
        RotateAnimation rotateAnim;
        rotateAnim = new RotateAnimation(contactTheta, contactTheta, joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        intensityCircleAnimation.addAnimation(rotateAnim);
        ScaleAnimation scaleAnim;
        scaleAnim =
                new ScaleAnimation(contactRadius, endScale, contactRadius, endScale, joystickRadius,
                        joystickRadius);
        scaleAnim.setDuration(0);
        scaleAnim.setFillAfter(true);
        intensityCircleAnimation.addAnimation(scaleAnim);
        // Apply the animation.
        intensity.startAnimation(intensityCircleAnimation);
    }

    /**
     * Scale and rotate the intensity circle over the specified duration. Unlike
     * {@link #animateIntensityCircle(float)} this method registers an animation
     * listener.
     *
     * @param endScale
     *          The scale factor that must be attained at the end of the
     *          animation.
     * @param duration
     *          The duration in milliseconds the animation should take.
     */
    private void animateIntensityCircle(float endScale, long duration) {
        AnimationSet intensityCircleAnimation = new AnimationSet(true);
        intensityCircleAnimation.setInterpolator(new LinearInterpolator());
        intensityCircleAnimation.setFillAfter(true);
        // The listener is needed to set the magnitude text to 0 only after the
        // animation is over.
        intensityCircleAnimation.setAnimationListener(this);
        RotateAnimation rotateAnim;
        rotateAnim = new RotateAnimation(contactTheta, contactTheta, joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(duration);
        rotateAnim.setFillAfter(true);
        intensityCircleAnimation.addAnimation(rotateAnim);
        ScaleAnimation scaleAnim;
        scaleAnim =
                new ScaleAnimation(contactRadius, endScale, contactRadius, endScale, joystickRadius,
                        joystickRadius);
        scaleAnim.setDuration(duration);
        scaleAnim.setFillAfter(true);
        intensityCircleAnimation.addAnimation(scaleAnim);
        // Apply the animation.
        intensity.startAnimation(intensityCircleAnimation);
    }

    /**
     * Fade in and fade out the {@link #orientationWidget}s. The widget best
     * aligned with the {@link #contactTheta} will be the brightest and the
     * successive ones within {@link #ORIENTATION_TACK_FADE_RANGE} the will be
     * faded out proportionally. The tacks out of that range will have alpha set
     * to 0.
     */
    private void animateOrientationWidgets() {
        float deltaTheta;
        for (int i = 0; i < orientationWidget.length; i++) {
            deltaTheta = differenceBetweenAngles(i * 15, contactTheta);
            if (deltaTheta < ORIENTATION_TACK_FADE_RANGE) {
                orientationWidget[i].setAlpha(1.0f - deltaTheta / ORIENTATION_TACK_FADE_RANGE);
            } else {
                orientationWidget[i].setAlpha(0.0f);
            }
        }
    }

    /**
     * From http://actionsnippet.com/?p=1451. Calculates the difference between 2
     * angles. The result is always the minimum difference between 2 angles (0<
     * result <= 360).
     *
     * @param angle0
     *          One of 2 angles used to calculate difference. The order of
     *          arguments does not matter. Must be in degrees.
     * @param angle1
     *          One of 2 angles used to calculate difference. The order of
     *          arguments does not matter. Must be in degrees.
     * @return The difference between the 2 arguments in degrees.
     */
    private float differenceBetweenAngles(float angle0, float angle1) {
        return Math.abs((angle0 + 180 - angle1) % 360 - 180);
    }

    /**
     * Sets {@link #turnInPlaceMode} to false indicating that the turn-in-place is
     * no longer active. It also changes the alpha values appropriately.
     */
    private void endTurnInPlaceRotation() {
        turnInPlaceMode = false;
        currentRotationRange.setAlpha(0.0f);
        previousRotationRange.setAlpha(0.0f);
        intensity.setAlpha(1.0f);
    }

    /**
     * Sets up the visual elements of the virtual joystick.
     */
    private void initVirtualJoystick(Context context) {
        // All the virtual joystick elements must be centered on the parent.
        setGravity(Gravity.CENTER);
        // Instantiate the elements from the layout XML file.
        LayoutInflater.from(context).inflate(R.layout.virtual_joystick, this, true);
        mainLayout = findViewById(R.id.virtual_joystick_layout);
        magnitudeText = findViewById(R.id.magnitude);
        intensity = findViewById(R.id.intensity);
        thumbDivet = findViewById(R.id.thumb_divet);
        orientationWidget = new ImageView[24];
        orientationWidget[0] = findViewById(R.id.widget_0_degrees);
        orientationWidget[1] = findViewById(R.id.widget_15_degrees);
        orientationWidget[2] = findViewById(R.id.widget_30_degrees);
        orientationWidget[3] = findViewById(R.id.widget_45_degrees);
        orientationWidget[4] = findViewById(R.id.widget_60_degrees);
        orientationWidget[5] = findViewById(R.id.widget_75_degrees);
        orientationWidget[6] = findViewById(R.id.widget_90_degrees);
        orientationWidget[7] = findViewById(R.id.widget_105_degrees);
        orientationWidget[8] = findViewById(R.id.widget_120_degrees);
        orientationWidget[9] = findViewById(R.id.widget_135_degrees);
        orientationWidget[10] = findViewById(R.id.widget_150_degrees);
        orientationWidget[11] = findViewById(R.id.widget_165_degrees);
        orientationWidget[12] = findViewById(R.id.widget_180_degrees);
        orientationWidget[13] = findViewById(R.id.widget_195_degrees);
        orientationWidget[14] = findViewById(R.id.widget_210_degrees);
        orientationWidget[15] = findViewById(R.id.widget_225_degrees);
        orientationWidget[16] = findViewById(R.id.widget_240_degrees);
        orientationWidget[17] = findViewById(R.id.widget_255_degrees);
        orientationWidget[18] = findViewById(R.id.widget_270_degrees);
        orientationWidget[19] = findViewById(R.id.widget_285_degrees);
        orientationWidget[20] = findViewById(R.id.widget_300_degrees);
        orientationWidget[21] = findViewById(R.id.widget_315_degrees);
        orientationWidget[22] = findViewById(R.id.widget_330_degrees);
        orientationWidget[23] = findViewById(R.id.widget_345_degrees);
        // Initially hide all the widgets.
        for (ImageView tack : orientationWidget) {
            tack.setAlpha(0.0f);
            tack.setVisibility(INVISIBLE);
        }
        // The value (radius) 40 is arbitrary, but small enough to work for the
        // smallest sized virtual joystick. Once the layout is set a value is
        // calculated based on the size of the virtual joystick.
        magnitudeText.setTranslationX((float) (40 * Math.cos((90 + contactTheta) * Math.PI / 180.0)));
        magnitudeText.setTranslationY((float) (40 * Math.sin((90 + contactTheta) * Math.PI / 180.0)));
        // Hide the intensity circle.
        animateIntensityCircle(0);
        // Initially the orientationWidgets should point to 0 degrees.
        contactTheta = 0;
        animateOrientationWidgets();
        currentRotationRange = findViewById(R.id.top_angle_slice);
        previousRotationRange = findViewById(R.id.mid_angle_slice);
        // Hide the slices/arcs used during the turn-in-place mode.
        currentRotationRange.setAlpha(0.0f);
        previousRotationRange.setAlpha(0.0f);
        lastVelocityDivet = findViewById(R.id.previous_velocity_divet);
        contactUpLocation = new Point(0, 0);
        holonomic = false;
        for (ImageView tack : orientationWidget) {
            tack.setVisibility(INVISIBLE);
        }
    }

    /**
     * Update the virtual joystick to indicate a contact down has occurred.
     */
    private void onContactDown() {
        // The divets should be completely opaque indicating
        // the virtual joystick is active.
        thumbDivet.setAlpha(1.0f);
        magnitudeText.setAlpha(1.0f);
        // Previous contact location need not be shown any more.
        lastVelocityDivet.setAlpha(0.0f);
        // Restore the orientation tacks.
        for (ImageView tack : orientationWidget) {
            tack.setVisibility(VISIBLE);
        }
        publishVelocity = true;
    }

    /**
     * Updates the virtual joystick layout based on the location of the contact.
     * Generates the velocity messages. Switches in and out of turn-in-place.
     *
     * @param x
     *          The x coordinates of the contact relative to the parent container.
     * @param y
     *          The y coordinates of the contact relative to the parent container.
     */
    private void onContactMove(float x, float y) {
        // Get the coordinates of the contact relative to the center of the main
        // layout.
        float thumbDivetX = x - joystickRadius;
        float thumbDivetY = y - joystickRadius;
        // Convert the coordinates from Cartesian to Polar.
        contactTheta = (float) (Math.atan2(thumbDivetY, thumbDivetX) * 180 / Math.PI + 90);
        contactRadius =
                (float) Math.sqrt(thumbDivetX * thumbDivetX + thumbDivetY * thumbDivetY)
                        * normalizingMultiplier;
        // Calculate the distance (0 to 1) from the center divet to the contact
        // point.
        normalizedMagnitude = (contactRadius - deadZoneRatio) / (1 - deadZoneRatio);
        // Perform bounds checking.
        if (contactRadius >= 1f) {
            // Since the contact is outside the outer ring, reset the coordinate for
            // the thumb divet to the on the outer ring.
            thumbDivetX /= contactRadius;
            thumbDivetY /= contactRadius;
            // The magnitude should not exceed 1.
            normalizedMagnitude = 1f;
            contactRadius = 1f;
        } else if (contactRadius < deadZoneRatio) {
            // Since the contact is inside the dead zone snap the thumb divet to the
            // dead zone. It should stay there till the contact gets outside the
            // deadzone area.
            thumbDivetX = 0;
            thumbDivetY = 0;
            // Prevent normalizedMagnitude going negative inside the deadzone.
            normalizedMagnitude = 0f;
        }

        // Magnetize!
        // If the contact is not snapped to the x axis.
        if (!magnetizedXAxis) {
            // Check if the contact should be snapped to either axis.
            if ((contactTheta + 360) % 90 < magnetTheta) {
                // If the current angle is within MAGNET_THETA degrees + 0, 90, 180, or
                // 270 then subtract the additional degrees so that the current theta is
                // 0, 90, 180, or 270.
                contactTheta -= ((contactTheta + 360) % 90);
            } else if ((contactTheta + 360) % 90 > (90 - magnetTheta)) {
                // If the current angle is within MAGNET_THETA degrees - 0, 90, 180, or
                // 270 then add the additional degrees so that the current theta is 0,
                // 90, 180, or 270.
                contactTheta += (90 - ((contactTheta + 360) % 90));
            }
            // Indicate that the contact has been snapped to the x-axis.
            if (floatCompare(contactTheta, 90) || floatCompare(contactTheta, 270)) {
                magnetizedXAxis = true;
            }
        } else {
            // Use a wider range to keep the contact snapped in.
            if (differenceBetweenAngles((contactTheta + 360) % 360, 90) < POST_LOCK_MAGNET_THETA) {
                contactTheta = 90;
            } else if (differenceBetweenAngles((contactTheta + 360) % 360, 270) < POST_LOCK_MAGNET_THETA) {
                contactTheta = 270;
            }
            // Indicate that the contact is not snapped to the x-axis.
            else {
                magnetizedXAxis = false;
            }
        }

        // Update the size and location (scale and rotation) of various elements.
        animateIntensityCircle(contactRadius);
        animateOrientationWidgets();
        updateThumbDivet(thumbDivetX, thumbDivetY);
        updateMagnitudeText();
        // Publish the velocities.
        if (holonomic) {
            publishVelocity(normalizedMagnitude * Math.cos(contactTheta * Math.PI / 180.0),
                    normalizedMagnitude * Math.sin(contactTheta * Math.PI / 180.0), 0);
        } else {
            publishVelocity(normalizedMagnitude * Math.cos(contactTheta * Math.PI / 180.0), 0,
                    normalizedMagnitude * Math.sin(contactTheta * Math.PI / 180.0));
        }

        // Check if the turn-in-place mode needs to be activated/deactivated.
        updateTurnInPlaceMode();
    }

    /**
     * Enable/Disable turn-in-place mode.
     */
    private void updateTurnInPlaceMode() {
        if (!turnInPlaceMode) {
            if (floatCompare(contactTheta, 270)) {
                // If the user is turning left and the turn-in-place mode is not active
                // then activate it for a left turn.
                turnInPlaceMode = true;
                rightTurnOffset = 0;
            } else if (floatCompare(contactTheta, 90)) {
                // If the user is turning right and the turn-in-place mode is not active
                // then activate it for a right turn.
                turnInPlaceMode = true;
                rightTurnOffset = 15;
            } else {
                // Nothing to do while not in turn-in-place mode and not at 270/90.
                return;
            }
            // Initiate the turn-in-place mode but wait some time before changing the
            // images. This is to avoid the users getting seizures because of the
            // quick changes every time they cross 270 or 90.
            initiateTurnInPlace();
            // Start a timer and if the user is still turning in place when the timer
            // is up, then visually indicate entering turn-in-place mode.
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (turnInPlaceMode) {
                                currentRotationRange.setAlpha(1.0f);
                                previousRotationRange.setAlpha(1.0f);
                                intensity.setAlpha(0.2f);
                            }
                        }
                    });
                    postInvalidate();
                }
            }, TURN_IN_PLACE_CONFIRMATION_DELAY);
        } else if (!(floatCompare(contactTheta, 270) || floatCompare(contactTheta, 90))) {
            // If the user was in turn-in-place mode and is now no longer on the x
            // axis, then exit turn-in-place mode.
            endTurnInPlaceRotation();
        }
    }

    /**
     * The divets and the ring are made transparent to reflect that the virtual
     * joystick is no longer active. The intensity circle is slowly scaled to 0.
     */
    private void onContactUp() {
        // TODO(munjaldesai): The 1000 should eventually be replaced with a number
        // that reflects the physical characteristics of the motor controller along
        // with the latency associated with the connection.
        animateIntensityCircle(0, (long) (normalizedMagnitude * 1000));
        magnitudeText.setAlpha(0.4f);
        // Place the lastVelocityDivet at the location of the last known contact.
        lastVelocityDivet.setTranslationX(thumbDivet.getTranslationX());
        lastVelocityDivet.setTranslationY(thumbDivet.getTranslationY());
        lastVelocityDivet.setAlpha(0.4f);
        contactUpLocation.x = (int) (thumbDivet.getTranslationX());
        contactUpLocation.y = (int) (thumbDivet.getTranslationY());
        // Move the thumb divet back to the center.
        updateThumbDivet(0, 0);
        // Reset the pointer id.
        pointerId = INVALID_POINTER_ID;
        // The robot should stop moving.
        publishVelocity(0, 0, 0);
        // Stop publishing the velocity since the contact is no longer on the
        // screen.
        publishVelocity = false;
        // Publish one last message to make sure the robot stops.
        publisher.publish(currentVelocityCommand);
        // Turn-in-place should not be active anymore.
        endTurnInPlaceRotation();
        // Hide the orientation tacks.
        for (ImageView tack : orientationWidget) {
            tack.setVisibility(INVISIBLE);
        }
    }

    /**
     * Publish the velocity as a ROS Twist message.
     *
     * @param linearVelocityX
     *          The normalized linear velocity (-1 to 1).
     * @param angularVelocityZ
     *          The normalized angular velocity (-1 to 1).
     */
    private void publishVelocity(double linearVelocityX, double linearVelocityY,
                                 double angularVelocityZ) {
        currentVelocityCommand.getLinear().setX(linearVelocityX);
        currentVelocityCommand.getLinear().setY(-linearVelocityY);
        currentVelocityCommand.getLinear().setZ(0);
        currentVelocityCommand.getAngular().setX(0);
        currentVelocityCommand.getAngular().setY(0);
        currentVelocityCommand.getAngular().setZ(-angularVelocityZ);
    }

    /**
     * Called each time turn-in-place mode is initiated.
     */
    private void initiateTurnInPlace() {
        // Record the orientation when the turn-in-place was initiated.
        turnInPlaceStartTheta = (currentOrientation + 360) % 360;
        RotateAnimation rotateAnim;
        rotateAnim =
                new RotateAnimation(rightTurnOffset, rightTurnOffset, joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        currentRotationRange.startAnimation(rotateAnim);
        rotateAnim = new RotateAnimation(15, 15, joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        previousRotationRange.startAnimation(rotateAnim);
    }

    /**
     * Update the linear velocity text view.
     */
    private void updateMagnitudeText() {
        // Don't updateConfig when the user is turning in place.
        if (!turnInPlaceMode) {
            magnitudeText.setText((int) (normalizedMagnitude * 100) + "%");
            magnitudeText.setTranslationX((float) (parentSize / 4 * Math.cos((90 + contactTheta)
                    * Math.PI / 180.0)));
            magnitudeText.setTranslationY((float) (parentSize / 4 * Math.sin((90 + contactTheta)
                    * Math.PI / 180.0)));
        }
    }

    /**
     * Based on the difference between the current orientation and the orientation
     * when the turn-in-place mode was initiated, updateConfig the visuals.
     */
    private void updateTurnInPlaceRotation() {
        final float currentTheta = (currentOrientation + 360) % 360;
        float offsetTheta;
        // Calculate the difference between the orientations.
        offsetTheta = (turnInPlaceStartTheta - currentTheta + 360) % 360;
        offsetTheta = 360 - offsetTheta;
        // Show the current rotation amount.
        magnitudeText.setText(String.valueOf((int) offsetTheta));
        // Calculate theta in increments of 15 degrees. (0-14 => 0, 15-29=>15, etc).
        offsetTheta = (int) (offsetTheta - (offsetTheta % 15));
        // Rotate the 2 arcs based on the offset in orientation.
        RotateAnimation rotateAnim;
        rotateAnim =
                new RotateAnimation(offsetTheta + rightTurnOffset, offsetTheta + rightTurnOffset,
                        joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        currentRotationRange.startAnimation(rotateAnim);
        rotateAnim =
                new RotateAnimation(offsetTheta + 15, offsetTheta + 15, joystickRadius, joystickRadius);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        previousRotationRange.startAnimation(rotateAnim);
    }

    /**
     * Moves the {@link #thumbDivet} to the specified coordinates (under the
     * contact) and also orients it so that is facing the direction opposite to
     * the center of the {@link #mainLayout}.
     *
     * @param x
     *          The x coordinate relative to the center of the {@link #mainLayout}
     * @param y
     *          The Y coordinate relative to the center of the {@link #mainLayout}
     */
    private void updateThumbDivet(float x, float y) {
        // Offset the specified coordinates to ensure that the center of the thumb
        // divet is under the thumb.
        thumbDivet.setTranslationX(-THUMB_DIVET_RADIUS);
        thumbDivet.setTranslationY(-THUMB_DIVET_RADIUS);
        // Set the orientation. This must be done before translation.
        thumbDivet.setRotation(contactTheta);
        thumbDivet.setTranslationX(x);
        thumbDivet.setTranslationY(y);
    }

    /**
     * Comparing 2 float values.
     *
     * @param v1
     * @param v2
     * @return True if v1 and v2 and within {@value #FLOAT_EPSILON} of each other.
     *         False otherwise.
     */
    private boolean floatCompare(float v1, float v2) {
        return Math.abs(v1 - v2) < FLOAT_EPSILON;
    }

    private boolean inLastContactRange(float x, float y) {
        return Math.sqrt((x - contactUpLocation.x - joystickRadius)
                * (x - contactUpLocation.x - joystickRadius) + (y - contactUpLocation.y - joystickRadius)
                * (y - contactUpLocation.y - joystickRadius)) < THUMB_DIVET_RADIUS;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_15/virtual_joystick_view");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher(topicName, geometry_msgs.Twist._TYPE);
        currentVelocityCommand = publisher.newMessage();
        Subscriber<nav_msgs.Odometry> subscriber =
                connectedNode.newSubscriber("odom", nav_msgs.Odometry._TYPE);
        subscriber.addMessageListener(this);
        publisherTimer = new Timer();
        publisherTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (publishVelocity) {
                    publisher.publish(currentVelocityCommand);
                }
            }
        }, 0, 80);
    }

    @Override
    public void onShutdown(Node node) {
    }

    @Override
    public void onShutdownComplete(Node node) {
        publisherTimer.cancel();
        publisherTimer.purge();
    }

    @Override
    public void onError(Node node, Throwable throwable) {
    }
}