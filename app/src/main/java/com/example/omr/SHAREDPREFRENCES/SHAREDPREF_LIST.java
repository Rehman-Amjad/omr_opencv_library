package com.example.omr.SHAREDPREFRENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SHAREDPREF_LIST {
    private static SHAREDPREF sharedPrefs;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SHAREDPREF_LIST(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("LIST", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SHAREDPREF getInstance(Context context) {

        if (sharedPrefs == null) {
            sharedPrefs = new SHAREDPREF(context.getApplicationContext());
        }
        return sharedPrefs;
    }

    public String getRollno() {
        return mSharedPreferences.getString("rollno","false");
    }

    public void setRollno(String Username) {
        mSharedPreferencesEditor.putString("rollno", Username);
        mSharedPreferencesEditor.commit();
    }
    public void RemoveRollno(){
        mSharedPreferencesEditor.remove("rollno");
        mSharedPreferencesEditor.commit();
    }
    public String getClassname() {
        return mSharedPreferences.getString("cname","false");
    }

    public void setClassname(String Username) {
        mSharedPreferencesEditor.putString("cname", Username);
        mSharedPreferencesEditor.commit();
    }
    public void RemoveClassname(){
        mSharedPreferencesEditor.remove("cname");
        mSharedPreferencesEditor.commit();
    }
    public String getuser() {
        return mSharedPreferences.getString("user","false");
    }

    public void setuser(String user) {
        mSharedPreferencesEditor.putString("user", user);
        mSharedPreferencesEditor.commit();
    }
    public String get_Student_rollno() {
        return mSharedPreferences.getString("student_rollno","false");
    }

    public void set_Student_rollno(String rollno) {
        mSharedPreferencesEditor.putString("student_rollno", rollno);
        mSharedPreferencesEditor.commit();
    }
    public String get_student_Classname() {
        return mSharedPreferences.getString("cname_student","false");
    }

    public void set_student_Classname(String classname) {
        mSharedPreferencesEditor.putString("cname_student", classname);
        mSharedPreferencesEditor.commit();
    }

}
