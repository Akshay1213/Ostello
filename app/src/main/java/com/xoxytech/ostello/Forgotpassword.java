package com.xoxytech.ostello;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Forgotpassword extends AppCompatActivity {
    String phone;
    private Button buttonsendotp, buttonConfirm;
    private EditText editTextphone;
    private ProgressDialog loading;
    private EditText editTextConfirmOtp;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        buttonsendotp = (Button) findViewById(R.id.buttonsendotp);
        editTextphone = (EditText) findViewById(R.id.editTextPhone);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        buttonsendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = editTextphone.getText().toString();
                sendotp();
            }
        });

    }

    private void confirmOtp() throws JSONException {

        LayoutInflater li = LayoutInflater.from(this);

        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);


        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(confirmDialog);


        final AlertDialog alertDialog = alert.create();


        alertDialog.show();


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                loading = ProgressDialog.show(Forgotpassword.this, "Authenticating", "Please wait while we check the entered OTP", false, false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();
                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.VERIFYOTP_URL + "?phone=" + phone + "&otp=" + otp,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if (response.contains("OTP verified successfully")) {
                                    Intent intent_confirm_password = new Intent(Forgotpassword.this, Confirm_Password.class);
                                    intent_confirm_password.putExtra("phone", phone);
                                    startActivity(intent_confirm_password);
                                } else {

                                    Toast.makeText(Forgotpassword.this, "Wrong OTP Please Try Again", Toast.LENGTH_LONG).show();
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
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);

                        return params;
                    }
                };

                //Adding the request to the queue
                requestQueue.add(stringRequest);
                loading.dismiss();
            }
        });
    }


    private void sendotp() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Sending OTP", "Please wait...", false, false);
        EditText editTextphone = (EditText) findViewById(R.id.editTextPhone);

        //Getting user data

        String phone = editTextphone.getText().toString().trim();
        if (PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            //Again creating the string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOTPASSWORD_URL + "?phone=" + phone,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Log.d("Response of forgotpass", response.toString());
                            try {

                                Log.d("wtf", response);

                                if (response.contains("Phone Number is registered!")) {

                                    Toast.makeText(Forgotpassword.this, "awaiting for otp", Toast.LENGTH_LONG).show();
                                    confirmOtp();
                                } else {
                                    //If not successful user may already have registered
                                    Toast.makeText(Forgotpassword.this, "Phone number not registered", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Forgotpassword.this, "ohh god error   ", Toast.LENGTH_LONG).show();
                            Toast.makeText(Forgotpassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(Forgotpassword.this, body, Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                // exception
                            }
                        }
                    });

//        Toast.makeText(Registeration.this, stringRequest.toString(), Toast.LENGTH_LONG).show();
            //Adding request the the queue
            requestQueue.add(stringRequest);
        } else {
            loading.dismiss();
            Toast.makeText(Forgotpassword.this, "Please enter valid Mobile number", Toast.LENGTH_LONG);
        }
        loading.dismiss();
    }
}
