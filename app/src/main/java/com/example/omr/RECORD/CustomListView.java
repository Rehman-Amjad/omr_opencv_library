package com.example.omr.RECORD;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omr.R;
import com.example.omr.SETTER_G.STUDENT_RECORD;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomListView extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<STUDENT_RECORD> modellist;
    ArrayList<STUDENT_RECORD> arrayList;

    //constructor
    public CustomListView(Context context, List<STUDENT_RECORD> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<STUDENT_RECORD>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
        TextView Classname, Rollno, dategenerated;
        ImageView qrimage;
        CheckBox box;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int postition, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            inflater=(LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_layout, null);

            //locate the views in row.xml
            holder.Classname = view.findViewById(R.id.classname_layout);
            holder.Rollno = view.findViewById(R.id.rollno_layout);
            holder.dategenerated = view.findViewById(R.id.dategenerted_layout);
            holder.qrimage = view.findViewById(R.id.qrstudent);
            holder.box=view.findViewById(R.id.list_selection);
            holder.box.setTag(holder);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //set the results into textviews
        holder.Classname.setText(modellist.get(postition).getClassname());
        holder.Rollno.setText(modellist.get(postition).getRollno());
        holder.dategenerated.setText(modellist.get(postition).getDategenerated());
        //set the result in imageview
        Picasso.get().load(modellist.get(postition).getQrref()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.qrimage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get()
                        .load(modellist.get(postition).getQrref())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(holder.qrimage);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.box.setVisibility(View.VISIBLE);
            }
        });



        return view;
    }

    //filter
    String charText;

    public void filter(String charText, String type) {
        this.charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            String modelexist;

            for (STUDENT_RECORD model : arrayList) {
                Log.d("MODEL", "filter: " + model.getClassname());
                if (type.equals("CLASS"))
                {
                    if(model.getClassname().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);}
                } else if (type.equals("ROLL NO"))
                {
                   if(model.getRollno().toLowerCase(Locale.getDefault()).contains(charText)){
                    modellist.add(model);}
                } else if (type.equals("DATE GENERATED")) {
                    if (model.getDategenerated().toLowerCase(Locale.getDefault())
                            .contains(charText)){
                        modellist.add(model);}
                } else {
                    Log.d("MODEL", "filter: " + type);
                    Toast.makeText(mContext, "First Select Option", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void checkboxenable(){
        notifyAll();
    }
}