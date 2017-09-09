package com.xoxytech.ostello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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


public class Registeration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextConfirmOtp;
    private TextView login_me;
    private AppCompatButton buttonRegister;
    private AppCompatButton buttonConfirm;
    private ProgressDialog loading;
    //Volley RequestQueue
    private RequestQueue requestQueue;

    //String variables to hold username password and phone
    private String username;
    private String password;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        login_me=(TextView)findViewById(R.id.linkSignup);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);

        //Initializing the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        //Adding a listener to button
        buttonRegister.setOnClickListener(this);

    login_me.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            startActivity(new Intent(Registeration.this,Login.class));
            onBackPressed();

        }
    });
    }

    //This method would confirm the otp
    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

//        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //Hiding the alert dialog
//
//
//                //Displaying a progressbar
//                final ProgressDialog loading = ProgressDialog.show(Registeration.this, "Authenticating", "Please wait while we check the entered code", false,false);
//
//                //Getting the user entered otp from edittext
//                final String otp = editTextConfirmOtp.getText().toString().trim();
//
//                //Creating an string request
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL+"?otp="+otp+"&username="+username,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                //if the server response is success
//                                if(response.contains("success")){
//                                    //dismissing the progressbar
//                                    loading.dismiss();
//                                    SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
//                                    editor.putString("USER_NAME", username); //username the user has entered
//                                    editor.commit();
//                                    //Starting a new activity
//                                    Toast.makeText(Registeration.this,"Congratulations Welcome to ostallo",Toast.LENGTH_SHORT);
//                                    startActivity(new Intent(Registeration.this, MainActivity.class));
//                                }else{
//                                    //Displaying a toast if the otp entered is wrong
//                                    Toast.makeText(Registeration.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
//                                    try {
//                                        //Asking user to enter otp again
//                                        confirmOtp();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
////                                alertDialog.dismiss();
////                                Toast.makeText(Registeration.this, error.getMessage()+"zak marke", Toast.LENGTH_LONG).show();
//                            }
//                        }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<String, String>();
//                        //Adding the parameters otp and username
//                        params.put(Config.KEY_OTP, otp);
//                        params.put(Config.KEY_USERNAME, username);
//                        return params;
//                    }
//                };
//
//                //Adding the request to the queue
//                requestQueue.add(stringRequest);
//            }
//
//        });

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                loading = ProgressDialog.show(Registeration.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();
                Log.e("nonsense", Config.CONFIRM_URL + "?otp=" + otp + "&fullname=" + username + "&password=" + password + "&phone=" + phone);
                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CONFIRM_URL + "?otp=" + otp + "&fullname=" + username + "&password=" + password + "&phone=" + phone,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if (response.contains("Registered Successfully")) {
                                    //dismissing the progressbar
                                    loading.dismiss();
                                    SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("USER_NAME", username); //username the user has entered
                                    editor.putString("USER_PHONE", phone);
                                    editor.commit();
                                    //Starting a new activity
                                    Toast.makeText(Registeration.this,"Congratulations Welcome to ostallo",Toast.LENGTH_SHORT);
                                    startActivity(new Intent(Registeration.this, MainActivity.class));
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                    Toast.makeText(Registeration.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
//                                Toast.makeText(Registeration.this, error.getMessage()+"zak marke", Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_USERNAME, username);
                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

    //this method will register the user
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        if(PhoneNumberUtils.isGlobalPhoneNumber(phone)) {

            Log.d("harami sala", username + password + phone);
            //Again creating the string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL + "?phone=" + phone,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Log.d("Zakmarya", response.toString());
                            try {
                                Toast.makeText(Registeration.this, response, Toast.LENGTH_LONG).show();
                                Log.d("wtf", response);
                                //Creating the json object from the response
//                            JSONObject jsonResponse = new JSONObject(response);

                                //If it is success
                                //if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                if (response.contains("otp sent")) {
                                    //Asking user to confirm otp
                                    Toast.makeText(Registeration.this, "awaiting for otp", Toast.LENGTH_LONG).show();
                                    confirmOtp();
                                } else {
                                    //If not successful user may already have registered
                                    Toast.makeText(Registeration.this, "Username or Phone number already registered", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            if (error == null || error.networkResponse == null)
                                return;
                            Toast.makeText(Registeration.this, "ohh god error   ", Toast.LENGTH_LONG).show();
                            Toast.makeText(Registeration.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            String body;
                            //get status code here
                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            if (error.networkResponse != null) {
                                Log.e("Volley", "Error. HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                            if (error instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            } else if (error instanceof NoConnectionError) {
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

                                body = new String(error.networkResponse.data, "UTF-8");
                                Toast.makeText(Registeration.this, body, Toast.LENGTH_LONG).show();
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
                    params.put("phone", phone);
                    return super.getParams();
                }
            };

//        Toast.makeText(Registeration.this, stringRequest.toString(), Toast.LENGTH_LONG).show();
            //Adding request the the queue
            requestQueue.add(stringRequest);
        }
        else
        {
            loading.dismiss();
            Toast.makeText(Registeration.this,"Please enter valid Mobile number",Toast.LENGTH_LONG);
        }
        loading.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        //Calling register method on register button click
        EditText ed=(EditText)findViewById(R.id.editTextUsername);
        if (ed.length()>=3) {
            ed=(EditText)findViewById(R.id.editTextPassword);
            if (ed.length()>3) {
                ed=(EditText)findViewById(R.id.editTextPhone);
                if (ed.length()==10) {

                    register();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.registerlayout), "Please enter valid 10 digit Mobile number", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
            else
            {
                Snackbar.make(findViewById(R.id.registerlayout), "Minimum length of password should be greater than four", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.registerlayout), "Minimum length of username should be greater than three", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }
}