package com.schneewittchen.rosandroidlib;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class Utils {

    public static float pxToCm(Context context, float px) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float cm = px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, dm);

        return cm;
    }

    public static float cmToPx(Context context, float cm) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, cm*10, dm);

        return px;
    }

    public static float dpToPx(Context context, float dp){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);

        return px;
    }
}
