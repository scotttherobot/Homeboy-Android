package com.oompow.homeboi;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by scottvanderlind on 7/16/15.
 */
public class KioskService extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2);
    private static final String TAG = KioskService.class.getSimpleName();

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    @Override
    public void onDestroy () {
        Log.i(TAG, "Stopping KioskService");
        running = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting KioskService");
        running = true;
        ctx = this;

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (Exception e) {
                        Log.i(TAG, "KioskService thread interrupted");
                    }
                } while (running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    private void handleKioskMode() {
        if (kioskModeEnabled() && isInBackground()) {
            restoreApp();
        }
    }

    private boolean isInBackground () {
        ActivityManager aman = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskinfo = aman.getRunningTasks(1);
        ComponentName info = taskinfo.get(0).topActivity;
        return !ctx.getApplicationContext().getPackageName().equals(info.getPackageName());
    }

    private void restoreApp () {
        Intent newApp = new Intent(ctx, RemoteKioskActivity.class);
        newApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(newApp);
    }

    private boolean kioskModeEnabled () {
        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();
        return prefs.getBoolean(KioskApplication.PREF_KIOSK_MODE, false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

