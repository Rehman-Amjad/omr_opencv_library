package com.example.omr.SETTINGS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.omr.DASHBOARD;
import com.example.omr.MainActivity;
import com.example.omr.OMR_FUNCTION.Util;
import com.example.omr.R;
import com.example.omr.SETTER_G.TEACHER;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREFRENCES_SWITCH;
import com.example.omr.SL_STUDENT.CustomToast;
import com.example.omr.SL_STUDENT.Utils;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SETTING extends AppCompatActivity {
    private Switch theme_change,style_change;
    private Button password_change,Change_name,Change_profile;
    private SHAREDPREFRENCES_SWITCH sharedprefrences_switch;
    private String theme_status,style_status;
    EditText oldpassword,newpassword,confirmpassword;
    CUSTOM_TOAST toast;
    private Uri Imageuri;
    private DatabaseReference ref;
    private Bitmap bitmap;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] cataimage;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedprefrences_switch=new SHAREDPREFRENCES_SWITCH(getApplicationContext());
        Change_profile=findViewById(R.id.change_picture_btn);
        theme_change=findViewById(R.id.theme_change);
        style_change=findViewById(R.id.Style_Change);
        Change_name=findViewById(R.id.change_name_btn);
        password_change=findViewById(R.id.change_password_btn);
        theme_status=sharedprefrences_switch.gettheme();
        style_status=sharedprefrences_switch.getstyle();
        toast=new CUSTOM_TOAST(SETTING.this);
        ref= FirebaseDatabase.getInstance().getReference("SIGINING_INFO");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //function calling
        setstyleswitch();
        setthemeswitch();
        theme_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d("THEME", "onCheckedChanged: "+theme_status+"  is notchecked");
                    sharedprefrences_switch.settheme("lite");
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {

                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d("THEME", "onCheckedChanged: "+theme_status+"  is checked");
                    sharedprefrences_switch.settheme("dark");
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            }
        });
        style_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedprefrences_switch.setstyle("grid");
                }
                else{
                    sharedprefrences_switch.setstyle("list");
                }
            }
        });

        password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_CHANGE_password();
            }
        });
        Change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changename_dialog();
            }
        });
        Change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepicker();
            }
        });

    }
    public void setthemeswitch(){
        if(theme_status.equals("dark")){
            theme_change.setChecked(true);
        }
        else{
            theme_change.setChecked(false);
        }
    }
    public void setstyleswitch(){
        if(style_status.equals("grid")){
            style_change.setChecked(true);
        }
        else {
            style_change.setChecked(false);
        }
    }
    private void Dialog_CHANGE_password(){
        final AlertDialog.Builder alert=new AlertDialog.Builder(SETTING.this);
        View mview=getLayoutInflater().inflate(R.layout.dialogbox_passwordchange,null);


        final Button cancel,done;
        oldpassword=mview.findViewById(R.id.cng_oldpassword);
        newpassword=mview.findViewById(R.id.cng_newpassword);
        confirmpassword=mview.findViewById(R.id.cng_confirmpassword);
        cancel = mview.findViewById(R.id.cancel_button_cng);
        done = mview.findViewById(R.id.done_button_cng);
        alert.setView(mview);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
       // Log.d("PASSWORD_CHANGE", "Dialog_CHANGE_password: "+getoldpassword);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getoldpassword=oldpassword.getText().toString();
                String getnewpassword=newpassword.getText().toString();
                String getconfirmpassword=confirmpassword.getText().toString();
                int error=0;
                if(TextUtils.isEmpty(getoldpassword)){
                    oldpassword.setError("Must not be Empty");
                    error++;
                }
                if(TextUtils.isEmpty(getnewpassword)){
                    newpassword.setError("Must not be Empty");
                    error++;
                }
                if(TextUtils.isEmpty(getconfirmpassword)){
                    confirmpassword.setError("Must not be Empty");
                    error++;
                }
                if(!getnewpassword.equals(getconfirmpassword)){
                    ;
                    confirmpassword.setError("Password not Matched");
                    error++;
                }
                if(getnewpassword.length() < 6 || getconfirmpassword.length() < 6){
                    newpassword.setError("Password must contain 6 or more digits");
                    confirmpassword.setError("Password must contain 6 or more digits");
                    error++;
                }
                if(error==0){
                    Changepassword();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }
    private void Changepassword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassword.getText().toString());
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                toast.ToastSuccess("Password Changed Succesfully");
                            }
                            else {
                                toast.ToastError("Error Occurrred while changing");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast.ToastError(e.getMessage());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast.ToastError(e.getMessage());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast.ToastError(e.getMessage());
            }
        });
    }
    private void changename_dialog(){

        final AlertDialog.Builder alert=new AlertDialog.Builder(SETTING.this);
        View mview=getLayoutInflater().inflate(R.layout.change_name_layout,null);

        final Button canel,done;
        final EditText fullname;
        canel=mview.findViewById(R.id.cancel_button_name);
        done=mview.findViewById(R.id.done_button_name);
        fullname=mview.findViewById(R.id.changed_fullname);

        alert.setView(mview);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getname=fullname.getText().toString().trim();
                int error=0;
                if(TextUtils.isEmpty(getname)){
                    error++;
                    fullname.setError("Must not be Empty");
                }
                if(getname.length()< 6){
                    error++;
                    fullname.setError("Length must be Larger than 5");
                }
                if(!getname.matches("[a-zA-Z ]+")){
                    error++;
                    fullname.setError("Name must not contain digits");
                }
                if(error==0){
                    done.setClickable(false);
                    changename(getname);
                    alertDialog.dismiss();
                }
            }
        });
         alertDialog.show();




    }
    private void changename(String name){
        toast.ToastInfo("PROCESSING");
        String user=FirebaseAuth.getInstance().getUid();
        ref.child(user).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    toast.ToastSuccess("NAME CHANGED SUCCESSFULLY");}
                else{
                    toast.ToastError("ERROR OCCURED WHILE CHANGING THE NAME");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast.ToastError(e.getMessage());
            }
        });

    }
    public void imagepicker(){
        Intent gallery= new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select Profile Picture"),1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            Imageuri=data.getData();
            verify_dialog();
        }
        else{
            toast.ToastError("ERROR OCCURED");

        }
    }
    public void verify_dialog(){
        new AlertDialog.Builder(this)
                .setTitle("SURE ?")
                .setMessage("REALLY WANT TO CHANGE YOUR PROFILE PICTURE")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(Imageuri!=null){
                            changeprofilepic();

                        }
                        else{
                            toast.ToastError("ERROR OCCURED WHILE CHANGING PICTURE");
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("CANCEL", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void changeprofilepic() {
        String key=FirebaseAuth.getInstance().getUid();
        ProgressDialog progressDialog=new ProgressDialog(SETTING.this);
        progressDialog.setMax(100);
        progressDialog.setTitle("CHANGING PROFILE PICTURE");
        progressDialog.setMessage("PLEASE WAIT...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setProgress(20);
        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),Imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Changing the bits of Image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        cataimage = baos.toByteArray();  //Converting image to bytes
        String location="PROFILE/"+key+".jpg";    // Location of Profile Image in Database;
        final StorageReference riverref=mStorageRef.child(location);    // Getting reference of Image Storage
        progressDialog.setProgress(40);
        riverref.putBytes(cataimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setProgress(80);
                toast.ToastSuccess("PROFILE PICTURE UPDATED SUCCESFULLY");
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                toast.ToastError(e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SETTING.this, DASHBOARD.class);
        startActivity(intent);
    }
}
