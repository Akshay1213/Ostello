package com.xoxytech.ostello;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapterhostel extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    protected boolean amuser = true;
    List<Datahostel> data= Collections.emptyList();
    Datahostel current;
    int currentPos=0;
    RequestQueue requestQueue;
    String hostel_id = "", phone = "", status = "", body;
    MyHolder myHolder;
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

//        hostel_id = myHolder.hiddenid.getText().toString();

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        phone = myHolder.sp.getString("USER_PHONE", null);
        requestQueue = Volley.newRequestQueue(context);

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
        setLikeDislike(myHolder);
        Log.d("imageurl",current.HostelImage);
        // load image into imageview using glide
        Glide.with(context).load(current.HostelImage).asBitmap().override(600, 600)
                .placeholder(R.drawable.sorryimagenotavailable)
                .error(R.drawable.sorryimagenotavailable)
                .into(myHolder.ivhostel);


        prevpos = position;
        //setLikeDislike(myHolder);

        myHolder.ivhostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//             Toast.makeText(NewMenu.this, "Card at " + position + " is clicked"+tv.getText(), Toast.LENGTH_SHORT).show();
                if (CheckInternet.checkinternet(context)) {
                    Intent i = new Intent(context, HostelDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", myHolder.hiddenid.getText().toString());
                    i.putExtras(bundle);
                    context.startActivity(i);
                } else {
                    Toast.makeText(context, "Make sure you have Active Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });
        myHolder.toggleCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myHolder.toggleCall.isChecked()) {

                    phone = myHolder.sp.getString("USER_PHONE", null);
                    Log.d("Phone", phone);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));

                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        context.startActivity(callIntent);
                    }


                }
            }
        });
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animation1 = AnimationUtils.loadAnimation(context,
                        R.anim.bounce);

                if (compoundButton == myHolder.toggleLike && isuser()) {
                    if (myHolder.toggleLike.isChecked()) {
                        myHolder.toggleDislike.setChecked(false);
                        myHolder.toggleLike.setChecked(true);
                        myHolder.textLikeCount.setText("" + (Integer.parseInt(myHolder.textLikeCount.getText().toString()) + 1));
                        status = "1";
                        compoundButton.startAnimation(animation1);
                        Log.d("s", status);
                        setStatus(status, myHolder);



                        // TODO: 22/9/17 make inc req to inc like
                    } else {
                        myHolder.toggleLike.setChecked(false);
                        myHolder.textLikeCount.setText("" + (Integer.parseInt(myHolder.textLikeCount.getText().toString()) - 1));
                        status = "0";
                        setStatus(status, myHolder);

                        //// TODO: 22/9/17 make derement request to like
                    }
                }
                if (compoundButton == myHolder.toggleDislike && isuser()) {
                    if (myHolder.toggleDislike.isChecked()) {
                        myHolder.toggleLike.setChecked(false);
                        compoundButton.startAnimation(animation1);
                        myHolder.toggleDislike.setChecked(true);
                        myHolder.textDislikeCount.setText("" + (Integer.parseInt(myHolder.textDislikeCount.getText().toString()) + 1));
                        status = "-1";
                        setStatus(status, myHolder);

                       /* int imgResource = R.drawable.unlike_active;
                        myHolder.toggleDislike.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);*/

                    } else {
                        myHolder.toggleDislike.setChecked(false);
                        myHolder.textDislikeCount.setText("" + (Integer.parseInt(myHolder.textDislikeCount.getText().toString()) - 1));
                        status = "-2";
                        setStatus(status, myHolder);

                      /*  int imgResource = R.drawable.unlike_inactive;
                        myHolder.toggleDislike.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);*/
                    }
                }
                if (compoundButton == myHolder.toggleFavourite && isuser()) {
                    if (myHolder.toggleFavourite.isChecked()) {
                        compoundButton.startAnimation(animation1);
                        // Toast.makeText(context,"Added to favourite list",Toast.LENGTH_LONG).show();
                        status = "3";
                        setStatus(status, myHolder);
                    } else {
                        // Toast.makeText(context,"Removed from favourite list",Toast.LENGTH_LONG).show();
                        status = "4";
                        compoundButton.startAnimation(animation1);
                        setStatus(status, myHolder);
                    }
                }
            }
        };

        myHolder.toggleLike.setOnCheckedChangeListener(toggleListener);
        myHolder.toggleDislike.setOnCheckedChangeListener(toggleListener);
        myHolder.toggleFavourite.setOnCheckedChangeListener(toggleListener);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setLikeDislike(final MyHolder myHolder) {
        Log.d("Like_URL", Config.LIKESDISLIKES_URL + "?hostel_id=" + myHolder.hiddenid.getText() + "&phone=" + phone);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.LIKESDISLIKES_URL + "?hostel_id=" + myHolder.hiddenid.getText() + "&phone=" + phone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        amuser = false;
                        Log.d("ResLike***", response + myHolder.hiddenid.getText());
                        if (!response.contains("no rows found")) {
                            String[] res1 = response.split("\n");
                            String[] res = res1[1].split(" ");
                            Log.d("ResLike***", res[0] + "**********" + res[1]);
                            if (res[0].equals("1")) {
                                myHolder.toggleLike.setChecked(true);
                                //Log.d("ResLike***",res[1]+"");
                            } else if (res[0].equals("-1")) {
                                myHolder.toggleDislike.setChecked(true);
                                // Log.d("ResDislike***",res[1]+"");
                            }
                            if (res[1].equals("1")) {
                                myHolder.toggleFavourite.setChecked(true);
                                // Log.d("ResFavourite***",res[1]+"");
                            }

                        }
                        amuser = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null)


                            Log.d("response", error.getMessage());
                        //get status code here

                        /*try {

//                            body = new String(error.networkResponse.data, "UTF-8");
//
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }*/
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return super.getParams();
            }
        };
        requestQueue.add(stringRequest1);
    }

    boolean isuser() {
        return amuser;
    }

    public void setStatus(String status1, MyHolder myHolder) {

//            Log.d("Status",Config.UPDATELIKEDISLIKE_URL + "?hostel_id=" + hostel_id + "&phone=" + phone + "&status=" + status1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATELIKEDISLIKE_URL + "?hostel_id=" + myHolder.hiddenid.getText() + "&phone=" + phone + "&status=" + status1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("status", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null)


                            Log.d("Error", error.getMessage());
                        //get status code here


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return super.getParams();
            }
        };
        requestQueue.add(stringRequest);

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
        RequestQueue requestQueue;

        ToggleButton toggleCall, toggleLike, toggleDislike, toggleFavourite;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            sp = context.getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
            requestQueue = Volley.newRequestQueue(context);

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
            toggleFavourite = (ToggleButton) itemView.findViewById(R.id.toggleFavourite);
        }

    }

}
