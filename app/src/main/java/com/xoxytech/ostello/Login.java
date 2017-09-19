package com.xoxytech.ostello;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements  View.OnClickListener{
    Toast toast;
    private TextView textView_NotRegistered, textView_forgot_password;
    private EditText editTextPassword;
    private EditText editTextUserphone;
    private String phone;
    private long back_pressed = 0;
    private String password;
    private RequestQueue requestQueue;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        try
        {
            File f = new File(
                    "/data/data/com.xoxytech.ostello/shared_prefs/YourSharedPreference.xml");
            if (f.exists()) {
                Log.d("TAG", "SharedPreferences Name_of_your_preference : exist");
                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                String username = sp.getString("USER_PHONE", null);
                Toast.makeText(Login.this,"Logged in using "+username,Toast.LENGTH_LONG);
                if(username!=null)
                    startActivity(new Intent(Login.this, MainActivity.class));
            }
            else
                Log.d("TAG", "Setup default preferences");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {


        textView_NotRegistered=(TextView)findViewById(R.id.link_signup);
            textView_forgot_password = (TextView) findViewById(R.id.link_forget_password);
            textView_forgot_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login.this, Forgotpassword.class));
                    return;
                }
            });
            editTextUserphone = (EditText) findViewById(R.id.input_usernumber);
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
            startActivity(intent);

        } else {
            // ask user to press back button one more time to close app
            toast = Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();


    }

    void verify()
    {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Verifying", "Please wait...", false, false);


        //Getting user data
        phone = editTextUserphone.getText().toString().trim();
        password = editTextPassword.getText().toString();
        Log.d("******->", Config.LOGIN_URL + "?phone=" + phone + "&password=" + password);


        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL + "?phone=" + phone + "&password=" + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.d("*******", response.toString());
                        try {
//                            Toast.makeText(Login.this, "atleast got response"+response, Toast.LENGTH_LONG).show();
                            Log.d("wtf",response);
                            //Creating the json object from the response
//                            JSONObject jsonResponse = new JSONObject(response);

                            //If it is success
                            //if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                            if (!response.contains("Invalid phone number or password")) {
                                //Asking user to confirm otp
                                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_PHONE", phone); //username the user has entered
                                editor.commit();
                                startActivity(new Intent(Login.this, MainActivity.class));
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

//                        Toast.makeText(Login.this, error.getMessage(),Toast.LENGTH_LONG).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
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
            if (checkInternet())
            verify();
            else
                Snackbar.make(v, "Make sure you have Active internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
        }
    }

    boolean checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();

        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo i = connMgr.getActiveNetworkInfo();

        if (wifiMgr.isWifiEnabled()) {
            //Toast.makeText(MainActivity_permissions.this, "wifi is enabled", Toast.LENGTH_SHORT).show();
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                return false;
            } else if (i.isAvailable()) {
                return true;
            }

        } else return isMobileConn;

        return false;
    }
}
