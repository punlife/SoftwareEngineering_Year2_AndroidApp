package com.com2027.group03;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.util.Locale;

/**
 * This class is used to get the current location of the device.
 * Created by Matus on 17-Apr-17.
 */
public class LocationService extends Service implements LocationListener {
    private static final String TAG = "LocationService";
    private static LocationService instance = null;
    private Context context = null;
    private LocationManager locationManager;

    public static final int PERMISSION_REQUEST_CODE = 1337;

    /**
     * Provides a way to create and access a single instance
     * @param context The context of the activity class
     * @return Instance of the LocationService
     */
    public static LocationService singleton(Context context){
        if(instance == null){
            instance = new LocationService(context);
        }
        return instance;
    }

    /**
     * Requests permissions from the user (if required)
     * @param activity The context of the activity class
     * @return True if permissions were granted, otherwise false when the permissions were not
     * granted (not yet) but the user will have to decide and the result will be available with
     * onRequestPermissionsResult method inside of activity class
     */
    public static boolean requestPermission(Activity activity){
        // Check if we have gps permissions
        boolean gpsPermission = ContextCompat.checkSelfPermission(
                activity.getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean netPermission = ContextCompat.checkSelfPermission(
                activity.getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        // No permission? Request the user!
        if(!gpsPermission || !netPermission) {
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

        // Return true if either of one permission was granted!
        return netPermission || gpsPermission;
    }

    /**
     * The main constructor
     */
    public LocationService(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Request the location
     * @return Location of the device. Returns null if there was an error or the permissions were
     * not granted.
     */
    public Location getLocation(){

        try {

            boolean gpsPermission = ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;

            boolean netPermission = ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;

            if (!gpsPermission && !netPermission) {
                Log.e(TAG, "Failed to get location! Permissions denied!");
                return null;
            }

            boolean gps = false;
            boolean net = false;

            if (gpsPermission) {
                gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }

            if (netPermission) {
                net = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            if (gps) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d(TAG, "Got location via GPS_PROVIDER: " + location.toString());
                return location;
            }

            if (net) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d(TAG, "Got location via NETWORK_PROVIDER: " + location.toString());
                return location;
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Nothing to do here
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Nothing to do here
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Nothing to do here
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Nothing to do here
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Nothing to do here
        return null;
    }
}
