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
import android.view.View;
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

public class Favourite extends AppCompatActivity {

    RecyclerView favouriteList;
    ArrayList<Datahostel> data;
    Adapterhostel adapterhostel;
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        favouriteList = (RecyclerView) findViewById(R.id.favouriteList);
        data = new ArrayList<>();
        //  favouriteList.setVisibility(View.VISIBLE);
        favouriteList.setLayoutManager(new LinearLayoutManager(Favourite.this));
        // adapterhostel = new Adapterhostel(Favourite.this, data);
//        favouriteList.setAdapter(adapterhostel);
        new AsyncFetchLoadHostels().execute();
    }

    private class AsyncFetchLoadHostels extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Favourite.this);
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

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                phone = sp.getString("USER_PHONE", null);
                url = new URL(Config.FAVOURITES_URL + "?phone=" + phone);

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
            if (result.contains("no rows")) {
                findViewById(R.id.textViewError).setVisibility(View.VISIBLE);
                pdLoading.dismiss();
                return;
            } else
                findViewById(R.id.textViewError).setVisibility(View.INVISIBLE);
            Log.d("*******************", result);
            pdLoading.dismiss();
            data = new ArrayList<>();
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
                adapterhostel = new Adapterhostel(Favourite.this, data);
                favouriteList.setAdapter(adapterhostel);
                adapterhostel.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Favourite.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
