package com.example.omr.RECORD;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omr.R;
import com.example.omr.SETTER_G.OMR;
import com.example.omr.SETTER_G.STUDENT_RECORD;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_LIST;
import com.example.omr.SL_STUDENT.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class list_click_detail extends AppCompatActivity {
    SHAREDPREF_LIST sharedprefrences;
    private TextView name,classname;
    private ImageView qr_image_std;
    private String rollno,classname_std,qrref,cname;
    private String[] name_std;
    private ArrayList<String> subject_list;
    private Spinner select_subject;
    private String selected_subject;
    private RecyclerView marks_list;
    private LinearLayout marks_history_titles;
    private Button signout_student;
    Marks_history adapter;
    ArrayAdapter<String> adapter_spinner;
    ArrayList<OMR> omr_marks=new ArrayList<>();
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_click_detail);
        sharedprefrences=new SHAREDPREF_LIST(getApplicationContext());
        rollno=sharedprefrences.getRollno();
        cname=sharedprefrences.getClassname();
        name=findViewById(R.id.name_st);
        classname=findViewById(R.id.classname_item);
        signout_student=findViewById(R.id.signout_student);
        marks_history_titles=findViewById(R.id.marks_history);
        qr_image_std=findViewById(R.id.qr_image_std);
        select_subject=findViewById(R.id.Course_list);
        marks_list= findViewById(R.id.marks_list);
        subject_list= new ArrayList<>();
        marks_list.setHasFixedSize(true);
        marks_list.setLayoutManager(new GridLayoutManager(this,1));
       // marks_list.setLayoutManager(new LinearLayoutManager(this));
        ref= FirebaseDatabase.getInstance().getReference("Students");
        Log.d("ROLLNO", "onCreate: "+cname);
        if(sharedprefrences.getuser().equals("STUDENT")){
            Log.d("CHECK_STUDENT_LOGIN", "onCreate: ");
        if (!sharedprefrences.get_Student_rollno().equals("false")){
            sharedprefrences.setRollno(sharedprefrences.get_Student_rollno());
            Log.d("CHECK_STUDENT_LOGIN", "onCreate: list_student"+sharedprefrences.get_student_Classname());
            rollno=sharedprefrences.get_Student_rollno();
            cname=sharedprefrences.get_student_Classname();
            Log.d("CHECK_STUDENT_LOGIN", "onCreate: list_student"+rollno);
        }}

       if(!rollno.equals("false")){
           if(sharedprefrences.getuser().equals("STUDENT")){
               Log.d("CHECK_STUDENT_LOGIN", "onCreate: list_"+rollno);
               signout_student.setVisibility(View.VISIBLE);
           }
           Load_values_database();
           loadsubjects();
           //ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.catagory,android.R.layout.simple_list_item_1);

           adapter_spinner =
                   new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item,subject_list);
           //arrayAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line);
           select_subject.setAdapter(adapter_spinner);

           Log.d("RECORDCHEK", "onCreate: ");
       }
        select_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_subject=parent.getItemAtPosition(position).toString();
                Log.d("RECORDCHEK", "onItemSelected: "+selected_subject);
                loadadapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("RECORDCHEK", "onNothingSelected: ");

            }
        });
       signout_student.setOnClickListener(this::signout);
    }
    public void Load_Values(){
        Log.d("ROLLNO", "Load_Values: "+name_std);
        name.setText(name_std[0]);
        classname.setText(classname_std);
        Picasso.get().load(qrref).fit().centerCrop().into(qr_image_std);}

    public boolean Load_values_database() {
        final boolean[] result = {false};
        Log.d("ROLLNO","HER "+cname);
        ref.child(cname).orderByChild("rollno").equalTo(rollno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ROLLNO", "onDataChange: "+dataSnapshot.hasChildren());
                Log.d("ROLLNO","HER "+dataSnapshot.getValue());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        STUDENT_RECORD student=postSnapshot.getValue(STUDENT_RECORD.class);
                        name_std=student.getRollno().split(",");
                        classname_std=student.getClassname();
                        qrref=student.getQrref();
                        result[0] =true;
                        Load_Values();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return result[0];
    }

    public void loadsubjects(){
        ref.child(cname).child(rollno).child("Subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    marks_history_titles.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    subject_list.add(postSnapshot.getKey().toString());
                }
                adapter_spinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void loadadapter(){
        omr_marks.clear();
        ref.child(cname).child(rollno).child("Subject").child(selected_subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    OMR omr=postsnapshot.getValue(OMR.class);
                    Log.d("RECORDCHEK", "onDataChange: "+omr.getDate());
                    omr_marks.add(omr);
                }
                adapter=new Marks_history(list_click_detail.this,omr_marks);
                adapter.notifyDataSetChanged();
                marks_list.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void signout(View v){
        sharedprefrences.set_Student_rollno("false");
        sharedprefrences.setuser("false");
        Intent intent=new Intent(list_click_detail.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
     if(sharedprefrences.getuser().equals("STUDENT")){
         moveTaskToBack(false);
     }
     else {
         Intent intent=new Intent(this,HISTORY.class);
         startActivity(intent);
     }

    }
}
