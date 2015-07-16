package com.oompow.homeboi;

import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by scottvanderlind on 7/15/15.
 */
public class API {

    public interface configHandler {
        public void onSuccess(JSONObject config);
        public void onFailure(int statusCode, String response);
    }
    private static configHandler callback;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getConfiguration (configHandler whenDone) {
        callback = whenDone;

        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();
        final SharedPreferences.Editor editor = prefs.edit();
        String remote_id = prefs.getString(KioskApplication.PREF_REMOTE_ID, "");

        API.get("/remote/" + remote_id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("API", "Response " + response.toString());

                try {
                    callback.onSuccess(response);
                    Log.i("API", "Fetched configuration.");
                } catch (Exception e) {
                    Log.i("API", "Error fetching configuration");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                Log.e("API", "Failed to fetch configuration.");
                callback.onFailure(statusCode, response);
            }
        });
    }

    public static void sendCommand (String command) {
        API.post("/command/" + command, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("API", "Sent command : " + response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                Log.e("API", "Failed to send command.");
            }
        });
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();
        String base_url = prefs.getString(KioskApplication.PREF_SERVER_ADDRESS, "");
        return base_url + relativeUrl;
    }
}
