package com.com2027.group03;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Matus on 17-Apr-17.
 */
public class LocationGeocoder {
    private static final String TAG = "LocationGeocoder";
    private Geocoder geocoder;
    private Context context;

    public LocationGeocoder(Context context){
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.context = context;
    }

    public String country(Location loc) {
        if(loc == null){
            Log.e(TAG, "Failed to get country code! Location is null!");
            return "";
        }

        this.geocoder = new Geocoder(context, Locale.getDefault());

        try {
            Address addr = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1).get(0);
            return addr.getCountryCode();

        } catch (IOException e){
            Log.e(TAG, e.getMessage());
        }

        return "";
    }
}
