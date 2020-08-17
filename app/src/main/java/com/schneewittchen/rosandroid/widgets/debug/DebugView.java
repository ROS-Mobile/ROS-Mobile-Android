package com.schneewittchen.rosandroid.widgets.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.camera.CameraData;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on
 * @modified by
 */
public class DebugView extends BaseView {

    public static final String TAG = "DebugView";


    public DebugView(Context context) {
        super(context);
        init();
    }

    public DebugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "On draw");
    }

    @Override
    public void setData(BaseData data) {
    }

}