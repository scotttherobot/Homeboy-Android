package com.oompow.homeboi;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by scottvanderlind on 7/15/15.
 */
public class KioskApplication extends Application {

    private static KioskApplication _instance;

    public static final String PREF_KEY = "pref_key_homeboi";
    public static final String PREF_SERVER_ADDRESS = "pref_server_address";
    public static final String PREF_REMOTE_ID = "pref_remote_id";
    public static final String PREF_DISPLAY_SLEEP = "pref_display_sleep";
    public static final String PREF_DISPLAY_BRIGHTNESS = "pref_display_brightness";

    public KioskApplication () {
        super();
    }

    public static KioskApplication getInstance () {
        return _instance;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        _instance = this;
    }

    public SharedPreferences getPreferences () {
        return getApplicationContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

}
