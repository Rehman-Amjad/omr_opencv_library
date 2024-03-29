package com.example.omr.SHAREDPREFRENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SHAREDPREF {
    private static SHAREDPREF sharedPrefs;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SHAREDPREF(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("SIGNIN", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SHAREDPREF getInstance(Context context) {

        if (sharedPrefs == null) {
            sharedPrefs = new SHAREDPREF(context.getApplicationContext());
        }
        return sharedPrefs;
    }

    public String getUsername() {
        return mSharedPreferences.getString("preusername","false");
    }

    public void setUsername(String Username) {
        mSharedPreferencesEditor.putString("preusername", Username);
        mSharedPreferencesEditor.commit();
    }
    public void RemoveUsername(){
        mSharedPreferencesEditor.remove("preusername");
        mSharedPreferencesEditor.commit();
    }

    public void setViewType(String type) {
        mSharedPreferencesEditor.putString("type", type);
        mSharedPreferencesEditor.commit();
    }

    public String getViewType() {
        return mSharedPreferences.getString("type", "list");
    }

    public String getRollno() {
        return mSharedPreferences.getString("rollno_id","false");
    }

    public void setRollno(String Rollno) {
        mSharedPreferencesEditor.putString("rollno_id", Rollno);
        mSharedPreferencesEditor.commit();
    }
    public void RemoveRollno(){
        mSharedPreferencesEditor.remove("rollno_id");
        mSharedPreferencesEditor.commit();
    }


}
