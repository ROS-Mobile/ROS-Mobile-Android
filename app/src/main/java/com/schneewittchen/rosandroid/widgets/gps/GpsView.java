package com.schneewittchen.rosandroid.widgets.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

public class GpsView extends BaseView {
    public static final String TAG = "GpsView";

    // Open Street Map (OSM)
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private GeoPoint locationGeoPoint = new GeoPoint(53.872018, 10.704724);
    IMapController mapController = null;

    // Rectangle Surrounding
    Paint paint;
    float cornerWidth;

    // Grid Map Information
    GpsData data;

    // Zoom Parameters
    private static float MIN_ZOOM = 1;         // min. and max. zoom
    private static float MAX_ZOOM = 18;
    private float scaleFactor = 1;
    private ScaleGestureDetector detector;

    private static int NONE = 0;                // mode
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;

    private float startX = 0f;                  // finger position tracker
    private float startY = 0f;

    private float translateX = 0f;              // Amount of translation
    private float translateY = 0f;

    private float previousTranslateX = 0f;      // Past amount of translation
    private float previousTranslateY = 0f;

    public GpsView(Context context) {
        super(context);
        init();
    }

    public GpsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.cornerWidth = Utils.dpToPx(this.getContext(), 8);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.whiteHigh));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        // OSM (initialize the map)
        Context ctx = this.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = new MapView(this.getContext(), null, null);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Map controller
        mapController = map.getController();

        // Touch
        detector = new ScaleGestureDetector(getContext(), new GpsView.ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // return map.onTouchEvent(event);

        boolean dragged = false;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;
                break;

            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                //We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
                //This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                        Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if(distance > 0) {
                    dragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;

                //All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and
                //previousTranslate
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                //This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
                //and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);

        //We redraw the canvas only in the following cases:
        //
        // o The mode is ZOOM
        //        OR
        // o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
        //   set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            this.invalidate();
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "On Draw");
        super.onDraw(canvas);
        canvas.save();

        // Get vizualization size
        float left = 0F;
        float right = 0F;
        float width = getWidth();
        float height = getHeight();

        // Set overlay item
        OverlayItem overlayItem = new OverlayItem("Position", "Robot", locationGeoPoint);
        ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<OverlayItem>();
        overlayItemArrayList.add(overlayItem);
        ItemizedOverlay<OverlayItem> locationOverlay = new ItemizedIconOverlay<OverlayItem>(this.getContext(), overlayItemArrayList, null);

        // Move the map to specific location
        mapController.setCenter(locationGeoPoint);
        mapController.setZoom(scaleFactor);
        map.requestLayout();

        // Draw the OMS
        map.layout((int) left, (int) right, (int) width, (int) height);
        map.getOverlays().add(locationOverlay);
        map.draw(canvas);

        // Apply the changes
        canvas.restore();
        // Put a rectangle around
        canvas.drawRoundRect(left, right, width, height, cornerWidth, cornerWidth, paint);
    }

    @Override
    public void setData(BaseData data) {
        System.out.println("GpsView: SetData!");
        this.data = (GpsData) data;
        locationGeoPoint.setLatitude(this.data.lat);
        locationGeoPoint.setLongitude(this.data.lon);
        this.invalidate();
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;
        }
    }
}
