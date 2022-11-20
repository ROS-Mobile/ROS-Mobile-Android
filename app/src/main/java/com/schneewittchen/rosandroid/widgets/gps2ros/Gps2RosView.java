package com.schneewittchen.rosandroid.widgets.gps2ros;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationListener;
import android.location.LocationManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;
import android.util.Log;


/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosView extends PublisherWidgetView implements LocationListener {

    public static final String TAG = Gps2RosView.class.getSimpleName();

    Paint buttonPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;
    boolean buttonPressed;

    protected LocationManager locationManager;
    double latitude;
    double longitude;
    Context context;

    public Gps2RosView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Gps2RosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        // By default it is ...
        buttonPressed = false;

        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("Gps2RosView","check permissions failed!");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Log.e("Gps2RosView","gps is not enabled!");
            return;
        }
    }

    private void changeState(boolean pressed) {
        this.buttonPressed = pressed;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(buttonPressed) {
                    buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
                    changeState(false);
                }
                else
                {
                    buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                    changeState(true);
                }
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float textLayoutWidth = width;

        Gps2RosEntity entity = (Gps2RosEntity) widgetEntity;

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

    @Override
    public void onLocationChanged(Location location) {
        if(buttonPressed) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i("Gps2RosView","Longitude: "+longitude+" Latitude: " + latitude);
            Gps2RosData data = new Gps2RosData(latitude,longitude);
            this.publishViewData(data);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
