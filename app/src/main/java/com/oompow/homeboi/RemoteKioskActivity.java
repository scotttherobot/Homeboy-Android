package com.oompow.homeboi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class RemoteKioskActivity extends ActionBarActivity {

    SharedPreferences prefs = null;
    ArrayList<HBButton> buttons = new ArrayList<HBButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_kiosk);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        prefs = KioskApplication.getInstance().getPreferences();

        String serveraddr = prefs.getString(KioskApplication.PREF_SERVER_ADDRESS, "");

        Log.d("KioskActivity", "Server address is " + serveraddr);
        if (serveraddr.equals("")) {
            // It looks like we haven't set up a server. Let's pop into settings.
            Intent launchApp = new Intent(this, SettingsActivity.class);
            startActivity(launchApp);
        } else {
            configure();
        }
    }


    private void configure () {
        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();
        final SharedPreferences.Editor editor = prefs.edit();

        API.getConfiguration(new API.configHandler() {
            @Override
            public void onSuccess(JSONObject config) {
                try {
                    // Save our remote settings
                    JSONObject settings = config.getJSONObject("settings");
                    editor.putString(KioskApplication.PREF_DISPLAY_BRIGHTNESS, settings.getString("display_brightness"));
                    editor.putString(KioskApplication.PREF_DISPLAY_SLEEP, settings.getString("display_sleep"));
                    editor.commit();
                    configureDisplaySettings();

                    // And get our buttons
                    buttons = HBButton.fromJson(config.getJSONArray("buttons"));
                    Log.i("KioskActivity", "Success fetching configuration and buttons.");
                    buildDynamicButtons();
                } catch (Exception e) {
                    Log.e("KioskActivity", "Error fetching config! Exception!");
                }
            }

            @Override
            public void onFailure(int statusCode, String response) {
                Log.e("KioskActivity", "Failed to fetch configuration.");
            }
        });
    }

    private void configureDisplaySettings () {
        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();
        // Set the display timeout, defaulting to never
        int timeout = Integer.parseInt(prefs.getString(KioskApplication.PREF_DISPLAY_SLEEP, "0"));
        if (timeout > 0) {
            Log.i("KioskActivity", "Setting screen timeout to " + timeout);
        } else {
            Log.i("KioskActivity", "Screen timeout disabled");
        }
        Log.i("KioskActivity", "TODO: IMPLEMENT SCREEN TIMEOUT!");

        // Set the display brightness, defaulting to 100%
        int brightness = Integer.parseInt(prefs.getString(KioskApplication.PREF_DISPLAY_BRIGHTNESS, "100"));
        Log.i("KioskActivity", "Setting screen brightness to " + brightness);
        Log.i("KioskActivity", "TODO: IMPLEMENT SCREEN BRIGHTNESS!");
    }

    private void buildDynamicButtons () {
        LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.buttonContainer);
        buttonContainer.removeAllViewsInLayout();

        if (!buttons.isEmpty()) {
            for (HBButton b : buttons) {
                Button newButton = new Button(this);
                newButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
                newButton.setText(b.title);
                newButton.setBackgroundColor(b.bgcolor);
                newButton.setTextColor(b.text_color);
                newButton.setTag(R.id.command, b.command);

                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("KioskActivity", "Sending command " + v.getTag(R.id.command));
                        API.sendCommand(v.getTag(R.id.command).toString());
                    }
                });

                buttonContainer.addView(newButton);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remote_kiosk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Launch the settings activity
            Intent launchApp = new Intent(this, SettingsActivity.class);
            startActivity(launchApp);
            return true;
        }

        if (id == R.id.action_exit) {
            // Exit the app.
            finish();
            System.exit(0);
            return true;
        }

        if (id == R.id.action_refresh) {
            // Reload the config.
            configure();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // Override the back button.
    public void onBackPressed () {
    }
}
