package com.example.omr.SHAREDPREFRENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SHAREDPREFRENCES_SWITCH {
    private static SHAREDPREF sharedPrefs;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SHAREDPREFRENCES_SWITCH(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("SWITCH_STATUS", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SHAREDPREF getInstance(Context context) {

        if (sharedPrefs == null) {
            sharedPrefs = new SHAREDPREF(context.getApplicationContext());
        }
        return sharedPrefs;
    }

    public String gettheme() {
        return mSharedPreferences.getString("theme","lite");
    }

    public void settheme(String theme) {
        mSharedPreferencesEditor.putString("theme", theme);
        mSharedPreferencesEditor.commit();
    }

    public void setstyle(String type) {
        mSharedPreferencesEditor.putString("type", type);
        mSharedPreferencesEditor.commit();
    }

    public String getstyle() {
        return mSharedPreferences.getString("type", "list");
    }


}
