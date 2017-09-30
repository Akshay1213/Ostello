package com.xoxytech.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Login_to_continue extends AppCompatActivity {

    long back_pressed = 0;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_continue);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_to_continue.this, Login.class);
                startActivity(intent);
            }
        });

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        ImageView iv = (ImageView) findViewById(R.id.imageViewdone);
        iv.startAnimation(animation1);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            // need to cancel the toast here
            toast.cancel();
            // code for exit
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);

        } else {
            // ask user to press back button one more time to close app
            toast = Toast.makeText(getBaseContext(), "Please Login to Continue!", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();


    }
}
