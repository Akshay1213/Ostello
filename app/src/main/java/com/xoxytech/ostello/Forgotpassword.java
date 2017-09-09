package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;

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

        buttonsendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = editTextphone.getText().toString();

            }
        });

    }
//    private void confirmOtp() throws JSONException {
//        //Creating a LayoutInflater object for the dialog box
//        LayoutInflater li = LayoutInflater.from(this);
//        //Creating a view to get the dialog box
//        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
//
//        //Initizliaing confirm button fo dialog box and edittext of dialog box
//        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
//        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
//
//        //Creating an alertdialog builder
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        //Adding our dialog box to the view of alert dialog
//        alert.setView(confirmDialog);
//
//        //Creating an alert dialog
//        final AlertDialog alertDialog = alert.create();
//
//        //Displaying the alert dialog
//        alertDialog.show();
//
//
//        buttonConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Hiding the alert dialog
//                alertDialog.dismiss();
//
//                //Displaying a progressbar
//                loading = ProgressDialog.show(Forgotpassword.this, "Authenticating", "Please wait while we check the entered OTP", false,false);
//
//                //Getting the user entered otp from edittext
//                final String otp = editTextConfirmOtp.getText().toString().trim();
//                //Creating an string request
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESETPASSWORD_URL + "?phone=" + phone + "&password=" + password,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                //if the server response is success
//                                if (response.contains("Registered Successfully")) {
//                                    //dismissing the progressbar
//                                    loading.dismiss();
//                                    SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sp.edit();
////                                    editor.putString("USER_NAME", username); //username the user has entered
//                                    editor.putString("USER_PHONE", phone);
//                                    editor.commit();
//                                    //Starting a new activity
//                                    Toast.makeText(Forgotpassword.this,"Congratulations Welcome to ostallo",Toast.LENGTH_SHORT);
//                                    startActivity(new Intent(Forgotpassword.this, MainActivity.class));
//                                }else{
//                                    //Displaying a toast if the otp entered is wrong
//                                    Toast.makeText(Forgotpassword.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
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
//                                alertDialog.dismiss();
////                                Toast.makeText(Registeration.this, error.getMessage()+"zak marke", Toast.LENGTH_LONG).show();
//                            }
//                        }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<String, String>();
//                        //Adding the parameters otp and username
//                        params.put(Config.KEY_OTP, otp);
//
//                        return params;
//                    }
//                };
//
//                //Adding the request to the queue
//                requestQueue.add(stringRequest);
//            }
//        });
//    }


//    private void sendotp() {
//
//        //Displaying a progress dialog
//        final ProgressDialog loading = ProgressDialog.show(this, "Sending OTP", "Please wait...", false, false);
//        EditText editTextphone=(EditText)findViewById(R.id.editTextPhone);
//
//        //Getting user data
//
//        String phone = editTextphone.getText().toString().trim();
//        if(PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
//            //Again creating the string request
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOTPASSWORD_URL + "?phone=" + phone,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            loading.dismiss();
//                            Log.d("Response of forgotpass", response.toString());
//                            try {
//                                Toast.makeText(Forgotpassword.this, response, Toast.LENGTH_LONG).show();
//                                Log.d("wtf", response);
//
//                                if (response.contains("otp sent")) {
//
//                                    Toast.makeText(Forgotpassword.this, "awaiting for otp", Toast.LENGTH_LONG).show();
//                                    confirmOtp();
//                                } else {
//                                    //If not successful user may already have registered
//                                    Toast.makeText(Forgotpassword.this, "Phone number not registered", Toast.LENGTH_LONG).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            loading.dismiss();
//                            if (error == null || error.networkResponse == null)
//                                return;
//                            Toast.makeText(Forgotpassword.this, "ohh god error   ", Toast.LENGTH_LONG).show();
//                            Toast.makeText(Forgotpassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                            String body;
//                            //get status code here
//                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                            //get response body and parse with appropriate encoding
//                            if (error.networkResponse != null) {
//                                Log.e("Volley", "Error. HTTP Status Code:" + error.networkResponse.statusCode);
//                            }
//
//                            if (error instanceof TimeoutError) {
//                                Log.e("Volley", "TimeoutError");
//                            } else if (error instanceof NoConnectionError) {
//                                Log.e("Volley", "NoConnectionError");
//                            } else if (error instanceof AuthFailureError) {
//                                Log.e("Volley", "AuthFailureError");
//                            } else if (error instanceof ServerError) {
//                                Log.e("Volley", "ServerError");
//                            } else if (error instanceof NetworkError) {
//                                Log.e("Volley", "NetworkError");
//                            } else if (error instanceof ParseError) {
//                                Log.e("Volley", "ParseError");
//                            }
//                            try {
//
//                                body = new String(error.networkResponse.data, "UTF-8");
//                                Toast.makeText(Forgotpassword.this, body, Toast.LENGTH_LONG).show();
//                            } catch (UnsupportedEncodingException e) {
//                                // exception
//                            }
//                        }
//                    });
//
////        Toast.makeText(Registeration.this, stringRequest.toString(), Toast.LENGTH_LONG).show();
//            //Adding request the the queue
//            requestQueue.add(stringRequest);
//        }
//        else
//        {
//            loading.dismiss();
//            Toast.makeText(Forgotpassword.this,"Please enter valid Mobile number",Toast.LENGTH_LONG);
//        }
//        loading.dismiss();
//    }
}
