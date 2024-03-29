package com.example.omr.DATABASE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.omr.DASHBOARD;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_LIST;
import com.example.omr.SL_TEACHER.LOGIN;
import com.example.omr.SETTER_G.TEACHER;
import com.example.omr.SETTER_G.STUDENT;
import com.example.omr.SETTER_G.STUDENT_RECORD;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF;
import com.example.omr.SL_TEACHER.SIGNUP;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.example.omr.RECORD.list_click_detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class todatabase {
    private DatabaseReference ref;      // For Database Reference
    private Context cont;       // To apply Root Activity Context to this
    private Boolean success=true;
    private Bitmap bitmap;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private byte[] cataimage;
    private StorageReference mStorageRef;
    private SHAREDPREF sharedPreferences;
    private SHAREDPREF_LIST lsharedPreferences;
    private CUSTOM_TOAST toast;
    private FirebaseAuth firebaseAuth;

    public todatabase(DatabaseReference ref, Context context) {
        this.ref = ref;   // Assiging Database Reference
        this.cont = context;  // Assigning Context to this Activity
        mStorageRef = FirebaseStorage.getInstance().getReference(); // getting StorageDatabase Reference
        toast=new CUSTOM_TOAST(context);    // To SHow Toast Messages
        sharedPreferences=new SHAREDPREF(context);    //To get Last User Name
        lsharedPreferences=new SHAREDPREF_LIST(context);
        ref.keepSynced(true);
        firebaseAuth=FirebaseAuth.getInstance();
        
    }


    // SIGNUP FUNCTION FOR TEACHER

    public void addValueSignup(String name, String edudeparment, String dob, Uri Imageuri,ProgressDialog progressDialog,String key,String email) {
        //ASSIGINING VALUE TO SETTER GETTER
        Log.d("VERIFYEMAIL", "addValueSignup: "+key);
        TEACHER signup = new TEACHER();
        signup.setName(name);
        signup.setEdu_department(edudeparment);
        signup.setDOB(dob);
        signup.setEmail(email);
        Log.d("VALUES", "addValueSignup: Button Clicked");

        final Boolean[] imageupload = {false}; // To Find Whether Image Uploaded or not
        //Progress Dialog to Show Signup Progress;
        progressDialog.setMax(100);
        progressDialog.setTitle("Creating Account ");
        progressDialog.setMessage("PLEASE WAIT...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Storing profile image to Bitmap
        try {
            bitmap= MediaStore.Images.Media.getBitmap(cont.getContentResolver(),Imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Changing the bits of Image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        cataimage = baos.toByteArray();  //Converting image to bytes
        String location="PROFILE/"+key+".jpg";    // Location of Profile Image in Database;
        final StorageReference riverref=mStorageRef.child(location);    // Getting reference of Image Storage
        progressDialog.incrementProgressBy(50);

        //Uploading Image to Database
        riverref.putBytes(cataimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Getting Image Download Referene
                riverref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("VERIFYEMAIL", "addValueSignup: onsucess");
                        Log.d("Upload", "onSuccess: " + uri.toString());
                        progressDialog.dismiss();
                        signup.setImage(uri.toString());
                        imageupload[0] = true; // Setting Image upload status
                        //Uploading Values of Signup to Database;
                        ref.child(key).setValue(signup).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("VERIFYEMAIL", "addValueSignup: onsuccess2");
                                toast.ToastSuccess("Account is Created Succesfully");
                                Intent intent = new Intent(cont, LOGIN.class);   // Moving to Login Class after Success
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
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("VERIFYEMAIL", "addValueSignup: onfailure");
                toast.ToastError(e.getMessage());
            }
        });

    }



    // Login Function For Teacher

    public void loginavaluation(String email, String password, Context conti, ProgressBar status) {
        ref=FirebaseDatabase.getInstance().getReference("SIGINING_INFO");
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        toast.ToastSuccess("LOGIN SUCCESSFULL");
                        status.setVisibility(View.INVISIBLE); // Progress Bar Visibility
                        ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChildren()){
                                    Intent intent=new Intent(conti, DASHBOARD.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    conti.startActivity(intent);
                                    sharedPreferences.setUsername(email);
                                    lsharedPreferences.setuser("TEACHER");
                                }
                                else{
                                    String UserID=firebaseAuth.getUid();
                                    Intent intent=new Intent(conti, SIGNUP.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("USERID",UserID);
                                    intent.putExtra("EMAIL",email);
                                    conti.startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        status.setVisibility(View.INVISIBLE);
                        toast.ToastError("PLEASE VERIFY YOUR EMAIL");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status.setVisibility(View.INVISIBLE);
                toast.ToastError(e.getMessage());
            }
        });
        // Checking Username in Databse Whether it Exist or not


    }



    //Funtion TO ADD STUDENT DETAIL (QR GENRATOR FUNCTION)

    public boolean addstudentdetail(String Rollno,String classname,String Dategenerated,String qrref) {
        // Set Value to Setter Getter
        String user_generator=FirebaseAuth.getInstance().getUid();
        STUDENT_RECORD student=new STUDENT_RECORD();
        student.setRollno(Rollno);
        student.setClassname(classname);
        student.setDategenerated(Dategenerated);
        student.setQrref(qrref);
        student.setQrgenerated_user(user_generator);

        //Add Student Detail to Database
        ref.child(Rollno).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success=false;
            }
        });
        return success;  // return success Result

    }


    //Funtion to Remove Student Entries

    public Boolean removestudent(ArrayList<STUDENT_RECORD> arrayList){
        final Boolean[] success = {false};

        // Itterate through the array until all the selected entries are Deleted
        for(int i=0;i<arrayList.size();i++){
            String classname=arrayList.get(i).getClassname();
            String rollno=arrayList.get(i).getRollno();

        ref.child(classname).child(rollno).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0]=true;
            }
        });
        }
        return success[0];
    }


    //Funtion to Check if Rollno Already Exist in Database or not

    public void validate_rollno(String rollno,String classname){
        boolean[] result = {false};
        ref= FirebaseDatabase.getInstance().getReference("Students");
        Log.d("VALUES_CHECK", "validate_rollno: "+rollno);
        ref.child(classname.toUpperCase()).orderByChild("rollno").equalTo(rollno).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("VALUES_CHECK", "onDataChange: "+dataSnapshot.getValue());
                if(dataSnapshot.hasChildren()){
                    Log.d("VALUES_CHECK", "onDataChange: CHILD EXIST");
                    sharedPreferences.setRollno("true");
                }
                else {
                    sharedPreferences.setRollno("false");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}



// Funtion For Signup Student

public void signup_student(String Name,String Email,String Rollno,String Password,String Classname){
    ref= FirebaseDatabase.getInstance().getReference("STUDENT");
        String getkey=ref.push().getKey();  // to get reference Key from Database (Unique ID)
    Log.d("VALUES_Student", "signup_student: "+Classname);
        STUDENT student=new STUDENT(Name,Email,Password,getkey,Rollno); //Setting Value to Setter
        student.setClassname(Classname);

        // Uploading Student Signup Credentials
        ref.child(getkey).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                toast.ToastSuccess("ACCOUNT CREATED SUCCESSFULLY");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast.ToastError("ERROR : "+e);
            }
        });
}


//SIGNIN FUNCTION FOR STUDENT

    public void Student_Signin(String Username,String Password){
        ref.orderByChild("rollno").equalTo(Username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("VALUES", "onDataChange: "+Username);
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.d("VALUES", "onDataChange: "+postSnapshot.getValue());
                    STUDENT student = postSnapshot.getValue(STUDENT.class);
                    if (student.getRollno().equals(Username) && student.getPassword().equals(Password)) {
                        lsharedPreferences.set_Student_rollno(student.getRollno());
                        Log.d("STUDENT_CLASSNAME", "onDataChange: "+student.getClassname());
                        lsharedPreferences.set_student_Classname(student.getClassname());
                        lsharedPreferences.setuser("STUDENT");
                       toast.ToastSuccess("LOGIN SUCCESSFULL");
                       Intent intent=new Intent(cont, list_click_detail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       cont.startActivity(intent);
                    }
                    else {
                        toast.ToastError("EMAIL OR PASSWORD NOT CORRECT");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                  toast.ToastError(databaseError.getMessage());
            }
        });
    }

}
