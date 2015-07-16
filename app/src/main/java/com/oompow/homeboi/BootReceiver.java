package com.oompow.homeboi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by scottvanderlind on 7/15/15.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launchApp = new Intent(context, RemoteKioskActivity.class);
        launchApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchApp);
    }
}
