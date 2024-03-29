package com.example.omr;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.omr.SHAREDPREFRENCES.SHAREDPREFRENCES_SWITCH;
import com.example.omr.SL_STUDENT.MainActivity;
import com.example.omr.SL_TEACHER.LOGIN;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LOGIN_SELECTION extends AppCompatActivity {

    ImageView teacher_image,student_image;
    TextView tvteacher,tvstudent,teacher_t,student_t;
    SHAREDPREFRENCES_SWITCH sharedprefrences_switch;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;
    String Theme_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__selection);
        checkAndRequestPermissions();
        teacher_image=findViewById(R.id.Teacher_Select);
        student_image=findViewById(R.id.Student_Select);
        tvstudent=findViewById(R.id.tvstudent_select);
        tvteacher=findViewById(R.id.tvteacher_select);
        sharedprefrences_switch=new SHAREDPREFRENCES_SWITCH(getApplicationContext());
        Theme_status=sharedprefrences_switch.gettheme();
        setThemesetyle();
        Log.d("NAINA", "onCreate: ");
        Picasso.get()
                .load(R.drawable.student_select)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(student_image);
        Picasso.get()
                .load(R.drawable.teacher_select)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(teacher_image);
       teacher_t=findViewById(R.id.teacher_t);
       student_t=findViewById(R.id.student_t);
       tvstudent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
               tvstudent.startAnimation(anim);
               Student();

           }
       });
       tvteacher.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
               tvteacher.startAnimation(anim);
               Teacher();
           }
       });
    }
    private void Teacher(){
        Thread background=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(500);
                    Intent intent=new Intent(getApplicationContext(), LOGIN.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
    private void Student(){
        Thread background=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(500);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

    }
    private void setThemesetyle(){
        if(Theme_status.equals("dark")){
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            // code for lollipop and pre-lollipop devices
        }

    }
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(LOGIN_SELECTION.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(LOGIN_SELECTION.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(LOGIN_SELECTION.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(LOGIN_SELECTION.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(LOGIN_SELECTION.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(LOGIN_SELECTION.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(LOGIN_SELECTION.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(LOGIN_SELECTION.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LOGIN_SELECTION.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}
