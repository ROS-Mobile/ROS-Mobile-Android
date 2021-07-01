package com.schneewittchen.rosandroid.widgets.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;
import com.schneewittchen.rosandroid.utility.Utils;

import org.ros.internal.message.Message;

import java.util.ArrayList;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class DebugView extends SubscriberWidgetView {

    public static final String TAG = DebugView.class.getSimpleName();

    // Canvas parameter
    private Paint paint;
    private Paint paintDark;
    private float cornerWidth;

    // GestureDetector for doubleClick
    private GestureDetectorCompat gestureDetector;

    // Views
    private ScrollView scrollView;
    private TextView textView;

    // Container for textView output
    private Boolean stopUpdate;
    private String output;
    private ArrayList<String> dataList;

    // Finger position tracker
    private float lastY = 0.0f;

    // Mode
    private static int NONE = 0;
    private static int DRAG = 1;
    private int mode;

    // Amount of translation
    private float translateY = 0f;

    // Drag parameters
    private int posY = 0;
    private float dragSensitivity = 1; //0.05f;


    public DebugView(Context context) {
        super(context);
        init();
    }

    public DebugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        // Set canvas parameter
        this.cornerWidth = 0; //Utils.dpToPx(getContext(), 8);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.borderColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setTextSize(20);

        // Initalize Views
        scrollView = new ScrollView(getContext());
        textView = new TextView(getContext());
        textView.setVisibility(View.VISIBLE);

        // Define action for onDoubleTap
        gestureDetector = new GestureDetectorCompat(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        stopUpdate = !stopUpdate;
                        if (stopUpdate) {
                            output = "";
                            for (String string : dataList) {
                                output = output.concat(string);
                                output = output.concat("\n\n");
                            }
                            updateView();
                        }
                        return true;
                    }
                });

        // Initialize variables
        stopUpdate = false;
        output = "";
        dataList = new ArrayList<>();

        // Background color
        paintDark = new Paint();
        paintDark.setColor(Color.argb(100, 0, 0, 0));
        //paintDark.setColor(getResources().getColor(R.color.black02dp));
        paintDark.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        // Get vizualization size
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        // Draw background and rectangle
        canvas.drawPaint(paintDark);
        canvas.drawRoundRect(leftViz, topViz, widthViz, heightViz, cornerWidth, cornerWidth, paint);
        canvas.translate(this.cornerWidth, this.cornerWidth);

        // Calculate the drag
        //posY = posY - (int) (translateY * dragSensitivity);
        //posY = Math.max(posY, 0);

        // Draw data
        textView.scrollTo(0, posY);
        scrollView.measure(getWidth(), getHeight());
        scrollView.layout(0, 0, getWidth(), getHeight());
        scrollView.draw(canvas);

        // Apply changes
        canvas.restore();
    }

    @Override
    public void onNewMessage(Message message) {
        DebugData debugData = new DebugData(message);
        DebugEntity entity = (DebugEntity) widgetEntity;

        dataList.add(debugData.value);
        while (dataList.size() > entity.numberMessages) {
            dataList.remove(0);
        }

        if (!stopUpdate) {
            this.output = debugData.value;
            updateView();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }

        // Handle double click
        gestureDetector.onTouchEvent(event);

        // Handle scrolling
        if (stopUpdate) {
            boolean dragged = false;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = DRAG;
                    lastY = event.getY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    translateY = event.getY() - lastY;
                    lastY = event.getY();
                    posY = posY - (int) (translateY * dragSensitivity);
                    posY = Math.max(posY, 0);

                    if (translateY != 0) {
                        dragged = true;
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    mode = NONE;
                    dragged = false;
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = DRAG;
                    break;
            }

            if ((mode == DRAG && dragged)) {
                this.invalidate();
            }
        }
        return true;
    }

    private void updateView() {
        int width = getWidth() - (int) (this.cornerWidth * 2);
        textView.setText(this.output);
        textView.measure(width, 0);
        scrollView.removeView(textView);
        scrollView.addView(textView, width, textView.getMeasuredHeight());
        this.invalidate();
    }
}