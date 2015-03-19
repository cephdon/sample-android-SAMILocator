package com.samsung.locator;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationWatcher implements LocationEvents, LocationEvents.Listener {

    private static final long MAX_NOTIFY_INTERVAL = 5 * 60 * 1000; // 5 minutes
    private boolean isRunning;
    private Locator gps;
    private Locator net;
    private Listener listener;
    private Location lastLoc;
    private long lastTime;

    public LocationWatcher(Context context) {
        gps = new Locator(context, LocationManager.GPS_PROVIDER);
        net = new Locator(context, LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Start GPS and network providers
     */
    public void start(Listener update) {
        if(isRunning){
            return;
        }
        gps.start(this);
        net.start(this);
        isRunning = true;
        listener = update;
    }

    /**
     * Stop all providers
     */
    public void stop(){
        if(isRunning){
            gps.stop();
            net.stop();
            isRunning = false;
            listener = null;
        }
    }


    /**
     * Updates the listener if:
     * 1. First location
     * 2. Location is reported by a different provider than last location
     * 3. Location is being reported by GPS
     * 4. Time elapsed is more than max notify interval
     * @param location
     * @param ts
     */
    public void onNewLocation(Location location, long ts) {
        if((lastLoc == null)
            || (lastLoc != null && lastLoc.getProvider().equals(location.getProvider()))
            || (location.getProvider().equals(LocationManager.GPS_PROVIDER))
            || (ts - lastTime > MAX_NOTIFY_INTERVAL)) {
            lastLoc = location;
            lastTime = ts;
            if(listener != null){
                listener.onNewLocation(location, ts);
            }
        }
	}
}