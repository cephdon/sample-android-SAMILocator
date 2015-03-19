package com.samsung.locator;

import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ari
 */
public class Config {
    public static final String DEVICE_ID = null;
    public static final String ACCESS_TOKEN = null;
    public static final String WEBSOCKET_URL = "wss://api.samsungsami.io/v1.1/websocket?ack=true";
    public static final String LIVE_URL = "wss://api.samsungsami.io/v1.1/live?sdid="+ DEVICE_ID +"&Authorization=bearer+"+ACCESS_TOKEN;
    public static final String MAPS_URL = "http://www.google.com/maps?q=";

    /**
     * Creates a proper environment for the webview
     * @param webView
     */
    public static void setWebViewSettings(WebView webView){
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "jsinterface");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

    }

    /**
     * Interface to provide information from Java to Javascript
     */
    static final class JavaScriptInterface {
        JavaScriptInterface () { }
        @JavascriptInterface
        public String getAccessToken() {
            return ACCESS_TOKEN;
        }
        @JavascriptInterface
        public String getDeviceId() {
            return DEVICE_ID;
        }
    }

}
