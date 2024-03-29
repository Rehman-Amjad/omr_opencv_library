package com.example.omr.OMR_FUNCTION;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.omr.DASHBOARD;
import com.example.omr.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

public class OMR_SCANNER extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    Mat frame;
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    private Mat mBgr;
    Button takepic,cancel_picture;
    boolean mtakepic=false;
    int Camera_width,Camera_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omr__scanner);
        takepic=findViewById(R.id.take_pic);
        cancel_picture=findViewById(R.id.cancel_action);
        takepic.bringToFront();
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraBridgeViewBase = (JavaCamera2View) findViewById(R.id.camera_opencv);
        Log.d("frame_test", "onCreate: "+Camera_height);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        cameraBridgeViewBase.forceLayout();
        //cameraBridgeViewBase.setMaxFrameSize(720,1280);
        cameraBridgeViewBase.setMinimumHeight(720);
        cameraBridgeViewBase.setMinimumWidth(1280);
        cameraBridgeViewBase.setMaxFrameSize(1280,720);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        mBgr = new Mat();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
    };
        takepic.bringToFront();
        cancel_picture.bringToFront();
        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtakepic=true;
            }
        });

        cancel_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OMR_SCANNER.this, DASHBOARD.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        frame = inputFrame.rgba();
        Mat mframe=frame.t();

         if(mtakepic==true){
            Log.d("filling", "onCameraFrame: "+mtakepic);
             Core.flip(frame.t(),mframe,1);
             //Imgproc.resize(mframe,mframe,frame.size());

            takePhoto(mframe);

        }

        return frame;
    }
    public void zxing(Mat mRgba){
        Bitmap bMap = Bitmap.createBitmap(mRgba.width(), mRgba.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mRgba, bMap);
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(),intArray);

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new QRCodeMultiReader();

        String sResult = "";

        try {

            Result result = reader.decode(bitmap);
            sResult = result.getText();

            Log.d("QR_FOUND_VALUE","Found something: "+result.getText());

        }
        catch (NotFoundException e) {
            Log.d("QR_FOUND_VALUE", "Code Not Found");
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        }
    }
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "There's a problem, yo!", Toast.LENGTH_SHORT).show();
        } else {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase != null) {

            cameraBridgeViewBase.disableView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }
    private void takePhoto(final Mat rgba){
        int android_sdk=Build.VERSION.SDK_INT;
        final long currentTimeMillis = System.currentTimeMillis();
        final String appName = getString(R.string.app_name);
        final String galleryPath =      Environment.getExternalStoragePublicDirectory(        Environment.DIRECTORY_PICTURES).toString();
        final String albumPath = galleryPath + "/" + appName;
        final String photoPath = albumPath + "/" +      currentTimeMillis + ".png";
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, photoPath);
        values.put(MediaStore.Images.Media.MIME_TYPE,      Subject_info.PHOTO_MIME_TYPE);
        values.put(MediaStore.Images.Media.TITLE, appName);
        values.put(MediaStore.Images.Media.DESCRIPTION, appName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
        File album = new File(albumPath);
        if (!album.isDirectory() && !album.mkdirs()) {
            Log.d("filling", "takealbum: "+albumPath);
        }
        Imgproc.cvtColor(rgba, mBgr, Imgproc.COLOR_RGBA2BGR, 3);
        if(!Imgcodecs.imwrite(photoPath,mBgr)){
            Log.d("filling", "takePhotofailed: "+photoPath);
        }
        Uri uri=null;
        try{
            uri=getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values
            );
        }catch (final Exception e){
            e.printStackTrace();
        }
        Log.d("IMAGE_URI", "takePhoto: "+uri);
        mtakepic=false;
        final Intent intent = new Intent(this, Subject_info.class);    intent.putExtra(Subject_info.EXTRA_PHOTO_URI, uri);    intent.putExtra(Subject_info.EXTRA_PHOTO_DATA_PATH,      photoPath);    startActivity(intent);

    }
}
