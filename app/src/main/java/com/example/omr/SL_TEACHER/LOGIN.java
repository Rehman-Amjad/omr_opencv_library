package com.example.omr.SL_TEACHER;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.omr.DASHBOARD;
import com.example.omr.DATABASE.todatabase;
import com.example.omr.R;
import com.example.omr.SETTINGS.SETTING;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_LIST;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LOGIN extends AppCompatActivity {
    EditText username,password;
    Button login,signup;
    TextView forgetpassword;
    CUSTOM_TOAST toast;
    ProgressBar status;
    DatabaseReference ref;
    SHAREDPREF sharedPreferences;
    SHAREDPREF_LIST sharedpref_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username_signin);
        password=findViewById(R.id.password_signin);
        status=findViewById(R.id.Login_status);
        login=findViewById(R.id.button_login);
        forgetpassword=findViewById(R.id.forget_password_btn);
        login.setOnClickListener(this::evaluate);
        signup=findViewById(R.id.button_signup);
        toast=new CUSTOM_TOAST(getApplicationContext());
        sharedPreferences=new SHAREDPREF(getApplicationContext());
        sharedpref_list=new SHAREDPREF_LIST(getApplicationContext());
        String pusername=sharedPreferences.getUsername();
        Log.d("pusername", "onCreate: "+pusername);
        if (pusername!="false"){
            sharedpref_list.setuser("TEACHER");
            Intent intent=new Intent(this, DASHBOARD.class);
            startActivity(intent);
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SIGNUP_VERIFY.class);
                startActivity(intent);
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogbox_forgetpassword();

            }
        });
    }
    private void evaluate(View v){
        int error=0;
        String user=username.getText().toString().toUpperCase().trim();
        String pass=password.getText().toString();
        if (TextUtils.isEmpty(user)) {
            username.setError("You Must Enter Username");
            username.requestFocus();
            error++;
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("You Must Enter Password");
            password.requestFocus();
            error++;
        }

        if(error==0){
            ref= FirebaseDatabase.getInstance().getReference("SIGINING_INFO");
            todatabase td=new todatabase(ref,getApplicationContext());
            td.loginavaluation(user,pass,getApplicationContext(),status);
            status.setVisibility(View.VISIBLE);
        }


    }
    private void Dialogbox_forgetpassword(){

        final AlertDialog.Builder alert=new AlertDialog.Builder(LOGIN.this);
        View mview=getLayoutInflater().inflate(R.layout.change_name_layout,null);

        final Button canel,done;
        final EditText email;
        final TextView title;
        canel=mview.findViewById(R.id.cancel_button_name);
        done=mview.findViewById(R.id.done_button_name);
        email=mview.findViewById(R.id.changed_fullname);
        title=mview.findViewById(R.id.textView200);
        title.setText("FORGET PASSWORD");
        email.setHint("ENTER EMAIL");
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
                String getemail=email.getText().toString().trim();
                int error=0;
                if(TextUtils.isEmpty(getemail)){
                    error++;
                    email.setError("Must not be Empty");
                }
                if(getemail.length()< 6){
                    error++;
                    email.setError("Length must be Larger than 5");
                }
                if(!TextUtils.isEmpty(getemail)){
                    if(!getemail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                        email.setError("Enter Valid Email Address");
                        error++;
                    }
                }
                if(error==0){
                    forget_password(getemail);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
    private void forget_password(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast.ToastSuccess("MAIL SENT TO YOUR GIVEN EMAIL ADDRESS");
                        }
                        else {
                            toast.ToastError("ERROR OCCURRED WHILE PROCEEDING YOUR REQUEST");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast.ToastError(e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
