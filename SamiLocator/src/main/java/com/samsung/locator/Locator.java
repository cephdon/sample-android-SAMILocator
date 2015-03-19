package com.samsung.locator;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Locator implements LocationListener, LocationEvents {

    private static final long MIN_DISTANCE = 10; // meters
    private static final long MIN_TIME = 1000 * 60; //milliseconds
    private LocationManager locationManager;
    private String provider;
    private boolean isRunning;
    private Listener listener;

    public Locator(Context context, String provider) {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        this.provider = provider;
    }

    /**
     * Starts watching for location updates
     * @param update
     */
    public void start(Listener update) {
        if(isRunning){
            return;
        }
        isRunning = true;
        try {
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return;
        }
        listener = update;
    }

    /**
     * Stops the location update watcher
     */
    public void stop(){
        if(isRunning){
            locationManager.removeUpdates(this);
            isRunning = false;
            listener = null;
        }
    }

    /**
     * Notifies the listener when the OS notifies of location changes
     * @param location
     */
    public void onLocationChanged(Location location) {
        long now = System.currentTimeMillis();
        if(listener != null){
            listener.onNewLocation(location, now);
        }
    }

    public void onProviderDisabled(String arg0) {

    }

    public void onProviderEnabled(String arg0) {

    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}
