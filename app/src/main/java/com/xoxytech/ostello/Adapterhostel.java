package com.xoxytech.ostello;

/**
 * Created by akshay on 1/7/17.
 */


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;

import java.util.Collections;
import java.util.List;

public class Adapterhostel extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    List<Datahostel> data= Collections.emptyList();
    Datahostel current;
    int currentPos=0;
    private Context context;
    private LayoutInflater inflater;
    private int prevpos;
    // create constructor to innitilize context and data sent frm MainActivity
    public Adapterhostel(Context context, List<Datahostel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
//
        MyHolder myHolder = (MyHolder) holder;
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        int i = holder.getPosition();
        i = i % 2;
        i += 1;
        if (i % 2 == 0)
            animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        else
            animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        animation.setDuration(300);
        myHolder.cardView.setAnimation(animation);
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
        myHolder.textSize.setText(current.type);
        myHolder.textType.setText( current.catName);
        myHolder.textPrice.setText("Rs. " + current.price+"/-");
       // myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.white));
        myHolder.hiddenid.setText(current.id);
        Log.d("imageurl",current.HostelImage);
        // load image into imageview using glide
        Glide.with(context).load(current.HostelImage)
                .placeholder(R.drawable.sorryimagenotavailable)
                .error(R.drawable.sorryimagenotavailable)
                .into(myHolder.ivhostel);

//        TextSliderView textSliderView = new TextSliderView(context);
//        textSliderView
//
//                .image("https://images.unsplash.com/photo-1462496591979-5ba58a2ddec6?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&s=7973ca2b89e7907cb01759f4966e5189");
//
//        myHolder.sliderShow.addSlider(textSliderView);

prevpos=position;
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
        CardView cardView;
        TextView textPrice;
        TextView hiddenid;
        SliderLayout sliderShow;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            texthostelName= (TextView) itemView.findViewById(R.id.texthostelName);
            ivhostel= (ImageView) itemView.findViewById(R.id.ivhostel);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            sliderShow=(SliderLayout)itemView.findViewById(R.id.slider);
            hiddenid=(TextView)itemView.findViewById(R.id.hiddenid);
            cardView = (CardView) itemView.findViewById(R.id.layoutcardview);
        }

    }

}
