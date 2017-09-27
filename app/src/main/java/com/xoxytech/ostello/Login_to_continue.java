package com.xoxytech.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Login_to_continue extends AppCompatActivity {

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
}
