package com.example.omr.OMR_FUNCTION;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.omr.R;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_ANSWER;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.photo.Photo;
import org.opencv.utils.Converters;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


import static com.example.omr.OMR_FUNCTION.Util.getIndexOf;
import static com.example.omr.OMR_FUNCTION.Util.sortLeft2Right;
import static com.example.omr.OMR_FUNCTION.Util.sortTopLeft2BottomRight;
import static org.opencv.core.Core.countNonZero;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC4;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_NONE;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_RGBA2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_CLOSE;
import static org.opencv.imgproc.Imgproc.MORPH_DILATE;
import static org.opencv.imgproc.Imgproc.MORPH_ERODE;
import static org.opencv.imgproc.Imgproc.MORPH_OPEN;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.RETR_FLOODFILL;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import static org.opencv.imgproc.Imgproc.approxPolyDP;
import static org.opencv.imgproc.Imgproc.arcLength;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.equalizeHist;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.getPerspectiveTransform;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.putText;
import static org.opencv.imgproc.Imgproc.threshold;

public class Subject_info extends Activity {
    ImageView image;
    private Button submit;
    TextView tquestions,coanswers,wanswers;
    SHAREDPREF_ANSWER shared_answer;
    String[] options = new String[]{"A", "B", "C", "D"};
    String[] Answers;

    private String[] canswers=new String[24];
    private int[] fanswers=new int[24];
    public static final String PHOTO_MIME_TYPE = "image/png";
    public static final String EXTRA_PHOTO_URI = "com.nummist.secondsight.Subject_info.extra.PHOTO_URI";
    public static final String EXTRA_PHOTO_DATA_PATH = "com.nummist.secondsight.Subject_info.extra.PHOTO_DATA_PATH";
    private Uri mUri;
    private String mDataPath;
    private Boolean Qrfound=false;
    private int wrongans=0;
    private int correctans=0;
    private String sResult = "";
    private CUSTOM_TOAST custom_toast;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_info);
        final Intent intent = getIntent();
        mUri = intent.getParcelableExtra(EXTRA_PHOTO_URI);
        mDataPath = intent.getStringExtra(EXTRA_PHOTO_DATA_PATH);
        image = findViewById(R.id.show_pic);
        tquestions=findViewById(R.id.t_questions_ans);
        coanswers=findViewById(R.id.Correct_Answers_ans);
        wanswers=findViewById(R.id.w_Answers_ans);
        submit=findViewById(R.id.take_pic);
        custom_toast=new CUSTOM_TOAST(getApplicationContext());
        shared_answer=new SHAREDPREF_ANSWER(getApplicationContext());
        loadanswer();

        wrongans=0;
        image.setImageURI(mUri);
        xzing2();
        if (Qrfound.equals(true)){
            applychanges();
        }
        else if(Qrfound.equals(false)){
            applychanges();
            custom_toast.ToastError("QR-CODE NOT FOUND,TRY MAINTAINING CAMERA POSITION");
            Log.d("CHECK_OMR", "QRCODE : NOT FOUND ");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Qrfound.equals(true)){
                    uploaddatabase();
                }else {
                    custom_toast.ToastError("QR-CODE Required to Submit");
                }

            }
        });
    }

    public void applychanges() {
        try {
             Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUri);
                Log.d("image_URI", "applychanges: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
             Mat image1=new Mat();
             Utils.bitmapToMat(bitmap,image1);
            Mat frame = image1.clone();
             //cvtColor(image1,image1, COLOR_RGBA2GRAY);


            Mat gray = new Mat(frame.size(), CV_8UC1);
            Mat gaussianblur = new Mat(gray.size(), CV_8UC1);
            Mat thresholdwrapper = new Mat(gaussianblur.size(), CV_8UC1);
            Mat thresh = new Mat(gaussianblur.size(), CV_8UC1);
            Mat cannywrapper = new Mat(gaussianblur.size(), CV_8UC1);
            Mat rang = new Mat(frame.size(), CV_8UC4);
            Mat grayrang = new Mat(frame.size(), CV_8UC4);
            List<MatOfPoint> contours = new ArrayList<>();
            List<MatOfPoint> circlecontours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Mat mhierarchy = new Mat();
            List<MatOfPoint> selectedcontours = new ArrayList<>();
            List<MatOfPoint> optioncontours = new ArrayList<>();

            Log.d("TESTSIZE", "onCameraFrame: " + options.length);


            cvtColor(frame, gray, COLOR_RGBA2GRAY);
            CLAHE clahe= Imgproc.createCLAHE(3,new Size(8,8));
            clahe.apply(gray,gray);

            Imgproc.GaussianBlur(gray, gaussianblur, new Size(5., 5.), 2);
            adaptiveThreshold(gaussianblur, thresh, 128, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY_INV, 11, 6);
            threshold(thresh, thresh, 128, 255, THRESH_BINARY | THRESH_OTSU);
            Imgproc.Canny(gaussianblur, cannywrapper, 75, 200);
            threshold(cannywrapper, thresholdwrapper, 0, 255, THRESH_BINARY | THRESH_OTSU);
            erode(thresholdwrapper, thresh, getStructuringElement(MORPH_OPEN, new Size(1, 1)));

            findContours(thresh, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_NONE);
            Log.d("OMRTEST", "onCameraFrame: " + contours.size());

            //To Find Main Wrapper
            for (int i = 0; i < contours.size(); i++) {
                MatOfPoint2f approxCurve = new MatOfPoint2f(contours.get(i).toArray());
                double peri = arcLength(approxCurve, true);
                approxPolyDP(approxCurve, approxCurve, 0.02 * peri, true);
                double area = contourArea(contours.get(i));
                Log.d("AUTO_PASS", "applychanges: "+approxCurve.toArray().length);

                Rect rec = boundingRect(contours.get(i));
                double ratio = rec.width / rec.height;
                Log.d("cntarea", "applychanges: " + area + " : " + approxCurve.toArray().length + " : " + ratio);
                if (area > 10000 & approxCurve.toArray().length == 4) {
                    Log.d("cntworld", "applychanges: " + area);
                    drawContours(frame, contours, i, new Scalar(255, 0, 0), 3);
                    Rect ptsv = boundingRect(contours.get(i));
                    //Log.d("cntpoint", "applychanges: ");
                    int padding = 30;
                    ptsv.x += padding;
                    ptsv.y += padding;
                    ptsv.width -= 2 * padding;
                    ptsv.height -= 2 * padding;
                    List<Point> src_points = new ArrayList<>();

                    Point topleft = ptsv.tl();
                    Point bottomright = ptsv.br();
                    Point topright = new Point(topleft.x + (ptsv.width), topleft.y);
                    Point bottomleft = new Point(topleft.x, topleft.y + ptsv.height);
//                Log.d("POINTS12", "onCameratopleft: "+topleft);
//                Log.d("POINTS12","bottomright :"+bottomright);
//                Log.d("POINTS12", "Area: "+ptsv.area());
//                Log.d("POINTS12", "Width "+ptsv.width);
//                Log.d("POINTS12", "HEIGHT: "+ptsv.height);
                    src_points.add(topleft);
                    src_points.add(topright);
                    src_points.add(bottomleft);
                    src_points.add(bottomright);

                    Mat startM = Converters.vector_Point2f_to_Mat(src_points);
                    List<Point> dst_pnt = new ArrayList<Point>();
                    Point p4 = new Point(0, 0);
                    dst_pnt.add(p4);
                    Point p5 = new Point(thresh.width(), 0);
                    dst_pnt.add(p5);
                    Point p6 = new Point(0, thresh.height());
                    dst_pnt.add(p6);
                    Point p7 = new Point(thresh.width(), thresh.height());
                    dst_pnt.add(p7);
                    Mat endM = Converters.vector_Point2f_to_Mat(dst_pnt);
                    Mat prod = getPerspectiveTransform(startM, endM);
                    Imgproc.warpPerspective(thresh, rang, prod, new Size(thresh.width(), thresh.height()));
                    Imgproc.warpPerspective(image1, grayrang, prod, new Size(thresh.width(), thresh.height()));
                  //  dilate(rang, rang, getStructuringElement(MORPH_ERODE, new Size(3, 3)));
                    findContours(rang, circlecontours, mhierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);


                    Log.d("cntpoint1", "jhgj: "+circlecontours.size());
                    mhierarchy.release();
                    for (int k = 0; k < circlecontours.size(); k++) {
                        Rect cntw = boundingRect(circlecontours.get(k));
                        int w = cntw.width;
                        int h = cntw.height;
                        double ratioc = Math.max(w, h) / Math.min(w, h);
                        MatOfPoint2f apCurve = new MatOfPoint2f(circlecontours.get(k).toArray());
                        double perival = arcLength(apCurve, true);
                        approxPolyDP(apCurve, apCurve, 0.03 * perival, true);
                        //Log.d("circle_detect", "applychanges: "+apCurve.toArray().length);
                         Log.d("cnt_check", "applychanges: "+contourArea(circlecontours.get(k))+" : "+ratioc+" : "+apCurve.toArray().length);
                        if ( (ratioc >= 0.9 & ratioc <= 1.1) &&  apCurve.toArray().length>=5 & contourArea(circlecontours.get(k)) > 1000){
                            //Log.d("RATIOCONTOUR", "onCameraFrame: " + ratioc);
                            // Imgproc.drawContours(frame, circlecontours, k, new Scalar(255, 0, 0), 3);
                            //Log.d("AreaTest", "onCameraFrame: " + contourArea(circlecontours.get(k)));
                            selectedcontours.add(circlecontours.get(k));
                        }

                    }
                    Log.d("cnt_check", "Selected_Contours: "+selectedcontours.size());
                    sortTopLeft2BottomRight(selectedcontours);

                    List<MatOfPoint> bubbles = new ArrayList<>();

                    if (selectedcontours.size() == 96 || selectedcontours.size()==64) {
                        for (int j = 0; j < selectedcontours.size(); j += options.length * 2) {
                            Log.d("OMRSSID", "onCame: " + selectedcontours.size());
                            List<MatOfPoint> row = selectedcontours.subList(j, j + options.length * 2);
                            sortLeft2Right(row);
                            bubbles.addAll(row);

                        }
                    }
                    Log.d("filling", "onCamerabubbles: " + bubbles.size());

                    int counting=0;
                    if (selectedcontours.size()==96 || selectedcontours.size()==64) {

                        // sortTopLeft2BottomRight(bubbles);
                        // sortLeft2Right(bubbles);
                        //drawContours(frame.submat(ptsv), bubbles.subList(79,80), -1, new Scalar(0, 255, 0), 3);
                        Rect distance = boundingRect(bubbles.get(3));
                        Log.d("filling", "onCameraDistance: " + distance.x + " : " + distance.y);
                        //if (distance.y>250){
                        // drawContours(frame.submat(ptsv), bubbles.subList(5,6), -1, new Scalar(0, 255, 0), 3);
                        //}
                        Mat thressel=thresh.submat(ptsv);
                        Mat conjuction=new Mat();
                        for (int j=0;j<bubbles.size()/4;j++){
                            int values[]=new int[4];
                            for (int k=0;k<4;k++){

                                Mat mask = Mat.zeros(rang.size(),CV_8UC1);

                                drawContours(mask, bubbles.subList(((j*4)+k),((j*4)+k)+1), -1, new Scalar(255, 0, 0), -1);
                                dilate(mask, mask, getStructuringElement(MORPH_ERODE, new Size(3, 3)));
                                conjuction = Mat.zeros(rang.size(),CV_8UC1);
                                Core.bitwise_and(rang, mask, conjuction);
                                int countnonzero= countNonZero(conjuction);
                                Log.d("NONZERO", "applychanges: "+((j*4)+k)+" : "+countnonzero);
                                values[k]=countnonzero;



                            }
                            int valuescopy[]=values.clone();
                            boolean second_option=true;
                            Arrays.sort(values);
                            Log.d("testcircle" ,"applychanges: "+values[0]+" : "+values[1]);
                            if((values[0]<1000 && (values[1]-values[0])>300) ){
                                int index=getIndexOf(values[0],valuescopy);
                                String answer=alphacaught(index);
                                Log.d("ANSWER_VALUE", "applychanges: "+Answers[j]);
                                if(index==0 && Answers[j].equals("A")){
                                    canswers[j]=answer;
                                    drawContours(grayrang,bubbles,(j*4)+index,new Scalar(0,255,0),3);
                                    correctans++;
                                }
                                else if(index==1 && Answers[j].equals("B")){
                                    canswers[j]=answer;
                                    drawContours(grayrang,bubbles,(j*4)+index,new Scalar(0,255,0),3);
                                    correctans++;
                                }
                                else if(index==2 && Answers[j].equals("C")){
                                    canswers[j]=answer;
                                    drawContours(grayrang,bubbles,(j*4)+index,new Scalar(0,255,0),3);
                                    correctans++;
                                }
                                else if(index==3 && Answers[j].equals("D")){
                                    canswers[j]=answer;
                                    drawContours(grayrang,bubbles,(j*4)+index,new Scalar(0,255,0),3);
                                    correctans++;
                                }
                                else {
                                    wrongans++;
                                    Log.d("fillinf", "applychangessss: "+(values[1]-values[0]));
                                    drawContours(grayrang,bubbles,(j*4)+index,new Scalar(255,0,0),3);
                                    int ansindex=0;
                                    if(Answers[j].equals("A")){
                                        ansindex=0;
                                    }
                                    else if(Answers[j].equals("B")){
                                        ansindex=1;
                                    }
                                    else if(Answers[j].equals("C")){
                                        ansindex=2;
                                    }
                                    else if(Answers[j].equals("D")){
                                        ansindex=3;
                                    }

                                    drawContours(grayrang,bubbles,(j*4)+ansindex,new Scalar(0,255,0),3);
                                }
                            }
                            else {
                                wrongans++;
                                Log.d("CLASSF", "applychanges: "+Answers[0]+Answers[1]+Answers[15]);
                                int indexof=indexcaught(Answers[j]);
                                fanswers[j]=0;
                                drawContours(grayrang,bubbles,(j*4)+indexof,new Scalar(0,255,0),3);
                            }


                        }
                        String[] QRsplit = sResult.split(",");
                        putText(grayrang,QRsplit[0]+" : "+QRsplit[1],new Point(100,80),Core.FONT_HERSHEY_COMPLEX,1.5,new Scalar(0,0,255));
                         Utils.matToBitmap(grayrang,bitmap);
                        image.setImageBitmap(bitmap);
                    }
                }


            }

        } catch (Exception e) {
            Log.d("filling", "applychanges: "+e);
        }
        Log.d("AUTO_PASS", "applychanges: "+autopass());
        tquestions.setText(String.valueOf(Answers.length));
        coanswers.setText(String.valueOf(correctans));
        wanswers.setText(String.valueOf(wrongans));
    }





        private void xzing2(){
            Bitmap bitma = null;
            try {
                bitma = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Mat mRgba=new Mat();
            Utils.bitmapToMat(bitma,mRgba);
            Bitmap bMap = Bitmap.createBitmap(mRgba.width(), mRgba.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgba, bMap);
            int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(),intArray);

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new QRCodeMultiReader();


            try {

                Result result = reader.decode(bitmap);
                sResult = result.getText();
                Qrfound=true;
                Log.d("QR_FOUND_RESULT","Found something: "+result.getText());

            }
            catch (NotFoundException e) {
                Qrfound=false;
                Log.d("QR_FOUND_RESULT", "Code Not Found");
                e.printStackTrace();
            } catch (FormatException e) {
                Qrfound=false;
                e.printStackTrace();
            } catch (ChecksumException e) {
                Qrfound=false;
                e.printStackTrace();
            }
        }


    private int indexcaught(String Ans_wers){
        Log.d("CLASSF", "indexcaught: "+Ans_wers);
        int ansindex=0;
        if(Ans_wers.equals("A")){
            ansindex=0;
        }
        if(Ans_wers.equals("B")){
            ansindex=1;
        }
        if(Ans_wers.equals("C")){
            ansindex=2;
        }
        if(Ans_wers.equals("D")){
            ansindex=3;
        }
        return ansindex;
    }
    private String alphacaught(int index){
        String alphaans="N";
        if(index==0){
            alphaans="A";
        }
        if(index==1){
            alphaans="B";
        }
        if(index==2){
            alphaans="C";
        }
        if(index==3){
            alphaans="D";
        }
        return alphaans;
    }
    public void uploaddatabase(){
        String[] QRsplit = sResult.split(",");
        Log.d("CLASSC", "uploaddatabase: "+QRsplit[0]+":"+QRsplit[1]);
        String Rollno=QRsplit[0];
        String Classname=QRsplit[1];
        SHAREDPREF_ANSWER sharedpref_answer=new SHAREDPREF_ANSWER(getApplicationContext());
        String Subject=sharedpref_answer.getsubject();
        ProgressDialog progressDialog=new ProgressDialog(this);
        int correct_answers=autopass();
        To_Database database=new To_Database(this,Rollno,Classname,mUri,Subject,Integer.toString(correct_answers),Integer.toString(Answers.length));
        database.upload(progressDialog);
    }
    public void loadanswer(){
        Log.d("CLASSA", "loadanswer: "+shared_answer.getAnswers());
        String modifies=shared_answer.getAnswers().replaceAll(" ,, ","");
        Log.d("CLASSA", "loadanswer: "+modifies);
        Answers=modifies.split(",");
        Log.d("CLASSA", "loadanswer:SIZE "+Answers[1]+Answers[2]+Answers[15]);
        //Log.d("CLASSA", "loadanswer: "+ansall[1]+ansall[2]+ansall[3]);


    }
    private int autopass(){
            int correctanswer=Util.CountArray(canswers);
            SHAREDPREF_ANSWER sharedpref_answer=new SHAREDPREF_ANSWER(this);
            String auto_pass=sharedpref_answer.getautopass();

            if(!auto_pass.equals("NONE")){
                int passpercentage=Integer.parseInt(auto_pass);
                int correct=Util.CountArray(canswers);
                Log.d("AUTOPASSING", "autopass_correct: "+correct);
                int percentage=((Answers.length)*passpercentage)/100;
                Log.d("AUTOPASSING", "autopass: "+percentage);
                percentage=Math.round(percentage);
                Log.d("AUTOPASSING","autopass_round"+percentage);
                if(percentage>correct){
                    correctanswer=percentage;
                    return correctanswer;
                }
                else {
                    correctanswer=correct;
                    return correctanswer;
                }


            }
            return correctanswer;
    }
}




