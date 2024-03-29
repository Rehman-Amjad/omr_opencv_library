package com.example.omr.RECORD;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.omr.R;
import com.example.omr.SETTER_G.STUDENT_RECORD;
import com.example.omr.SHAREDPREFRENCES.SHAREDPREF_LIST;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CUSTOMADAPTER_GRID extends RecyclerView.Adapter<CUSTOMADAPTER_GRID.ProductViewHolder>{
    String charText;
    Context mContext;
    LayoutInflater inflater;
    List<STUDENT_RECORD> modellist;
    ArrayList<STUDENT_RECORD> arrayList;
    HISTORY history;
    SHAREDPREF_LIST sharedpref;
    ArrayList<String> checked_box;

    //getting the context and product list with constructor
    public CUSTOMADAPTER_GRID(Context context, List<STUDENT_RECORD> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<STUDENT_RECORD>();
        this.arrayList.addAll(modellist);
        history=(HISTORY)context;
        checked_box=new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gridview_layout, null);
        return new ProductViewHolder(view,history);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        STUDENT_RECORD student = modellist.get(position);
        String[] croll=student.getRollno().split(",");
        //binding the data with the viewholder views
        holder.Classname.setText(student.getClassname());
        holder.Rollno.setText(croll[0]);
        holder.dategenerated.setText(String.valueOf(student.getDategenerated()));
        Picasso.get()
                .load(student.getQrref())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.qrimage);
        if(history.is_in_action){
            holder.box.setVisibility(View.VISIBLE);
            if(checked_box.contains(holder.Rollno.getText())){
                holder.box.setChecked(true);
            }
            else {
                holder.box.setChecked(false);
            }
        }
        else {
            holder.box.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView Classname, Rollno, dategenerated;
        ImageView qrimage;
        CheckBox box;
        HISTORY history;
        RelativeLayout relativeLayout;

        public ProductViewHolder(View itemView,HISTORY history) {
            super(itemView);

            Classname = itemView.findViewById(R.id.classname_layout_grid);
            Rollno = itemView.findViewById(R.id.rollno_layout_grid);
            dategenerated = itemView.findViewById(R.id.dategenerted_layout_grid);
            qrimage = itemView.findViewById(R.id.qrstudent_grid);
            relativeLayout=itemView.findViewById(R.id.listview_layout_relative_grid);
            this.history=history;
            box = itemView.findViewById(R.id.list_selection_grid);
            qrimage.setOnLongClickListener(history);
            box.setOnClickListener(this);
            qrimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedpref=new SHAREDPREF_LIST(mContext);
                    sharedpref.setRollno(modellist.get(getAdapterPosition()).getRollno());
                    sharedpref.setClassname(modellist.get(getAdapterPosition()).getClassname());
                    Intent intent=new Intent(mContext,list_click_detail.class);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

            checked_box.add(modellist.get(getAdapterPosition()).getRollno());
            history.getselection(v,getAdapterPosition());
        }
    }

    public void filter(String charText, String type) {
        this.charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            for (STUDENT_RECORD model : arrayList) {
                Log.d("MODEL", "filter: " + model.getClassname());
                if (type.equals("CLASS"))
                {
                    Log.d("CLASSM", "filter: "+model.getClassname());
                    Log.d("CLASSM", "filter:ss "+charText);
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

    public void updatearray(ArrayList<STUDENT_RECORD> students){
        for(STUDENT_RECORD student:students){
            modellist.remove(student);
        }
        notifyDataSetChanged();
    }

}
