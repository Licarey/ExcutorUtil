package com.example.liming.validateproject.GestureLock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liming on 2017/11/23.
 * email liming@finupgroup.com
 */

public class GesturePreference {
    private Context context;
    private final String fileName = "com.example.liming.gesturelock.filename";
    private String nameTable = "com.example.liming.gesturelock.nameTable";

    public GesturePreference(Context context, int nameTableId) {
        this.context = context;
        if (nameTableId != -1)
            this.nameTable = nameTable + nameTableId;
    }

    public void WriteStringPreference(String data) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nameTable, data);
        editor.apply();
    }

    public String ReadStringPreference() {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getString(nameTable, "null");
    }
}
