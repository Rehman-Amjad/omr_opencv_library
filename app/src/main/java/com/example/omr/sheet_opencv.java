package com.example.omr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.IOException;

public class sheet_opencv extends AppCompatActivity {
    ImageView image_opencv;
    Uri[] imageuri;
    Button image1,image2,show_image;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_opencv);
        image_opencv=findViewById(R.id.opencv_image);
        show_image=findViewById(R.id.show_image);
        imageuri=new Uri[2];
        image1=findViewById(R.id.image_1);
        image2=findViewById(R.id.image_2);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepicker(v);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepicker(v);
            }
        });
        show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_image();
            }
        });

    }
    public void imagepicker(View v){
        Intent gallery= new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select Profile Picture"),1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            imageuri[count]=data.getData();
            if(count==1){
                count=0;
            }
            count++;
        }
    }
    public void show_image(){
        Bitmap bitmap = null;
        Bitmap bitmap1=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri[0]);
            bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri[1]);
            Log.d("image_URI", "applychanges: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
          Mat img1=new Mat();
          Mat img2=new Mat();
        Utils.bitmapToMat(bitmap,img1);
        Utils.bitmapToMat(bitmap1,img2);
        image_opencv.setImageBitmap(bitmap);

        }

}
