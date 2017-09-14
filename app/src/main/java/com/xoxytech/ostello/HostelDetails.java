package com.xoxytech.ostello;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HostelDetails extends AppCompatActivity implements OnMapReadyCallback {
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 25000;
    SliderLayout sliderShow;
    TextView textViewhostelName;
    Button btnEnquiry;
    TextView textViewcategory;
    TextView textViewType;
    TextView textViewrate;
    TextView textViewaddress;
    TextView textViewcity;
    TextView textViewtandc;
    TextView textViewvacancies;
    String id;
    RequestQueue requestQueue;
    CardView cardSliderlayout, cardDetailslayout, cardFeatures, cardMaps, cardDesc;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //getting id
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        id = bundle.getString("id");

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        for(int i=1;i<=5;i++) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.image("http://janaipackaging.com/ostello/images/" + id + "/" + i + ".jpg");
            Log.d("*******", "http://janaipackaging.com/ostello/images/" + id + "/" + i + ".jpg");

            sliderShow.addSlider(textSliderView);
        }
        textViewhostelName=(TextView)findViewById(R.id.textViewHostelname);
        textViewcategory=(TextView)findViewById(R.id.textViewcategory);
        textViewType=(TextView)findViewById(R.id.textViewtype);
        textViewrate=(TextView)findViewById(R.id.textViewrate);
        textViewaddress=(TextView)findViewById(R.id.textViewaddress);
        textViewcity=(TextView)findViewById(R.id.textViewcity);
        textViewvacancies=(TextView)findViewById(R.id.textViewvacancy);
        textViewtandc = (TextView) findViewById(R.id.textViewtandc);
        textViewtandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 7/9/17 add dialogue box of conditions
                LayoutInflater li = LayoutInflater.from(HostelDetails.this);
                //Creating a view to get the dialog box
                View confirmDialog = li.inflate(R.layout.dialog_tandc, null);

                //Creating an alertdialog builder

                AlertDialog.Builder alert = new AlertDialog.Builder(HostelDetails.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();

                //Displaying the alert dialog
                alertDialog.show();

            }
        });


        cardSliderlayout = (CardView) findViewById(R.id.sliderlayout);
        cardDetailslayout = (CardView) findViewById(R.id.detailslayout);
        cardFeatures = (CardView) findViewById(R.id.featurescard);
        cardMaps = (CardView) findViewById(R.id.mapscard);
        cardDesc = (CardView) findViewById(R.id.cardDesc);
        btnEnquiry = (Button) findViewById(R.id.btnenquirenow);

        btnEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("nonsense", Config.ENQUIRY_URL + "?id=" + id);
                LayoutInflater li = LayoutInflater.from(HostelDetails.this);
                //Creating a view to get the dialog box
                View enquireDialog = li.inflate(R.layout.dialogue_enquirenow, null);
                final TextView txtName = (TextView) enquireDialog.findViewById(R.id.txtName);
                final RadioGroup radiogrpPhone = (RadioGroup) enquireDialog.findViewById(R.id.radioBtnGroup);
                final RadioButton radiobtnPhone1 = (RadioButton) enquireDialog.findViewById(R.id.radioBtnPhone1);
                final RadioButton radiobtnPhone2 = (RadioButton) enquireDialog.findViewById(R.id.radioBtnPhone2);
               /* final TextView txtPrimaryPhone= (TextView) enquireDialog.findViewById(R.id.txtPrimaryPhone);
                final TextView txtSecondaryPhone= (TextView) enquireDialog.findViewById(R.id.txtSecondaryPhone);*/
                Button btnCall = (Button) enquireDialog.findViewById(R.id.buttonCall);


                AlertDialog.Builder alert = new AlertDialog.Builder(HostelDetails.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(enquireDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();

                //Displaying the alert dialog
                alertDialog.show();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ENQUIRY_URL + "?id=" + id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String name = jsonObject.getString("fullname");
                                        String password = jsonObject.getString("password");
                                        String phone = jsonObject.getString("phone");
                                        String secondaryphone = jsonObject.getString("secondaryphone");
                                        txtName.setText(name);
                                        radiobtnPhone1.setText("Primary Number " + phone);
                                        radiobtnPhone2.setText("Secondary Number " + secondaryphone);
                                     /* txtPrimaryPhone.setText(phone);
                                      txtSecondaryPhone.setText(secondaryphone);*/
                                    }
                                    Log.d("Response:", response + "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // alertDialog.dismiss();
//                                Toast.makeText(Registeration.this, error.getMessage()+"zak marke", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username

                        return params;
                    }
                };


                //Adding the request to the queue
                requestQueue.add(stringRequest);
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        String number;
                        if (radiobtnPhone1.isChecked())
                            number = radiobtnPhone1.getText().toString().split(" ")[2];
                        else
                            number = radiobtnPhone2.getText().toString().split(" ")[2];

                        // =txtPrimaryPhone.getText().toString();
                        callIntent.setData(Uri.parse("tel:" + number));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);

                    }
                });
            }

        });

        new AsyncFetch().execute();
    }
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(18.6728856, 73.8880155);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void inittoggle_facilities(String facilities) {
        int i = 0;
        ToggleButton[] tb = new ToggleButton[15];
        tb[0] = (ToggleButton) findViewById(R.id.toggleElevator);
        tb[1] = (ToggleButton) findViewById(R.id.toggleDrinkingwater);
        tb[2] = (ToggleButton) findViewById(R.id.toggleCot);
        tb[3] = (ToggleButton) findViewById(R.id.togglecctv);
        tb[4] = (ToggleButton) findViewById(R.id.toggleAc);
        tb[5] = (ToggleButton) findViewById(R.id.toggleElectricity);
        tb[6] = (ToggleButton) findViewById(R.id.toggleGym);
        tb[7] = (ToggleButton) findViewById(R.id.toggleHotwater);
        tb[8] = (ToggleButton) findViewById(R.id.toggleTV);
        tb[9] = (ToggleButton) findViewById(R.id.toggleCleaning);
        tb[10] = (ToggleButton) findViewById(R.id.toggleParking);
        tb[11] = (ToggleButton) findViewById(R.id.toggleWashingmachine);
        tb[12] = (ToggleButton) findViewById(R.id.toggleMess);
        tb[13] = (ToggleButton) findViewById(R.id.toggleStudytable);
        tb[14] = (ToggleButton) findViewById(R.id.toggleWifi);

        while (i < 15) {
            if (facilities.charAt(i) == '1') {
                tb[i].setChecked(true);
                Log.d("********", "" + i + ") " + facilities.charAt(i));

            }
            i++;
        }


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
//                    textViewhostelName.setTypeface(EasyFonts.caviarDreamsBold(HostelDetails.this));
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
                    inittoggle_facilities(json_data.getString("facilities"));
                    String s[]=json_data.getString("location").split(",");
                    LatLng hostelmarker = new LatLng(Double.parseDouble(s[0]),Double.parseDouble(s[1]));
                    mMap.addMarker(new MarkerOptions().position(hostelmarker).title(textViewhostelName.getText().toString()));
                    mMap.addMarker(new MarkerOptions().position(hostelmarker).title("Marker "+json_data.getString("address")));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(hostelmarker));
//                    hostelData.HostelImage= "https://upload.wikimedia.org/wikipedia/commons/e/e8/Hostel_Dormitory.jpg";
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layouthostelcontainer);
                    relativeLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    int x = 300;
                    animation.setStartOffset(x);
                    // animation.setDuration(500);
                    Animation animation1 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation1.setStartOffset(x + 200);
                    // animation1.setDuration(500);
                    Animation animation2 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation2.setStartOffset(x + 400);
                    //  animation2.setDuration(500);
                    Animation animation3 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation3.setStartOffset(x + 600);
                    // animation3.setDuration(500);
                    Animation animation4 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation4.setStartOffset(x + 900);
                    Animation animation5 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation5.setStartOffset(x + 1100);
                    Animation animation6 = AnimationUtils.loadAnimation(HostelDetails.this, R.anim.slide_in_right);
                    animation6.setStartOffset(x + 1300);
                    // animation4.setDuration(500);

                    textViewhostelName.setAnimation(animation);
                    cardSliderlayout.startAnimation(animation1);
                    cardDetailslayout.startAnimation(animation2);
                    cardFeatures.startAnimation(animation3);
                    cardMaps.startAnimation(animation4);
                    cardDesc.startAnimation(animation5);
                    btnEnquiry.startAnimation(animation6);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HostelDetails.this,"Error"+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}
