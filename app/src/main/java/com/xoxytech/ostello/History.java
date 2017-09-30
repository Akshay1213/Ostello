package com.xoxytech.ostello;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.xoxytech.ostello.MainActivity.CONNECTION_TIMEOUT;
import static com.xoxytech.ostello.MainActivity.READ_TIMEOUT;

public class History extends AppCompatActivity {

    RecyclerView HistoryList;
    ArrayList<Datahostel> data;
    Adapterhostel adapterhostel;
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        HistoryList = (RecyclerView) findViewById(R.id.HistoryList);
        data = new ArrayList<>();
        //  HistoryList.setVisibility(View.VISIBLE);
        HistoryList.setLayoutManager(new LinearLayoutManager(History.this));
        // adapterhostel = new Adapterhostel(History.this, data);
//        HistoryList.setAdapter(adapterhostel);
        new AsyncFetchLoadHostels().execute();
    }

    private class AsyncFetchLoadHostels extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(History.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                String history = sp.getString("HISTORY", null);
                url = new URL(Config.History_URL + "?hostel_ids=" + history);

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {

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

                    return ("unsuccessful");
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

            //this method will be running on UI thread
            Log.d("*******************", result);
            pdLoading.dismiss();
            data = new ArrayList<>();
            if (result.contains("no rows")) {
                Toast.makeText(History.this, "No Recent History", Toast.LENGTH_LONG).show();
                return;
            }
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Datahostel hostelData = new Datahostel();
                    hostelData.HostelImage = "http://ostallo.com/ostello/images/" + json_data.getString("hostel_id") + "/home.jpg";
                    Log.d("******->", "http://ostallo.com/ostello/images/" + json_data.getString("hostel_id") + "/home.jpg");
                    hostelData.HostelName = json_data.getString("hostelname");
                    hostelData.HostelImage = hostelData.HostelImage.replace('_', ' ');
                    hostelData.catName = json_data.getString("category");
                    hostelData.type = json_data.getString("type");
                    hostelData.price = json_data.getInt("rate");
                    hostelData.id = json_data.getString("hostel_id");
                    hostelData.facilities = json_data.getString("facilities");
                    hostelData.views = json_data.getInt("views");
                    hostelData.likes = json_data.getInt("likes");
                    hostelData.dislikes = json_data.getInt("dislikes");
                    data.add(hostelData);
                }
                Log.d("Data", data.size() + "");
                adapterhostel = new Adapterhostel(History.this, data);
                HistoryList.setAdapter(adapterhostel);
                adapterhostel.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(History.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
