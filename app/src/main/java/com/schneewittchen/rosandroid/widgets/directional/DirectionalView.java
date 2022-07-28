package com.schneewittchen.rosandroid.widgets.directional;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;
import com.schneewittchen.rosandroid.widgets.button.ButtonData;

import android.text.StaticLayout;
import android.text.TextPaint;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 18.10.19
 */
public class DirectionalView extends PublisherWidgetView {

    public class MoveThread extends Thread {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1);
                    publishViewData(new DirectionalData());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                }
            }
        }
    }

    public static final String TAG = DirectionalView.class.getSimpleName();

    Paint buttonPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;
    MoveThread t;

    public DirectionalView(Context context) {
        super(context);
        init();
    }

    public DirectionalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
        t = new MoveThread();
    }

    private void changeState(boolean pressed) {
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_BUTTON_RELEASE:
            case MotionEvent.ACTION_UP:
                buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
                changeState(false);
                t.interrupt();
                t = new MoveThread();
                break;
            case MotionEvent.ACTION_DOWN:
                buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                changeState(true);
                t.start();
                break;
            default:
                return false;
        }

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;

        DirectionalEntity entity = (DirectionalEntity) widgetEntity;

        if (entity.rotation == 90 || entity.rotation == 270) {
            textLayoutWidth = height;
        }

        canvas.drawRect(new Rect(0, 0, (int) width, (int) height), buttonPaint);

        staticLayout = new StaticLayout(entity.text,
                textPaint,
                (int) textLayoutWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0,
                false);
        canvas.save();
        canvas.rotate(entity.rotation, width / 2, height / 2);
        canvas.translate(((width / 2) - staticLayout.getWidth() / 2), height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
