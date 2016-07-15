package com.breakdhack.singweatherlah.GeoLocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Divakar on 13/g/16.
 */
public class MyLocationListener implements LocationListener {

    public static double lonData, latData;
    public static Location myLocation;

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            latData = location.getLatitude();
            lonData = location.getLongitude();
            myLocation = location;

        }

    }

    public Location getMyLocation(){
        return myLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
