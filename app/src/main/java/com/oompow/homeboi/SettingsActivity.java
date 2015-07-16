package com.oompow.homeboi;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText server = (EditText)findViewById(R.id.serverAddress);
        EditText remote = (EditText)findViewById(R.id.remoteId);
        SharedPreferences prefs = KioskApplication.getInstance().getPreferences();

        String serveraddr = prefs.getString(KioskApplication.PREF_SERVER_ADDRESS, "");
        Log.d("SETTINGS", "Got saved server address " + serveraddr);

        server.setText(serveraddr);
        remote.setText(prefs.getString(KioskApplication.PREF_REMOTE_ID, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveClicked(View v) {
        EditText server = (EditText)findViewById(R.id.serverAddress);
        EditText remote = (EditText)findViewById(R.id.remoteId);
        SharedPreferences.Editor editor = KioskApplication.getInstance().getPreferences().edit();

        editor.putString(KioskApplication.PREF_SERVER_ADDRESS, server.getText().toString());
        editor.putString(KioskApplication.PREF_REMOTE_ID, remote.getText().toString());
        editor.commit();

        Toast toast = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
