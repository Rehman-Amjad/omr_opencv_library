package com.example.omr.SPLASH_SCREEN;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.omr.LOGIN_SELECTION;
import com.example.omr.R;
import com.squareup.picasso.Picasso;

public class SPLASH_SCREEN extends AppCompatActivity {
    ImageView splash_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        splash_screen=findViewById(R.id.splash_screen);
        Picasso.get().load(R.drawable.penink).fit().into(splash_screen);

        Thread background=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3*1000);
                    Intent intent=new Intent(getBaseContext(), LOGIN_SELECTION.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
