package com.xoxytech.ostello;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by akshay on 14/8/17.
 */

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder holder,boolean goesdown)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(holder.itemView,"translationY",goesdown==true?200:-200,0);
        Log.d("****going",""+(goesdown==true?200:-200));
        objectAnimator.setDuration(500);
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }
}
