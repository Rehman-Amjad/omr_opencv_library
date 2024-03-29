package com.example.omr.SL_TEACHER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omr.R;
import com.example.omr.SL_STUDENT.CustomToast;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SIGNUP_VERIFY extends AppCompatActivity {
    private EditText email,password;
    private String Email;
    private String Password;
    private Button submit_button;
    private String Email_get;
    private String password_get;
    private String Userid;
    CUSTOM_TOAST toast;
    FirebaseAuth firebaseAuth;
    private int error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__verify);
        email=findViewById(R.id.email_verify);
        password=findViewById(R.id.password_verify);
        submit_button=findViewById(R.id.SUBMIT_EMAIL);
        toast=new CUSTOM_TOAST(getApplicationContext());
        firebaseAuth=FirebaseAuth.getInstance();
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SIGNUP_VERIFY.this, "click", Toast.LENGTH_SHORT).show();
                inputboxes_evaluation();
            }
        });
    }
    public void inputboxes_evaluation(){
        Email_get=email.getText().toString().trim().toUpperCase();
        password_get=password.getText().toString().trim();
        error=0;

        if (TextUtils.isEmpty(Email_get)) {
            email.setError("You Must Enter Email");
            email.requestFocus();
            error++;
        }
        if(!TextUtils.isEmpty(Email_get)){
            if(!Email_get.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                email.setError("Enter Valid Email Address");
                error++;
            }
        }
        if (TextUtils.isEmpty(password_get)) {
            password.setError("You Must Confirm Your Password ");
            password.requestFocus();
            error++;
        }
        if(error==0){
            create_account();
        }
        else {
            toast.ToastError("FILL ALL THE BOXES");
        }
    }
    public void verify_dialog(){
        new AlertDialog.Builder(this)
                .setTitle("VERIFY EMAIL")
                .setMessage("VERIFY YOUR EMAIL AND CLICK ON VERIFIED BUTTON")
                .setCancelable(false)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("VERIFIED", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            verify_email();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("CANCEL", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void verify_email(){
        Log.d("EMAIL_here", "verify_email: ");
        Log.d("EMAIL_here", "verify_email: "+password_get);
        firebaseAuth.signInWithEmailAndPassword(Email_get,password_get).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("EMAIL_here", "onComplete: "+"here");
                if(task.isSuccessful()){
                    Log.d("EMAIL_here", "onComplete: "+"here");
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Log.d("EMAIL_here", "onComplete: user verifies");
                        toast.ToastInfo("USER VARIFIED");
                        Userid=firebaseAuth.getUid();
                        Intent intent=new Intent(getApplicationContext(),SIGNUP.class);
                        intent.putExtra("USERID",Userid);
                        intent.putExtra("EMAIL",Email_get);
                        startActivity(intent);

                    }else {
                        Log.d("EMAIL_here", "onComplete: "+"ELSE");
                        toast.ToastInfo("USER NOT VERIFIED");
                        verify_dialog();
                    }
                }
                else {
                    toast.ToastError(task.getException().getMessage());
                    verify_dialog();
                }

            }
        });
    }
    public void create_account(){
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().toUpperCase().trim(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                toast.ToastInfo("EMAIL SENT : VERIFY YOUR EMAIL");
                                verify_dialog();
                            }else{
                                toast.ToastError(task.getException().getMessage());
                            }
                        }
                    });
                }else {
                    toast.ToastError(task.getException().getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast.ToastError("LOGIN FAILED : "+e.getMessage());
            }
        });

    }
    public void onBackPressed() {
        Intent intent=new Intent(SIGNUP_VERIFY.this,LOGIN.class);
        startActivity(intent);
    }
}
