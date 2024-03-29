package com.example.omr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.omr.OMR_FUNCTION.Scanner_setup;
import com.example.omr.QR_GENERATOR.QR_GENERATOR;
import com.example.omr.SETTER_G.TEACHER;
import com.example.omr.SETTINGS.SETTING;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF;
import com.example.omr.SL_TEACHER.LOGIN;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DASHBOARD extends AppCompatActivity {
    private ImageView cover,profilepic;
    private CardView QRGENERATOR,HISTORY,QR_SCAN,OMR_SCANNER,SETTING;
    private TextView USER_NAME;
    private Button signout;
    private DatabaseReference ref;
    private SHAREDPREF sharedPreferences;
    private String pusername;
    CUSTOM_TOAST toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Initializing Variables
        sharedPreferences=new SHAREDPREF(getApplicationContext());
        cover=findViewById(R.id.dashboard_cover);
        USER_NAME=findViewById(R.id.USER_NAME);
        profilepic=findViewById(R.id.profile_pic);
        SETTING=findViewById(R.id.SETTINGS);
        signout=findViewById(R.id.sign_out);
        pusername=sharedPreferences.getUsername();
        QR_SCAN=findViewById(R.id.QR_SCANNER);
        OMR_SCANNER=findViewById(R.id.OMR_SCANNER);
        Picasso.get().load(R.drawable.black_pen).fit().into(cover);
        ref= FirebaseDatabase.getInstance().getReference("SIGINING_INFO");
        toast=new CUSTOM_TOAST(getApplicationContext());
        Load_cred();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Signout();
            }
        });

        QRGENERATOR=findViewById(R.id.QR_GENERATOR);
        QRGENERATOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    QRGENERATOR();
            }
        });
        HISTORY=findViewById(R.id.HISTORY);
        HISTORY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HISTORY();
            }
        });
        QR_SCAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),qr_scanner.class);
                startActivity(intent);
            }
        });
        OMR_SCANNER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Scanner_setup.class);
                startActivity(intent);
            }
        });
        SETTING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), com.example.omr.SETTINGS.SETTING.class);
                startActivity(intent);
            }
        });
    }
    private void Load_cred(){
        ref.orderByChild("email").equalTo(pusername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    TEACHER signup=postSnapshot.getValue(TEACHER.class);
                    USER_NAME.setText(signup.getName());
                    Picasso.get()
                            .load(signup.getImage())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .placeholder(R.mipmap.ic_launcher)
                            .resize(250,250)
                            .centerCrop()
                            .into(profilepic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void HISTORY(){
        Intent intent=new Intent(getApplicationContext(), com.example.omr.RECORD.HISTORY.class);
        startActivity(intent);
    }
    private void QRGENERATOR(){
        Intent intent=new Intent(getApplicationContext(), QR_GENERATOR.class);
        startActivity(intent);
    }
    private void Signout(){
        pusername=sharedPreferences.getUsername();
        sharedPreferences.RemoveUsername();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(), LOGIN.class);
            startActivity(intent);
        }

    }

}
