package com.oompow.homeboi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;

/**
 * Created by scottvanderlind on 7/16/15.
 */
public class OnScreenOffReceiver extends BroadcastReceiver {

    /**
     * Someone tried to press the power button. FIX IT!
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            KioskApplication app = (KioskApplication) context.getApplicationContext();
            if (kioskModeEnabled(app)) {
                wakeUpDevice(app);
            }
        }
    }

    /**
     * Wake up the device!!!
     * @param app
     */
    private void wakeUpDevice(KioskApplication app) {
        PowerManager.WakeLock wakeLock = app.getWakeLock();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        wakeLock.acquire();
        wakeLock.release();
    }

    /**
     * See if we are in kiosk mode.
     * @param app
     * @return
     */
    private boolean kioskModeEnabled(KioskApplication app) {
        SharedPreferences prefs = app.getPreferences();
        return prefs.getBoolean(KioskApplication.PREF_KIOSK_MODE, false);
    }

}