package com.oompow.homeboi;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by scottvanderlind on 7/15/15.
 */
public class HBButton {

    public int order;
    public String title;
    public String command;
    public int bgcolor;
    public int text_color;

    public HBButton (JSONObject object) {
        try {
            this.order = object.getInt("order");
            this.title = object.getString("title");
            this.command = object.getString("command");
            this.bgcolor = Color.parseColor(object.getString("bgcolor"));
            this.text_color = Color.parseColor(object.getString("text_color"));

            Log.i("HBButton", "Successfully created HBButton titled " + this.title);
        } catch (Exception e) {
            Log.i("HBButton", "Error parsing JSON object.");
        }
    }

    public static ArrayList<HBButton> fromJson(JSONArray buttonArray) {
        ArrayList<HBButton> buttons = new ArrayList<HBButton>();
        for (int i = 0; i < buttonArray.length(); i++) {
            try {
                buttons.add(new HBButton(buttonArray.getJSONObject(i)));
            } catch (Exception e) {
                Log.e("HBButton", "There was an issue adding a button to the ArrayList");
            }
        }
        return buttons;
    }
}
