package com.schneewittchen.rosandroid.widgets.viz2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.ui.general.DataListener;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;
import com.schneewittchen.rosandroid.ui.views.widgets.LayerView;
import com.schneewittchen.rosandroid.ui.views.widgets.WidgetGroupView;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class Viz2DView extends WidgetGroupView {

    public static final String TAG = Viz2DView.class.getSimpleName();

    private DataListener dataListener;
    private Paint borderPaint;
    private Paint paintBackground;
    private VisualizationView layerView;


    public Viz2DView(Context context) {
        super(context);
        init();
    }

    public Viz2DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layerView.layout(0, 0, getWidth(), getHeight());
    }


    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.argb(100, 255, 255, 255));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);

        // Background color
        paintBackground = new Paint();
        paintBackground.setColor(Color.argb(100, 0, 0, 0));
        paintBackground.setStyle(Paint.Style.FILL);

        layerView = new VisualizationView(getContext());
        this.addView(layerView);
    }

    @Override
    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);

        layerView.getCamera().jumpToFrame(((Viz2DEntity)widgetEntity).frame);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return layerView.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPaint(paintBackground);

        // Define image size based on the Bitmap width and height
        float leftViz = 0F;
        float topViz = 0F;
        float widthViz = getWidth();
        float heightViz = getHeight();

        // Draw Border
        canvas.drawRect(leftViz, topViz, widthViz, heightViz, borderPaint);

        super.onDraw(canvas);
    }


    @Override
    public void onNewData(RosData data) {
        layerView.onNewData(data);
    }


    @Override
    public void publishViewData(BaseData data) {
        if(dataListener == null) return;

        data.setTopic(widgetEntity.topic);
        dataListener.onNewWidgetData(data);
    }

    @Override
    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    @Override
    public void addLayer(LayerView layer) {
        layerView.addLayer(layer);
    }
}