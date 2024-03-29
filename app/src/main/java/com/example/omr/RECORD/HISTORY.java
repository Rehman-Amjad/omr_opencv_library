package com.example.omr.RECORD;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omr.DASHBOARD;
import com.example.omr.DATABASE.todatabase;
import com.example.omr.R;
import com.example.omr.SETTER_G.STUDENT_RECORD;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREFRENCES_SWITCH;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class HISTORY extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnLongClickListener {

    private RecyclerView lst;
    private CustomAdapter adapter;
    private CUSTOMADAPTER_GRID adapter2;
    String get_theme;
    private SHAREDPREFRENCES_SWITCH sharedprefrences_switch;
    DatabaseReference ref;
    ArrayList<STUDENT_RECORD> arrayList = new ArrayList<>();
    ArrayList<STUDENT_RECORD> selectionlist=new ArrayList<>();
    private Spinner cata;
    SearchView search;
    private String Text,list_type;
    Button delete;
    Boolean is_in_action=false;
    private TextView counter;
    private int mark_counter;
    private LinearLayout action_options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Initialize Variable
        lst=findViewById(R.id.history_list);
        counter=findViewById(R.id.select_counter);
        cata=findViewById(R.id.catagories_list);
        action_options=findViewById(R.id.action_options);
        search=findViewById(R.id.list_search);
        delete=findViewById(R.id.delete);
        change_theme();
        //SHARED PREFRENCES INITIALIZATION
        sharedprefrences_switch=new SHAREDPREFRENCES_SWITCH(getApplicationContext());
        // Load Value for Search Category
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.catagory,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        cata.setAdapter(arrayAdapter);  // Assign value to adapter
        //Get Stored Values using SharedPrefrences
        list_type=sharedprefrences_switch.getstyle();
        //Set RecyclerView Item Size
        lst.setHasFixedSize(true);
        if(list_type.equals("list")){
        lst.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            lst.setLayoutManager(new GridLayoutManager(this,3));
        }
        cata.setOnItemSelectedListener(this);
        retrive_from_database(); // Retrive Data from Database

        //On Searching Students
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("TEXT", "onQueryTextChange: "+Text);
                if (TextUtils.isEmpty(s)){
                    if(list_type.equals("list")){
                    adapter.filter("",Text);}
                    else {
                        adapter2.filter("",Text);
                    }
                }
                else {
                    String value=s.toLowerCase(Locale.getDefault());
                    if(list_type.equals("list")){
                    adapter.filter(value,Text);}
                    else {
                        adapter2.filter(value,Text);
                    }
                }
                return true;
            }
        });

        delete.setOnClickListener(this::delete_student_verify); //To Delete Students

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Text=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Long Click (For Print and Delete)
    @Override
    public boolean onLongClick(View v) {
        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_enter);
        action_options.startAnimation(anim);
        counter.startAnimation(anim);
        action_options.setVisibility(View.VISIBLE);
        counter.setVisibility(View.VISIBLE);
        is_in_action=true;
        if(list_type.equals("list")){
        adapter.notifyDataSetChanged();}
        else{
            adapter2.notifyDataSetChanged();
        }
        return true;
    }

    //To Get All the Selected Items
    public void getselection(View view,int position){

        if(((CheckBox)view).isChecked()){
            selectionlist.add(arrayList.get(position));
            mark_counter++; //Count Sleected Items
            counter.setText(mark_counter+" Student Selected");
        }
        else{
            selectionlist.remove(arrayList.get(position));
            mark_counter--;
            counter.setText(mark_counter+" Student Selected");

        }
    }

    //Function to Delete Students
    public void deletestudent(){
        todatabase db=new todatabase(ref,HISTORY.this);
        Boolean result=db.removestudent(selectionlist);
        Intent intent=new Intent(this,HISTORY.class);
        startActivity(intent);
        onBackPressed();
    }

    //Retrive Values from Database

    public void retrive_from_database(){
        String User= FirebaseAuth.getInstance().getUid();
        ref= FirebaseDatabase.getInstance().getReference("Students");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot psnapshot: postSnapshot.getChildren()){
                        STUDENT_RECORD student=psnapshot.getValue(STUDENT_RECORD.class);
                        Log.d("USER_ID_CHECK", "onDataChange: "+student.getQrgenerated_user());
                        if(student.getQrgenerated_user().equals(User)){
                            Log.d("USER_ID_CHECK", "onDataChange: "+student.getQrgenerated_user());
                        arrayList.add(student);}
                    }}
                if (list_type.equals("list")) {
                    adapter = new CustomAdapter(HISTORY.this, arrayList); //Set Custom Adapter
                    lst.setAdapter(adapter);
                }else {
                    adapter2=new CUSTOMADAPTER_GRID(HISTORY.this,arrayList);
                    lst.setAdapter(adapter2);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if(is_in_action){
            is_in_action=false;
            Log.d("BACKPRESS", "onBackPressed: ");
            action_options.setVisibility(View.GONE);
            counter.setVisibility(View.GONE);
            if(list_type.equals("list")){
            adapter.notifyDataSetChanged();}
            else {
                adapter2.notifyDataSetChanged();
            }
        }
        else{
            Intent intent=new Intent(this, DASHBOARD.class);
            startActivity(intent);
        }
    }

    public void change_theme(){
        sharedprefrences_switch=new SHAREDPREFRENCES_SWITCH(getApplicationContext());
        get_theme=sharedprefrences_switch.gettheme();
        if(get_theme.equals("dark")){
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public void delete_student_verify(View v){
        new AlertDialog.Builder(this)
                .setTitle("DELETE STUDENT")
                .setMessage("DELETED STUDENT CANNOT BE RECOVERED. DO YOU REALY WANT TO DELETE STUDENT?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletestudent();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("CANCEL", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
