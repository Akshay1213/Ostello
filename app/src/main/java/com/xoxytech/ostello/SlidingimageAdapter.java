package com.xoxytech.ostello;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by user on 19/9/17.
 */

public class SlidingimageAdapter extends PagerAdapter {

    ImageView image;
    private ArrayList<Imagemodel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public SlidingimageAdapter(Context context, ArrayList<Imagemodel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimage, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageview);


        //imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        //imageView.setImageDrawable(Drawable.createFromPath(imageModelArrayList.get(position).getImage_drawable()));
        Glide.with(context).load(imageModelArrayList.get(position).getImage_drawable())
                .placeholder(R.drawable.sorryimagenotavailable)
                .error(R.drawable.sorryimagenotavailable)
                .into(imageView);
        view.addView(imageLayout, 0);
        //Glide.with(this).load(imageView.setImageDrawable(Drawable.createFromPath(imageModelArrayList.get(position).getImage_drawable()))).into(R.layout.slidingimage);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
