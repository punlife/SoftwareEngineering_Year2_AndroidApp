package com.com2027.group03;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

/**
 * This class is used to return an ISO code of the country based on the location.
 * For example: GB, DE, RU, CZ
 *
 * Created by Matus on 17-Apr-17.
 */
public class LocationGeocoder {
    private static final String TAG = "LocationGeocoder";
    private Geocoder geocoder;
    private Context context;

    /**
     * Main constructor
     * @param context The context of the activity class
     */
    public LocationGeocoder(Context context){
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.context = context;
    }

    /**
     * Returns a 2-alpha ISO code of the country (example: GB, DE, RU, CZ)
     * @param loc The location of the position to get the country code from
     * @return 2-alpha ISO code of the country as a string
     */
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
