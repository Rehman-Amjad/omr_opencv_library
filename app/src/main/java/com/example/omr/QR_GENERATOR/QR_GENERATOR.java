package com.example.omr.QR_GENERATOR;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.omr.DATABASE.todatabase;
import com.example.omr.R;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class QR_GENERATOR extends AppCompatActivity {
    private String TAG="MAIN";
    private EditText value,rollno,rend,rstart,classname;
    private Button generate;
    private ImageView image;
    CUSTOM_TOAST custom_toast;
    Bitmap bitmap;
    DatabaseReference ref;
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String TAG="MAIN";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__generator);
        rollno=findViewById(R.id.rollno);
        rend=findViewById(R.id.Rend);
        rstart=findViewById(R.id.Rstart);
        classname=findViewById(R.id.classname);
        value=findViewById(R.id.classname);
        generate=findViewById(R.id.generate);
        image=findViewById(R.id.qrview);
        custom_toast=new CUSTOM_TOAST(getApplicationContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        generate.setOnClickListener(this::generateqr);
}


public void generateqr(View v){
        // PROGRESS DIALOG TO SHOW PROGRESS OF QR GENERATION
    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMax(100);
    progressDialog.setTitle("QR IS GENERATING");
    progressDialog.setMessage("PLEASE WAIT...");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setCancelable(false);
    String cname=classname.getText().toString().trim().toUpperCase();
    ref= FirebaseDatabase.getInstance().getReference("Students").child(cname);
    String RN=rollno.getText().toString().trim().toUpperCase();
    int RS=Integer.parseInt(rstart.getText().toString());
    int RE=Integer.parseInt(rend.getText().toString());
    todatabase td=new todatabase(ref,getApplicationContext());
    mStorageRef = FirebaseStorage.getInstance().getReference();
    // GET CURRENT DATE
    Calendar cal=Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH)+1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    String Currentdate=year+"/"+month+"/"+day;
    if(RE-RS>0) {
        int progress = 100 / (RE - RS);
        String RANGE = "FROM " + RN + "-" + RS + " TO " + RE;
        for (int i = RS; i <= RE; i++) {
            String ROLLNUMBER = RN.concat("-" + i + "," + cname);
            String[] Croll = ROLLNUMBER.split(",");
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            int prog = i;
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(ROLLNUMBER.toString(), BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                image.setImageBitmap(bitmap);
                Log.d(TAG, "onClick: " + bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String location = "students/" + cname + "/" + Croll[0].toString() + ".jpg";
                StorageReference riverref = mStorageRef.child(location);
                if (true) {
                    progressDialog.show();
                    riverref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riverref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.incrementProgressBy(progress);
                                    Log.d(TAG, "onSuccess: " + progress);
                                    if (progressDialog.getProgress() == 100) {
                                        progressDialog.dismiss();
                                    }
                                    Boolean result = td.addstudentdetail(Croll[0], cname, Currentdate, uri.toString());
                                    if (prog == (RE - 1) && result == true) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "QR GENERATED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                    }
                                    if (result == false) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "ERROR OCCURED", Toast.LENGTH_LONG).show();
                                    }
                                    if (progressDialog.getProgress() == 100) {
                                        progressDialog.dismiss();
                                    }

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "ERROR :" + e, Toast.LENGTH_LONG).show();
                        }
                    });


                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        ;
    }
    else {
        custom_toast.ToastError("Must Enter Valid Range");
    }
    }
    public void alreadyexist(){

    }
    }

