package com.com2027.group03;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Matus on 17-Apr-17.
 */
public class TestLocationService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get instance to our location service
        LocationService locationService = LocationService.singleton(this);

        // Check if permissions were granted
        if(locationService.requestPermission(this)){

            // If this passes, the permissions were already granted previously for this application

            // Get location and country code
            Location loc = locationService.getLocation();
            Log.w("MainActivity", "Got location: " + new LocationGeocoder(this).country(loc));
        } else {
            // If this fails, you need to check for PERMISSION_REQUEST_CODE code inside
            // of onRequestPermissionsResult method.

            // The locationService.requestPermission will request the user to grant or deny permissions
            // outside of this activity thread! It is a non-blocking function!
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            // Check for LocationService code
            case LocationService.PERMISSION_REQUEST_CODE: {

                // Check if permission was granted (the array will be non-empty)
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Get instance to our location service
                    LocationService locationService = LocationService.singleton(this);

                    if(locationService.requestPermission(this)){

                        // Get location and country code
                        Location loc = locationService.getLocation();
                        Log.w("MainActivity", "Got location via permission request: " + new LocationGeocoder(this).country(loc));
                    }
                } else {

                }
                break;
            }
        }
    }
}
