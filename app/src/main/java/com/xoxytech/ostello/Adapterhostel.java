package com.xoxytech.ostello;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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

        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        animation.setStartOffset(800);
        ((MyHolder) holder).textPrice.setAnimation(animation);
        animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setStartOffset(800);
        ((MyHolder) holder).textType.setAnimation(animation);
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
        final MyHolder myHolder = (MyHolder) holder;
        Datahostel current=data.get(position);
        myHolder.texthostelName.setText(current.HostelName);
        myHolder.textSize.setText(current.type);
        myHolder.textType.setText( current.catName);
        myHolder.textViewviews.setText(current.views + "");
        myHolder.textPrice.setText("Rs. " + current.price + "/mo.");
        myHolder.hiddenfacilities.setText(current.facilities);
        myHolder.textLikeCount.setText(current.likes + "");
        myHolder.textDislikeCount.setText(current.dislikes + "");
        // myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.white));
        myHolder.hiddenid.setText(current.id);
        Log.d("imageurl",current.HostelImage);
        // load image into imageview using glide
        Glide.with(context).load(current.HostelImage).asBitmap().override(600, 600)
                .placeholder(R.drawable.sorryimagenotavailable)
                .error(R.drawable.sorryimagenotavailable)
                .into(myHolder.ivhostel);


        prevpos = position;

        myHolder.ivhostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(NewMenu.this, "Card at " + position + " is clicked"+tv.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, HostelDetails.class);

//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("id", myHolder.hiddenid.getText().toString());

//Add the bundle to the intent
                i.putExtras(bundle);

//Fire that second activity
                context.startActivity(i);
            }

        });
        myHolder.toggleCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myHolder.toggleCall.isChecked()) {
                }


                String username = myHolder.sp.getString("USER_PHONE", null);
                Log.d("Phone", username);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + username));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);
            }
        });
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton == myHolder.toggleLike) {
                    if (myHolder.toggleLike.isChecked()) {
                        myHolder.toggleDislike.setChecked(false);
                        myHolder.toggleLike.setChecked(true);
                        myHolder.textLikeCount.setText("" + (Integer.parseInt(myHolder.textLikeCount.getText().toString()) + 1));
                        // TODO: 22/9/17 make inc req to inc like
                    } else {
                        myHolder.toggleLike.setChecked(false);

                        myHolder.textLikeCount.setText("" + (Integer.parseInt(myHolder.textLikeCount.getText().toString()) - 1));
                        //// TODO: 22/9/17 make derement request to like
                    }
                }
                if (compoundButton == myHolder.toggleDislike) {
                    if (myHolder.toggleDislike.isChecked()) {
                        myHolder.toggleLike.setChecked(false);
                        myHolder.toggleDislike.setChecked(true);
                        myHolder.textDislikeCount.setText("" + (Integer.parseInt(myHolder.textDislikeCount.getText().toString()) + 1));
                       /* int imgResource = R.drawable.unlike_active;
                        myHolder.toggleDislike.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);*/

                    } else {
                        myHolder.toggleDislike.setChecked(false);
                        myHolder.textDislikeCount.setText("" + (Integer.parseInt(myHolder.textDislikeCount.getText().toString()) - 1));
                      /*  int imgResource = R.drawable.unlike_inactive;
                        myHolder.toggleDislike.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);*/
                    }
                }

            }
        };


        myHolder.toggleLike.setOnCheckedChangeListener(toggleListener);
        myHolder.toggleDislike.setOnCheckedChangeListener(toggleListener);



    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView texthostelName, textLikeCount, textDislikeCount;
        ImageView ivhostel;
        TextView textSize;
        TextView textType, textViewviews;
        CardView cardView;
        TextView textPrice;
        TextView hiddenid, hiddenfacilities;
        SliderLayout sliderShow;
        SharedPreferences sp;
        ToggleButton toggleCall, toggleLike, toggleDislike;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
            texthostelName= (TextView) itemView.findViewById(R.id.texthostelName);
            ivhostel= (ImageView) itemView.findViewById(R.id.ivhostel);
            textViewviews = (TextView) itemView.findViewById(R.id.iveyeviews);
            textSize = (TextView) itemView.findViewById(R.id.textSize);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            sliderShow=(SliderLayout)itemView.findViewById(R.id.slider);
            hiddenid=(TextView)itemView.findViewById(R.id.hiddenid);
            hiddenfacilities = (TextView) itemView.findViewById(R.id.hiddenfacilities);
            cardView = (CardView) itemView.findViewById(R.id.layoutcardview);
            toggleLike = (ToggleButton) itemView.findViewById(R.id.toggleLike);
            toggleDislike = (ToggleButton) itemView.findViewById(R.id.toggleDislike);
            toggleCall = (ToggleButton) itemView.findViewById(R.id.toggleCall);
            textLikeCount = (TextView) itemView.findViewById(R.id.txtLikeCount);
            textDislikeCount = (TextView) itemView.findViewById(R.id.txtDislikeCount);
        }

    }

}
