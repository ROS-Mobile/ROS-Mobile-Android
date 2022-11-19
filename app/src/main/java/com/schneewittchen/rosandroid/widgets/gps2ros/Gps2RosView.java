package com.schneewittchen.rosandroid.widgets.gps2ros;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;


/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosView extends PublisherWidgetView {

    public static final String TAG = Gps2RosView.class.getSimpleName();

    public Gps2RosView(Context context) {
        super(context);
        init();
    }

    public Gps2RosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }



}
