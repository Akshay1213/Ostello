package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

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

public class HostelDetails extends AppCompatActivity {
    SliderLayout sliderShow;
    TextView textViewhostelName;
    TextView textViewcategory;
    TextView textViewType;
    TextView textViewrate;
    TextView textViewaddress;
    TextView textViewcity;
    TextView textViewvacancies;
    String id;
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 25000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_details);

        //getting id
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        id = bundle.getString("id");

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description("Game of Thrones")
                .image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        sliderShow.addSlider(textSliderView);
         textViewhostelName=(TextView)findViewById(R.id.textViewHostelname);
        textViewcategory=(TextView)findViewById(R.id.textViewcategory);
        textViewType=(TextView)findViewById(R.id.textViewtype);
        textViewrate=(TextView)findViewById(R.id.textViewrate);
        textViewaddress=(TextView)findViewById(R.id.textViewaddress);
        textViewcity=(TextView)findViewById(R.id.textViewcity);
        textViewvacancies=(TextView)findViewById(R.id.textViewvacancy);
new AsyncFetch().execute();
    }
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(HostelDetails.this);
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
                Log.d("--------------------->",""+id);
                url = new URL(Config.SEARCHSPECIFICHOSTEL_URL+"?id="+id);

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

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
Log.d("*****************",result);
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data=jArray.getJSONObject(i);
                    textViewhostelName.setText("hostelname : " + json_data.getString("hostelname"));
                    textViewcategory.setText("category : " + json_data.getString("category"));
                    textViewType.setText("Type : " + json_data.getString("type"));
                    textViewrate.setText("Rate : " + json_data.getString("rate"));
                    textViewaddress.setText("Address : " + json_data.getString("address"));
                    textViewcity.setText("City : " + json_data.getString("city"));
                    textViewvacancies.setText("Vacancy : " + json_data.getString("vacancy"));
//                    hostelData.HostelImage= "https://upload.wikimedia.org/wikipedia/commons/e/e8/Hostel_Dormitory.jpg";

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HostelDetails.this,"zumka gira re"+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}
