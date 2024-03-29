package com.example.omr.SHAREDPREFRENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SHAREDPREF_ANSWER {
    private static SHAREDPREF sharedPrefs;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SHAREDPREF_ANSWER(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("ANSWERS", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SHAREDPREF getInstance(Context context) {

        if (sharedPrefs == null) {
            sharedPrefs = new SHAREDPREF(context.getApplicationContext());
        }
        return sharedPrefs;
    }

    public String getAnswers() {
        return mSharedPreferences.getString("ANSSTRING","false");
    }

    public void setAnswers(String Answer) {
        mSharedPreferencesEditor.putString("ANSSTRING", Answer);
        mSharedPreferencesEditor.commit();
    }
    public void RemoveAnswer(){
        mSharedPreferencesEditor.remove("ANSSTRING");
        mSharedPreferencesEditor.commit();
    }
    public void setsubject(String subject){
        mSharedPreferencesEditor.putString("SUBJECT",subject);
        mSharedPreferencesEditor.commit();
    }
    public String getsubject() {
        return mSharedPreferences.getString("SUBJECT","false");
    }

    public String getautopass() {
        return mSharedPreferences.getString("autopass","NONE");
    }

    public void setautopass(String Answer) {
        mSharedPreferencesEditor.putString("autopass", Answer);
        mSharedPreferencesEditor.commit();
    }


}
