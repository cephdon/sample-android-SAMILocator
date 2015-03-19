package com.samsung.locator;

import android.location.Location;

public interface LocationEvents {
    public interface Listener {
        public void onNewLocation(Location location, long ts);
    }

    public void start(Listener listener);

    public void stop();

}