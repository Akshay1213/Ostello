package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vstechlab.easyfonts.EasyFonts;

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

public class HostelDetails extends AppCompatActivity implements OnMapReadyCallback {
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
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getting id
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        id = bundle.getString("id");

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        for(int i=1;i<=5;i++) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.image("http://janaipackaging.com/ostello/images/"+id+"/"+i+".jpg");
            Log.d("*******","http://janaipackaging.com/ostello/images/"+id+"/"+i+".jpg");

            sliderShow.addSlider(textSliderView);
        }
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
                    textViewhostelName.setText(json_data.getString("hostelname"));
                    textViewhostelName.setTypeface(EasyFonts.captureIt(HostelDetails.this));
                    String hostelname[]=textViewhostelName.getText().toString().split("_|\\ ");
                    StringBuilder tmpstr=new StringBuilder();
                    for(i=1;i<hostelname.length;i++)
                    {
                        tmpstr.append(" "+hostelname[i]);
                    }
                    String text = "<font color='red'>"+hostelname[0]+" "+"</font>"+tmpstr;
                    textViewhostelName.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                    textViewcategory.setText( json_data.getString("category"));
                    textViewType.setText(json_data.getString("type"));
                    textViewrate.setText(json_data.getString("rate"));
                    textViewaddress.setText(json_data.getString("address"));
                    textViewcity.setText(json_data.getString("city"));
                    textViewvacancies.setText(json_data.getString("vacancy"));
                    String s[]=json_data.getString("location").split(",");
                    LatLng hostelmarker = new LatLng(Double.parseDouble(s[0]),Double.parseDouble(s[1]));
                    mMap.addMarker(new MarkerOptions().position(hostelmarker).title(textViewhostelName.getText().toString()));
                    mMap.addMarker(new MarkerOptions().position(hostelmarker).title("Marker "+json_data.getString("address")));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hostelmarker));
//                    hostelData.HostelImage= "https://upload.wikimedia.org/wikipedia/commons/e/e8/Hostel_Dormitory.jpg";

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HostelDetails.this,"Error"+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(18.6728856,73.8880155);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
