package com.schneewittchen.rosandroid.widgets.location;

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
import androidx.core.content.ContextCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.PublisherWidgetView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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

    protected LocationManager locationManager;
    double gpsLatitude;
    double gpsLongitude;
    double gpsAltitude;
    double networkLatitude;
    double networkLongitude;
    double networkAltitude;
    long gpsTime = 0;
    long networkTime = 0;

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            gpsTime = location.getTime();
            gpsLatitude = location.getLatitude();
            gpsLongitude = location.getLongitude();
            gpsAltitude = location.getAltitude();
            Log.d("Gps2RosView","GPS - Longitude: "+ gpsLongitude +" Latitude: " + gpsLatitude +" Altitude: " + gpsAltitude);
            publishCoordinates();
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            networkTime = location.getTime();
            networkLatitude = location.getLatitude();
            networkLongitude = location.getLongitude();
            networkAltitude = location.getAltitude();
            Log.d("Gps2RosView","NETWORK - Longitude: "+networkLongitude+" Latitude: " + networkLatitude +" Altitude: " + networkAltitude);
            publishCoordinates();
        }
    };

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

        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsIfNecessary(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            });
        }

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(gpsEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (networkEnabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

        if(!gpsEnabled && !networkEnabled)
        {
            Log.e("Gps2RosView","gps and network locations are not enabled!");
            return;
        }
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
            double latitude;
            double longitude;
            double altitude;
            String type;
            Log.d("Gps2RosView", "time network " + networkTime + " time GPS "+ gpsTime);
            if( 0 < gpsTime - networkTime) {
                latitude = gpsLatitude;
                longitude = gpsLongitude;
                altitude = gpsAltitude;
                type = "GPS";
            }
            else
            {
                latitude = networkLatitude;
                longitude = networkLongitude;
                altitude = networkAltitude;
                type = "NETWORK";
            }
            Log.d("Gps2RosView", type + " Longitude: " + longitude + " Latitude: " + latitude + " Altitude " + altitude);
            this.publishViewData(new LocationData(latitude, longitude, altitude, type));
        }
    }

}
