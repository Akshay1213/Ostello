package com.xoxytech.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Confirm_Password extends AppCompatActivity {


    int flag = 0;
    String pwd;
    String phone1;
    String confirm_pwd;
    RequestQueue queue;
    private EditText edtPwd, edtConfirmPwd;
    private AppCompatButton imgReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm__password);
        queue = Volley.newRequestQueue(getApplicationContext());

        edtPwd = (EditText) findViewById(R.id.edtPwd);
        edtConfirmPwd = (EditText) findViewById(R.id.edtConfirmPassword);
        imgReset = (AppCompatButton) findViewById(R.id.buttonReset);


        imgReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                pwd = edtPwd.getText().toString();
                confirm_pwd = edtConfirmPwd.getText().toString();
                if (pwd.length() == 0) {
                    edtPwd.setError("Please enter password");
                    flag = 1;
                } else if (confirm_pwd.length() == 0) {
                    edtConfirmPwd.setError("Please enter password");
                    flag = 1;
                } else if (!confirm_pwd.matches(pwd)) {
                    edtConfirmPwd.setError("New password and Confirm Password Should match !!!");
                    flag = 1;
                }
                if (flag == 0) {
                    setPassword();

                }

            }
        });


    }

    public void setPassword() {
        phone1 = getIntent().getStringExtra("phone");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESETPASSWORD_URL + "?phone" + phone1 + ",&password" + pwd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Password changed", response + Config.RESETPASSWORD_URL + "?phone=" + phone1 + "&password=" + pwd);
                Intent intent = new Intent(Confirm_Password.this, Login_to_continue.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error == null || error.networkResponse == null)
                    return;

                String body;
                Log.d("response", error.getMessage());
                //get status code here

                try {
                    body = new String(error.networkResponse.data, "UTF-8");
//
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return super.getParams();
            }
        };

        queue.add(stringRequest);
    }


}