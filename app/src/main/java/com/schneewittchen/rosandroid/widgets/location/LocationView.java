package com.schneewittchen.rosandroid.widgets.location;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;


/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class LocationView extends PublisherWidgetView {

    public static final String TAG = LocationView.class.getSimpleName();

    Context context;

    Paint buttonPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;

    LocationRequest locationRequest;
    double altitude;
    double latitude;
    double longitude;
    String provider;


    public LocationView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        LocationEntity entity = (LocationEntity) widgetEntity;

        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //long interval = (long)(1.0/entity.publishRate)*1000;
        //Log.i(TAG,"INTERVAL - --------------------" + interval);
        long interval = 500;
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(interval);
        locationRequest.setSmallestDisplacement(0);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNecessary(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }
        getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        provider = locationResult.getLastLocation().getProvider();
                        altitude = locationResult.getLastLocation().getAltitude();
                        latitude = locationResult.getLastLocation().getLatitude();
                        longitude = locationResult.getLastLocation().getLongitude();
                        publishCoordinates();
                    }
                },
                Looper.myLooper());

    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
    }

    private void changeState(boolean pressed) {
        LocationEntity entity = (LocationEntity) widgetEntity;
        entity.buttonPressed = pressed;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }

        LocationEntity entity = (LocationEntity) widgetEntity;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(entity.buttonPressed)
                    changeState(false);
                else
                    changeState(true);
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

        LocationEntity entity = (LocationEntity) widgetEntity;

        if (entity.rotation == 90 || entity.rotation == 270) {
            textLayoutWidth = height;
        }

        if(!entity.buttonPressed)
            buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        else
            buttonPaint.setColor(getResources().getColor(R.color.color_attention));

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

    public void publishCoordinates() {

        LocationEntity entity = (LocationEntity) widgetEntity;

        if(entity.buttonPressed) {
            Log.d(TAG, " Provider: " + provider + " Longitude: " + longitude + " Latitude: " + latitude + " Altitude " + altitude);
            this.publishViewData(new LocationData(latitude, longitude, altitude, provider));
        }
    }

}
