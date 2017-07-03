package com.xoxytech.ostello;

/**
 * Created by akshay on 1/7/17.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class Adapterhostel extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private LayoutInflater inflater;

    @Override
    public void onClick(View v) {

    }

    List<Datahostel> data= Collections.emptyList();

    Datahostel current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Adapterhostel(Context context, List<Datahostel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_hostel, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Datahostel current=data.get(position);
        myHolder.texthostelName.setText(current.HostelName);
        myHolder.textSize.setText("Type: " + current.type);
        myHolder.textType.setText("Category: " + current.catName);
        myHolder.textPrice.setText("Rs. " + current.price + "/person");
        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        // load image into imageview using glide
        Glide.with(context).load(current.HostelImage)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.ivhostel);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView texthostelName;
        ImageView ivhostel;
        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            texthostelName= (TextView) itemView.findViewById(R.id.texthostelName);
            ivhostel= (ImageView) itemView.findViewById(R.id.ivhostel);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
        }

    }

}
