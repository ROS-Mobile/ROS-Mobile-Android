package com.schneewittchen.rosandroid.widgets.gps2ros;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.util.TypedValue;
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
import android.location.LocationProvider;


/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosView extends PublisherWidgetView implements LocationListener {

    public static final String TAG = Gps2RosView.class.getSimpleName();

    Paint textPaint;
    float textSize;
    float borderWidth;
    String displayedText;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected boolean gps_enabled, network_enabled;
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

        float textDip = 18f;
        float borderDip = 10f;

        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textDip,
                getResources().getDisplayMetrics());
        borderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderDip,
                getResources().getDisplayMetrics());

        borderWidth = 10;

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(R.color.whiteHigh));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);


        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("Gps2RosView","checkSelfPermission!");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            Log.e("Gps2RosView","gpsEnabled");
            return;
        }
    }


        @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }
        Log.i("Gps2RosView","Longitude: "+longitude+" Latitude: " + latitude);

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float middleX = width/2;

        float left = borderWidth/2;
        float right = width - borderWidth/2;
        float top = borderWidth * 2;
        float bottom = height - borderWidth - textSize;

        // Draw status text
        displayedText = ("GPS 2 ROS");
        canvas.drawText(displayedText, middleX, height, textPaint);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.i("Gps2RosView","Longitude: "+longitude+" Latitude: " + latitude);

        Gps2RosData data = new Gps2RosData(latitude,longitude);
        this.publishViewData(data);
    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.d("Gps2RosView","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {

        Log.d("Gps2RosView","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.d("Gps2RosView","status");
    }

}
