package com.xoxytech.ostello;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Contactus extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button b1;
    int flag = 0;
    String msg;
    private EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        b1 = (Button) findViewById(R.id.btnSubmit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msg="";
                e1=(EditText)findViewById(R.id.name);
                String name = e1.getText().toString();
                if (name.length() == 0) {
                    e1.setError("Please enter name");
                    flag = 1;
                } else {
                    msg += e1.getText() + "\n";
                }
                e1=(EditText)findViewById(R.id.email);
                String mail = e1.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!mail.matches(emailPattern)) {
                    e1.setError("Invalid email");
                    flag = 1;
                } else {
                    msg += e1.getText() + "\n";
                }

                e1=(EditText)findViewById(R.id.phone);

                String number = e1.getText().toString();
                if (number.length() < 10 || number.length() == 0) {
                    e1.setError("Invalid number");
                    flag = 1;
                } else {
                    msg += e1.getText() + "\n";
                }

                e1=(EditText)findViewById(R.id.description);
                String comment = e1.getText().toString();
                if (comment.length() == 0) {
                    e1.setError("Please enter comment");
                    flag = 1;
                } else {
                    msg += e1.getText() + "\n";
                }
                if (flag == 0) {

                    String[] to = {"ostallohostels@gmail.com"};
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
                    intent.putExtra(Intent.EXTRA_TEXT, msg);

                    try {
                        startActivity(Intent.createChooser(intent, "Send Email"));
                        // Snackbar.make(findViewById(R.id.drawer_layout), "Mail sent", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Log.d("******", "Finished sending email...");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Contactus.this, "Mail not sent", Toast.LENGTH_SHORT).show();
                    }
                   /* finally {
                        finish();
                    }*/
                }
                flag = 0;


            }
        });
    }
  /*  public void sendMail()
    {
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.filtercloseanim, R.anim.filtercloseanim);
    }

    public class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread


        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL(Config.CONTACTUS_URL+"");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(Contactus.this, "Message Sent successfully " + result, Toast.LENGTH_SHORT);
        }

    }
}
