package com.example.omr.OMR_FUNCTION;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_ANSWER;
import com.example.omr.TOAST.CUSTOM_TOAST;
import com.example.omr.R;

import java.util.ArrayList;
import java.util.Scanner;

public class Scanner_setup extends AppCompatActivity {
    private int[] id16,id24,Q_Visibilty16,Q_Visibilty24,Q_INVISIBLE;
    private LinearLayout Llayout;
    private Button button;
    private Spinner spin;
    private Spinner subject;
    private String option_Question;
    private String subjectname;
    private CUSTOM_TOAST custom_toast;
    private Boolean Accurate=false;
    private Boolean subject_select=false;
    private RadioGroup autopassing;
    private RadioButton ap_none;
    ArrayList<String> Answers;
    private Boolean usepreviousanswers=true;
    SHAREDPREF_ANSWER sharedpref_answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_setup);
        custom_toast=new CUSTOM_TOAST(getApplicationContext());
        sharedpref_answer=new SHAREDPREF_ANSWER(getApplicationContext());
        Answers=new ArrayList<>();
        if(usepreviousanswers==true){
            if(!sharedpref_answer.getAnswers().equals("false")){
                  startup();
        }}
        autopassing=findViewById(R.id.AUTO_PASS);
        ap_none=findViewById(R.id.none_ap);
        ap_none.setChecked(true);
        id16=new int[]{R.id.QNO_1,R.id.QNO_2,R.id.QNO_3,R.id.QNO_4,R.id.QNO_5,R.id.QNO_6,R.id.QNO_7,R.id.QNO_8,R.id.QNO_9,R.id.QNO_10,R.id.QNO_11,R.id.QNO_12,R.id.QNO_13,R.id.QNO_14,R.id.QNO_15,R.id.QNO_16};
        id24=new int[]{R.id.QNO_1,R.id.QNO_2,R.id.QNO_3,R.id.QNO_4,R.id.QNO_5,R.id.QNO_6,R.id.QNO_7,R.id.QNO_8,R.id.QNO_9,R.id.QNO_10,R.id.QNO_11,R.id.QNO_12,R.id.QNO_13,R.id.QNO_14,R.id.QNO_15,R.id.QNO_16,R.id.QNO_17,R.id.QNO_18,R.id.QNO_19,R.id.QNO_20,R.id.QNO_21,R.id.QNO_22,R.id.QNO_23,R.id.QNO_24};
        Q_Visibilty16=new int[]{R.id.LQ1,R.id.LQ2,R.id.LQ3,R.id.LQ4,R.id.LQ5,R.id.LQ6,R.id.LQ7,R.id.LQ8,R.id.LQ9,R.id.LQ10,R.id.LQ11,R.id.LQ12,R.id.LQ13,R.id.LQ14,R.id.LQ15,R.id.LQ16};
        Q_Visibilty24=new int[]{R.id.LQ1,R.id.LQ2,R.id.LQ3,R.id.LQ4,R.id.LQ5,R.id.LQ6,R.id.LQ7,R.id.LQ8,R.id.LQ9,R.id.LQ10,R.id.LQ11,R.id.LQ12,R.id.LQ13,R.id.LQ14,R.id.LQ15,R.id.LQ16,R.id.LQ17,R.id.LQ18,R.id.LQ19,R.id.LQ20,R.id.LQ21,R.id.LQ22,R.id.LQ23,R.id.LQ24};
        Q_INVISIBLE=new int[]{R.id.LQ17,R.id.LQ18,R.id.LQ19,R.id.LQ20,R.id.LQ21,R.id.LQ22,R.id.LQ23,R.id.LQ24};
        Llayout=findViewById(R.id.SUBMIT_BUTTON);
        button=findViewById(R.id.Submit);
        spin=findViewById(R.id.spinner);
        subject=findViewById(R.id.Subject_select);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.QUESTION_T,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin.setAdapter(arrayAdapter);
        spin.setSelection(0);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option_Question=parent.getItemAtPosition(position).toString();
                Log.d("SELECTED", "onItemClick: "+option_Question);
                if(position==0){
                    Question_NONE();
                }
                if(position==1){
                    Log.d("QUESTION_", "onItemSelected: "+option_Question);
                    Question_16();
                }
                else if(position==2){
                    Question_24();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit_Answers();
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter1=ArrayAdapter.createFromResource(this,R.array.Subject,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subject.setAdapter(arrayAdapter1);
        subject.setSelection(0);
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectname=parent.getItemAtPosition(position).toString();
                subject_select=true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void selected_16(){
        Log.d("SELECTED", "selected: ");
        Answers.clear();
        for(int ids : id16){
            RadioGroup rg=(RadioGroup)findViewById(ids);
            int selectedid=rg.getCheckedRadioButtonId();
            if(selectedid == -1){
               custom_toast.ToastError("Please Select All The Answers");
               Accurate=false;
               break;
            }
            else{
            RadioButton button=findViewById(selectedid);
            Answers.add(button.getText().toString());
            Log.d("SELECTED", "selected: "+button.getText());
            Accurate=true;
                Log.d("SELECTIT", "selected: "+Accurate);

            }
        }
        if(Accurate){
            StringBuilder ansstring=new StringBuilder();
            for(String s:Answers){
                ansstring.append(s);
                ansstring.append(",");
            }
            sharedpref_answer.setAnswers(ansstring.toString());
            sharedpref_answer.setsubject(subjectname);
            Intent intent=new Intent(getApplicationContext(), com.example.omr.OMR_FUNCTION.OMR_SCANNER.class);
            startActivity(intent);

        }
    }
    public void selected_24(){
        Log.d("SELECTED", "selected: ");
        Answers.clear();
        for(int ids : id24){
            RadioGroup rg=(RadioGroup)findViewById(ids);
            int selectedid=rg.getCheckedRadioButtonId();
            if(selectedid == -1){
                custom_toast.ToastError("Please Select All The Answers");
                Accurate=false;
                break;
            }
            else{
                RadioButton button=findViewById(selectedid);
                Answers.add(button.getText().toString());
                Log.d("SELECTED", "selected: "+button.getText());
                Accurate=true;
                Log.d("SELECTIT", "selected: "+Accurate);

            }
        }
        if(Accurate){
            StringBuilder ansstring=new StringBuilder();
            for(String s:Answers){
                ansstring.append(s);
                ansstring.append(",");
            }
            sharedpref_answer.setAnswers(ansstring.toString());
            sharedpref_answer.setsubject(subjectname);
            Intent intent=new Intent(getApplicationContext(), com.example.omr.OMR_FUNCTION.OMR_SCANNER.class);
            startActivity(intent);
        }
    }
    public void Question_NONE(){
        for(int id : Q_Visibilty24){
            LinearLayout layout=(LinearLayout)findViewById(id);
            Llayout.setVisibility(View.GONE);
        }
        Llayout.setVisibility(View.GONE);
    }
    public void Question_16(){
        Log.d("QUESTION_", "Question_16: ");
        for(int id : Q_Visibilty16){
            LinearLayout layout=(LinearLayout)findViewById(id);
            layout.setVisibility(View.VISIBLE);
        }
        for(int id : Q_INVISIBLE){
            LinearLayout layout=(LinearLayout)findViewById(id);
            layout.setVisibility(View.GONE);
        }
        Llayout.setVisibility(View.VISIBLE);

    }
    public void Question_24(){
        for(int id : Q_Visibilty24){
            LinearLayout layout=(LinearLayout)findViewById(id);
            layout.setVisibility(View.VISIBLE);
        }
        Llayout.setVisibility(View.VISIBLE);
    }
    public void Submit_Answers(){
        if(subject_select){
        if(option_Question.equals("16")){
            selected_16();
            Autopassing();
            Log.d("SELECTARRAY", "Submit_Answers: "+Answers.get(2));

        }
        if(option_Question.equals("24")){
             selected_24();
             Autopassing();
        }}
        else {
            custom_toast.ToastError("PLEASE SELECT SUBJECT FIRST");
        }

    }
    public void startup(){

        new AlertDialog.Builder(this)
                .setTitle("PREVIOUS SUBMISSION")
                .setMessage("WANT TO USE PREVIOUS SUBMITTED ANSWERS?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(), com.example.omr.OMR_FUNCTION.OMR_SCANNER.class);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void Autopassing(){
        int selectedid=autopassing.getCheckedRadioButtonId();
        RadioButton ap_selected=findViewById(selectedid);
        String val_ap=ap_selected.getText().toString().toUpperCase().trim();
        Log.d("AUTO_PASS", "Autopassing: "+val_ap);
        if(val_ap.equals("NONE")){
            sharedpref_answer.setautopass("NONE");
        }
        else if(val_ap.equals("60%")){
            sharedpref_answer.setautopass("60");
            Log.d("AUTO_PASS", "Autopassing: 60");

        }
        else if(val_ap.equals("50%")){
            sharedpref_answer.setautopass("50");

        }
        else if (val_ap.equals("40%")){
            sharedpref_answer.setautopass("40");

        }

    }
}
