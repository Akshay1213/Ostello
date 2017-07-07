package com.xoxytech.ostello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.List;


public class Loadhostels extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 25000;
    private RecyclerView mRVhostelList;
    private Adapterhostel mAdapter;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadhostels);



        //getting id
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        city = bundle.getString("city");

        List<Datahostel> data=new ArrayList<>();

            // Extract data from json and store into ArrayList as class objects
            for(int i=0;i<10;i++){
                Datahostel hostelData = new Datahostel();
                hostelData.HostelImage= "https://upload.wikimedia.org/wikipedia/commons/e/e8/Hostel_Dormitory.jpg";
                hostelData.HostelName= "hostelname"+i;
                hostelData.catName="B";
                hostelData.type= "cot_basis";
                hostelData.price= 1500;
                data.add(hostelData);
            }
            // Setup and Handover data to recyclerview
            mRVhostelList = (RecyclerView)findViewById(R.id.hostelList);
//                LinearLayoutManager llm = new LinearLayoutManager(Loadhostels.this);
//                llm.setOrientation(LinearLayoutManager.VERTICAL);

            mAdapter = new Adapterhostel(Loadhostels.this, data);
            mRVhostelList.setAdapter(mAdapter);
            mRVhostelList.setLayoutManager(new LinearLayoutManager(Loadhostels.this));
            mAdapter.notifyDataSetChanged();


        mRVhostelList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view.findViewById(R.id.hiddenid);

                Toast.makeText(Loadhostels.this, "Card at " + position + " is clicked"+tv.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Loadhostels.this, HostelDetails.class);

//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("id", tv.getText().toString());

//Add the bundle to the intent
                i.putExtras(bundle);

//Fire that second activity
                startActivity(i);
            }
        }));


        //Make call to AsyncTask
        new AsyncFetch().execute();
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Loadhostels.this);
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
                url = new URL(Config.FETCHONCLICKHOSTELS_URL+"?city="+city);

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
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

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
            Log.d("*******************",result);
            pdLoading.dismiss();
            List<Datahostel> data=new ArrayList<>();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Datahostel hostelData = new Datahostel();
                    hostelData.HostelImage= "https://upload.wikimedia.org/wikipedia/commons/e/e8/Hostel_Dormitory.jpg";
                    hostelData.HostelName= json_data.getString("hostelname");
                    hostelData.catName= json_data.getString("category");
                    hostelData.type= json_data.getString("type");
                    hostelData.price= json_data.getInt("rate");
                    hostelData.id=json_data.getString("id");
                    data.add(hostelData);
                }

                // Setup and Handover data to recyclerview
                mRVhostelList = (RecyclerView)findViewById(R.id.hostelList);
//                LinearLayoutManager llm = new LinearLayoutManager(Loadhostels.this);
//                llm.setOrientation(LinearLayoutManager.VERTICAL);

                mAdapter = new Adapterhostel(Loadhostels.this, data);
                mRVhostelList.setAdapter(mAdapter);
                mRVhostelList.setLayoutManager(new LinearLayoutManager(Loadhostels.this));
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Loadhostels.this,"zumka gira re"+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}
