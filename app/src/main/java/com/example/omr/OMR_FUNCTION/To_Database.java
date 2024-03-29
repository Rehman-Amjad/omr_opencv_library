package com.example.omr.OMR_FUNCTION;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.omr.SETTER_G.OMR;
import com.example.omr.SL_TEACHER.LOGIN;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.Subject;

public class To_Database {
    private DatabaseReference ref;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private Uri uri;
    private byte[] cataimage;
    private Context cont;
    private CUSTOM_TOAST toast;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private String classname,rollno,subject,marks_obtained,total_marks;
    public To_Database(Context context, String Rollno, String Classname, Uri Imageuri, String subject,String marks_obtained,String total_marks){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.cont=context;
        this.classname=Classname;
        this.rollno=Rollno;
        this.uri=Imageuri;
        this.subject=subject;
        this.total_marks=total_marks;
        this.marks_obtained=marks_obtained;
        toast=new CUSTOM_TOAST(cont);
    }
    public void upload(ProgressDialog progressDialog){
        ref= FirebaseDatabase.getInstance().getReference("Students").child(classname).child(rollno).child("Subject").child(subject);
        Log.d("TESTJJ", "upload: outside");
        progressDialog.setMax(100);
        progressDialog.setTitle("SUBMITTING ANSWER");
        progressDialog.setMessage("PLEASE WAIT...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        final Boolean[] imageupload = {false};
        Log.d("TESTJJ", "upload: outside2");
        try {
            bitmap= MediaStore.Images.Media.getBitmap(cont.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TESTJJ", "upload: outside3");
        progressDialog.show();
        Log.d("TESTJJ", "upload: outside4");
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        cataimage = baos.toByteArray();  //Converting image to bytes
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String datetime = simpleDateFormat.format(date);
        String location="OMR/"+classname+"/"+subject+"/"+rollno+"/"+datetime+".jpg";
        final StorageReference riverref=mStorageRef.child(location);    // Getting reference of Image Storage
        progressDialog.incrementProgressBy(50);
        Log.d("TESTJJ", "upload: outside6");
        riverref.putBytes(cataimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("TESTJJ", "upload: success");
                riverref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TESTJJ", "upload: internal");
                        progressDialog.dismiss();
                        OMR omr=new OMR(classname,rollno,subject,uri.toString(),total_marks,marks_obtained,datetime);
                        imageupload[0] = true;
                        ref.push().setValue(omr).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                toast.ToastSuccess("SUBMITTED SUCCESSFULLY");
                                Intent intent = new Intent(cont,OMR_SCANNER.class);   // Moving to Login Class after Success
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                cont.startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toast.ToastError("ERROR : "+e);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        toast.ToastError("ERROR : "+e);
                    }
                });
            }
        });

    }
}
