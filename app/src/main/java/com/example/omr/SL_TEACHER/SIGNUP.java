package com.example.omr.SL_TEACHER;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.omr.DATABASE.todatabase;
import com.example.omr.R;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SIGNUP extends AppCompatActivity {
    //Variable Initialization
    private EditText name, edepartment;
    boolean age_match;
    Button signup,profile_pic;
    private TextView dob;
    int Year=0;
    private static Calendar cal;
    private static final String TAG = "MainA";
    DatabaseReference ref;
    private String dobm,EMAIL;
    Uri Imageuri;
    private int error=0;
    CUSTOM_TOAST toast;
    Bundle bundle;
    private String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Layout Assignment
        setContentView(R.layout.activity_signup);
        bundle = getIntent().getExtras();
        name = findViewById(R.id.name);
        edepartment = findViewById(R.id.edu_name);
        profile_pic=findViewById(R.id.profile_pic);
        dob = findViewById(R.id.dob);
        signup=findViewById(R.id.signup);
        toast=new CUSTOM_TOAST(getApplicationContext()); //For Toast Messages
        //Function Call
        profile_pic.setOnClickListener(this::imagepicker);
        dob.setOnClickListener(this::datepicker);
        signup.setOnClickListener(this::evaluate);
        Userid=bundle.getString("USERID");
        EMAIL=bundle.getString("EMAIL");
        Log.d("USERID_CHECK", "onCreate: "+Userid);
        ref= FirebaseDatabase.getInstance().getReference("SIGINING_INFO"); //Database Reference
    }

    // Fields Evaluation
    private void evaluate(View v) {
        error=0;
        String nameg = name.getText().toString().trim().toUpperCase();
        String edu_departmentg = edepartment.getText().toString().toUpperCase();
        if (TextUtils.isEmpty(nameg)) {
            name.setError("You Must Enter Name");
            name.requestFocus();
            error++;
        }
        if(!TextUtils.isEmpty(nameg)){
            if(!nameg.matches("[a-zA-Z ]+")){
                error++;
                name.setError("Name must not contain digits");
            }
        }
        if (TextUtils.isEmpty(edu_departmentg)) {
            edepartment.setError("You Must Enter Department Name");
            edepartment.requestFocus();
            error++;
        }

        cal=Calendar.getInstance();
        int age=cal.get(Calendar.YEAR)-Year;
        Log.d(TAG, "evaluate: "+age);
        if(age>18&&age<70){
            age_match=true;
        }
        if(age<18){
            error++;
            toast.ToastWarning("AGE REQUIRED 18+");
        }
        if(error==0){
            Log.d("VERIFYEMAIL", "evaluate: ");
            todatabase td=new todatabase(ref,getApplicationContext()); //Load Current Class Context
            ProgressDialog progressDialog=new ProgressDialog(this);
            td.addValueSignup(nameg,edu_departmentg,dobm,Imageuri,progressDialog,Userid,EMAIL);  //Add Values to Database

        }


    }
    // CODE for Date Picker
    public void datepicker(View v) {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                SIGNUP.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        Year=year;
                        Log.d(TAG, "onDateSet: "+Year);
                        dobm=month + "/" + day + "/" + year;
                        dob.setText(dobm);
                    }
                },year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //Code to get image from gallery
    public void imagepicker(View v){
        Intent gallery= new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select Profile Picture"),1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode == RESULT_OK && (!data.equals(null)) && (!data.getData().equals(null))){
            Imageuri=data.getData();
        }
        else{
            error++;
        }
    }

    //To Check whether Username Already Exist
  /* public void alreadyexist(View v){
        String user=username.getText().toString().trim().toUpperCase();
        ref.orderByChild("username").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username.setError("USERNAME ALREADY EXIST");
                    username.requestFocus();
                    toast.ToastWarning("USERNAME ALREADY EXIST");
                }
                else{
                    evaluate(v);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,LOGIN.class);
        startActivity(intent);
    }
}
