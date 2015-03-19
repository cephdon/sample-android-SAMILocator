package com.samsung.locator;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

/*
Copyright (c) 2014 Samsung Electronics Co., Ltd.
*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
*
http://www.apache.org/licenses/LICENSE-2.0
*
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
public class SamiActivity extends Activity {
    private Websocket websocket, live;
    private Handler handler;
    private TextView txtLive, txtWebsocket;
    private WebView webView;
    private String lastLat = "", lastLng = "";
    private LocationWatcher locationWatcher;

    /**
     * Setup UI, start available location providers and connect to SAMI
     * @param savedInstanceState
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sami);
        if(Config.ACCESS_TOKEN == null || Config.DEVICE_ID == null){
            Toast.makeText(this, R.string.configure, Toast.LENGTH_LONG).show();
            return;
        }
        handler = new Handler();

        webView = (WebView) findViewById(R.id.webView);
        Config.setWebViewSettings(webView);

        locationWatcher = new LocationWatcher(this);
        locationWatcher.start(new LocationEvents.Listener() {
			
			@Override
			public void onNewLocation(final Location location, long ts) {
				//Avoid blocking here, UI and network jobs ahead
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() { connectWebsocketAndSendLocation(location); }
                }, 10);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectLiveWebsocket();
                    }
                }, 10);
			}
		});
	}

    /**
     * Disconnect and free resources
     */
    @Override
    protected void onDestroy() {
        locationWatcher.stop();
        if (handler != null)
            handler = null;
        disconnect(live);
        disconnect(websocket);
        super.onDestroy();
    }

    /**
     * Makes sure the websocket connects to /live on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        connectLiveWebsocket();
    }

    /**
     * Connects to /websocket if necessary, registers to SAMI and send the lat,lng
     */
    private void connectWebsocketAndSendLocation(final Location location){
        final String messageLatLng = JsonUtil.getDeviceMessage(Config.DEVICE_ID, location);

        setWebsocketText(("Sending to SAMI:" + messageLatLng));

        if(websocket == null) {
            websocket = new Websocket();
        }
        if(websocket.isConnected()) {
            websocket.send(messageLatLng);
        }
        else {
            if(!websocket.isConnecting()) {
                websocket.connect(Config.WEBSOCKET_URL, new WebsocketEvents() {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        websocket.send(JsonUtil.getRegisterMessage(Config.DEVICE_ID, Config.ACCESS_TOKEN));
                        websocket.send(messageLatLng);
                    }

                    @Override
                    public void onMessage(String message) {
                        setWebsocketText(("onMessage(): "+ message));
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        setWebsocketText(("onClose(): "+ code));
                    }

                    @Override
                    public void onError(Exception ex) {
                        setWebsocketText(("onError(): "+ ex.getMessage()));
                    }
                });
            }
        }
    }

    /**
     * Connects to /live websocket if necessary
     */
    private void connectLiveWebsocket(){
        if(live == null) {
            live = new Websocket();
        }
        if(!live.isConnected() && !live.isConnecting()) {
            live.connect(Config.LIVE_URL, new WebsocketEvents() {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    setLiveText("Live connected to from SAMI!");
                }

                @Override
                public void onMessage(String message) {
                    setLiveText("Live onMessage(): " + message);
                    try {
                        JSONObject json = new JSONObject(message);
                        JSONObject dataNode = json.optJSONObject("data");
                        if(dataNode != null) {
                            lastLat = dataNode.getString("lat");
                            lastLng = dataNode.getString("lng");
                        }
                    } catch (JSONException e) {
                        // This message doesn't contain data node, might be a ping.
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    setLiveText("Live closed: " + code);
                }

                @Override
                public void onError(Exception ex) {
                    setLiveText("Live error: " + ex.getMessage());
                }
            });
        }
    }

    /**
     * Closes a websocket connection
     * @param pipe
     */
    private void disconnect(Websocket pipe){
        if (pipe != null && pipe.isConnected()){
            pipe.disconnect();
            pipe = null;
        }
    }

    /**
     * Updates the textview for /websocket
     * @param text
     */
    void setWebsocketText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(txtWebsocket == null)
                    txtWebsocket = (TextView) findViewById(R.id.txtWebsocket);
                txtWebsocket.setText(text);
            }
        });
    }

    /**
     * Updates the textview for /live websocket
     * @param text
     */
    void setLiveText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(txtLive == null)
                    txtLive = (TextView) findViewById(R.id.txtLive);
                txtLive.setText(text);
            }
        });
    }

    /**
     * Called by button that places a marker on the latest position received from /live
     * @param v
     */
    public void whereIsThis(View v){
        String url = Config.MAPS_URL + lastLat + "," + lastLng;
            if(lastLat.length() > 0 && lastLng.length() > 0){
                try {
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Maybe show a "No location available yet" sign.
        }
    }

    /**
     * Called by button that display latest 100 events with a polyline on a map
     * @param view
     */
    public void showMyRoute(View view){
        String url = "file:///android_asset/locator.html";
        try {
            webView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
