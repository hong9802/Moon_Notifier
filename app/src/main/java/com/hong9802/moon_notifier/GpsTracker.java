package com.hong9802.moon_notifier;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Sourced by https://webnautes.tistory.com/1315
public class GpsTracker extends Service implements LocationListener
{
    private  final Context mContext;
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public GpsTracker(Context mContext)
    {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try
        {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled) {

            } else {
                int hasFineLocaitonPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                if(hasFineLocaitonPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                    return null;
                if(isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled)
                {
                    if(location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if(locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.d("@@@", ""+e.toString());
        }
        return location;
    }
    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }
    public String convertLocation(String address)
    {
        return "s";
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
}
//Sourced by https://webnautes.tistory.com/1315
