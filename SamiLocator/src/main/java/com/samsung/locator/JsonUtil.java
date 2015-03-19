package com.samsung.locator;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ari
 */
public class JsonUtil {
    private static final String SOURCE_DEVICE = "sdid";
    private static final String DATA = "data";
    private static final String TYPE = "type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lng";

    /**
     * Returns JSON payload to send a SAMI registration message
     * @param sdid
     * @param accessToken
     * @return
     */
    public static String getRegisterMessage(String sdid, String accessToken){
        JSONObject message = new JSONObject();
        try {
            message.put(TYPE, "register");
            message.put(SOURCE_DEVICE, sdid);
            message.put(AUTHORIZATION, "bearer " + accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message.toString();
    }

    /**
     * Returns a full JSON payload to send a message for DemoGPS device type
     * @param sdid
     * @param location
     * @return
     */
    public static String getDeviceMessage(String sdid, Location location){
        JSONObject message = new JSONObject();
        try {
            message.put(SOURCE_DEVICE, sdid);
            JSONObject data = new JSONObject();
            data.put(LATITUDE, location.getLatitude());
            data.put(LONGITUDE, location.getLongitude());
            message.put(DATA, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message.toString();
    }
}
