package com.xoxytech.ostello;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements  View.OnClickListener{
    private TextView textView_NotRegistered;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private String username;
    private String password;
    private RequestQueue requestQueue;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        textView_NotRegistered=(TextView)findViewById(R.id.link_signup);
        editTextUsername=(EditText)findViewById(R.id.input_username);
        editTextPassword=(EditText)findViewById(R.id.input_password);
        login_button=(Button)findViewById(R.id.btn_login);
        requestQueue = Volley.newRequestQueue(this);
        textView_NotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Login.this,Registeration.class));
                return;
            }
        });
login_button.setOnClickListener(this);

    }

    void verify()
    {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Verifying", "Please wait...", false, false);


        //Getting user data
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString();

        Log.d("harami sala",username+password);
        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL+"?username="+username+"&password="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.d("Zakmarya", response.toString());
                        try {
//                            Toast.makeText(Login.this, "atleast got response"+response, Toast.LENGTH_LONG).show();
                            Log.d("wtf",response);
                            //Creating the json object from the response
//                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            //if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                            if(response.contains("success")){
                                //Asking user to confirm otp
                                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_NAME", username); //username the user has entered
                                editor.commit();
                                startActivity(new Intent(Login.this,Autocompletesearch.class));
                                return;

                            }else{
                                Snackbar.make(findViewById(R.id.myLoginLayout), "Invalid Login", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if(error==null|| error.networkResponse == null)
                            return;

                        Toast.makeText(Login.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        String body;
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+error.networkResponse.statusCode);
                        }

                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(error instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                        try {

                            body = new String(error.networkResponse.data,"UTF-8");
                            Toast.makeText(Login.this, body,Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                return super.getParams();
            }
        };

//        Toast.makeText(Registeration.this, stringRequest.toString(), Toast.LENGTH_LONG).show();
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_login)
        {
            verify();
        }
    }
}
