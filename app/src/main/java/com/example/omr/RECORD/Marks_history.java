package com.example.omr.RECORD;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.omr.R;
import com.example.omr.SETTER_G.OMR;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Marks_history extends RecyclerView.Adapter<Marks_history.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<OMR> markshistory;

    //getting the context and product list with constructor
    public Marks_history(Context mCtx, List<OMR> markshistory) {
        this.mCtx = mCtx;
        this.markshistory = markshistory;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_marks_history, null);
       // View view = inflater.inflate(R.layout.layout_marks_history, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        OMR marks = markshistory.get(position);

        //binding the data with the viewholder views
        holder.textViewDate.setText(marks.getDate());
        holder.textViewTotalmarks.setText(marks.getTotal_Marks());
        holder.textViewMarksObtained.setText(marks.getMarks_Obtain());

    }


    @Override
    public int getItemCount() {
        return markshistory.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewTotalmarks, textViewMarksObtained;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.marks_date);
            textViewTotalmarks = itemView.findViewById(R.id.total_marks);
            textViewMarksObtained = itemView.findViewById(R.id.marks_obtained);
        }


    }
}

