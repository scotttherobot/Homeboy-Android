package com.oompow.homeboi;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by scottvanderlind on 7/15/15.
 */
public class KioskApplication extends Application {

    private static KioskApplication _instance;
    private PowerManager.WakeLock wakeLock;

    public static final String PREF_KEY = "pref_key_homeboi";
    public static final String PREF_SERVER_ADDRESS = "pref_server_address";
    public static final String PREF_REMOTE_ID = "pref_remote_id";
    public static final String PREF_DISPLAY_SLEEP = "pref_display_sleep";
    public static final String PREF_DISPLAY_BRIGHTNESS = "pref_display_brightness";
    public static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

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
        registerKioskModeScreenOffReceiver();
        startKioskService();
    }

    public SharedPreferences getPreferences () {
        return getApplicationContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    private void startKioskService () {
        startService(new Intent(this, KioskService.class));
    }

    private void registerKioskModeScreenOffReceiver () {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        OnScreenOffReceiver onScreenOffReceiver = new OnScreenOffReceiver();
        registerReceiver(onScreenOffReceiver, filter);
    }

    public PowerManager.WakeLock getWakeLock () {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wake up");
        }
        return wakeLock;
    }

}
