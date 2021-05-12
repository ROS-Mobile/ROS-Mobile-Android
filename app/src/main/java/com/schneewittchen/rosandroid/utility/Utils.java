package com.schneewittchen.rosandroid.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.schneewittchen.rosandroid.BuildConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 25.09.20
 * @modified by Nico Studt
 */
public class Utils {

    public static boolean isVisible(View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }

        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        final Rect screen = new Rect(0, 0, width, height);
        return actualPosition.intersect(screen);
    }
    /**
     * Get a string resource with its identifier.
     * @param context Current context
     * @param resourceName Identifier
     * @return String resource
     */
    public static String getStringByName(Context context, String resourceName) {
        String packageName = context.getPackageName();
        int resourceId = context.getResources().getIdentifier(resourceName, "string", packageName);
        return context.getResources().getString(resourceId);
    }

    public static void hideSoftKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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

    public static Object getObjectFromClassName(String relativeClassPath){
        String classPath = BuildConfig.APPLICATION_ID + relativeClassPath;

        try {
            Class<?> clazz = Class.forName(classPath);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();

        } catch (Exception e) {
            return null;
        }
    }

    public static int getResId(String resName, Class<?> clazz) {
        try {
            Field idField = clazz.getDeclaredField(resName);
            return idField.getInt(idField);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Convert byte array to hex string
     * @param bytes toConvert
     * @return hexValue
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     * @param str which to be converted
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes(StandardCharsets.UTF_8); } catch (Exception ex) { return null; }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename which to be converted to string
     * @return String value of File
     * @throws java.io.IOException if error occurs
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8=false;
            int read,count=0;
            while((read=is.read(bytes)) != -1) {
                if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                    isUTF8=true;
                    baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(), StandardCharsets.UTF_8) : new String(baos.toByteArray());
        } finally {
            try{ is.close(); } catch(Exception ignored){}
        }
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());

                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();

                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;

                        } else {

                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions

        return "";
    }


    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static ArrayList<String> getIPAddressList(boolean useIPv4) {

        ArrayList<String> ipAddresses = new ArrayList<>();

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());

                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();

                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                ipAddresses.add(sAddr);

                        } else {

                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                ipAddresses.add(delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase());
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions

        return ipAddresses;
    }

    /**
     * Check if host is reachable.
     * @param host The host to check for availability. Can either be a machine name, such as "google.com",
     *             or a textual representation of its IP address, such as "8.8.8.8".
     * @param port The port number.
     * @param timeout The timeout in milliseconds.
     * @return True if the host is reachable. False otherwise.
     */
    public static boolean isHostAvailable(final String host, final int port, final int timeout) {
        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);

            socket.connect(inetSocketAddress, timeout);
            return true;

        } catch (ConnectException e) {
            Log.e("Connection", "Failed do to unavailable network.");

        }catch (SocketTimeoutException e) {
            Log.e("Connection", "Failed do to reach host in time.");

        } catch (UnknownHostException e) {
            Log.e("Connection", "Unknown host.");

        } catch (IOException e) {
            Log.e("Connection", "IO Exception.");
        }

        return false;
    }

    public static String getWifiSSID(WifiManager wifiManager) {
        if (wifiManager == null)
            return null;

        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }

        return null;
    }



    public static String numberToDegrees(int number){
        return new Integer(number).toString() + "°";
    }

    public static int degreesToNumber(String degrees) {
        return Integer.parseInt(degrees.substring(0, degrees.length() - 1));
    }

    /**
     * Check if class of an object contains a field by a given field name.
     * @param object Object to check
     * @param fieldName Name of the field
     * @return Object class includes the field
     */
    public static boolean doesObjectContainField(Object object, String fieldName) {
        Class<?> objectClass = object.getClass();

        for (java.lang.reflect.Field field : objectClass.getFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }

        return false;
    }
}
