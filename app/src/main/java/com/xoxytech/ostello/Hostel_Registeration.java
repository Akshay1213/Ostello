
package com.xoxytech.ostello;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Hostel_Registeration extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    EditText editText_ownername;
    Button submit;
    TextView name, mobilenumber1, mobilenumber2, typetext, hostelname, hosteladdr, price, vacancy, city;
    EditText textname, txtmobilenumber1, txtmobilenumber2, texthostelname, texthosteladdr, textprice, textvacancy, textCity;
    String sname, smobilenumber1, smobilenumber2, stype, sgender = "", shostelname, saddress, sprice, svacancy, togglestatus = "", scity;
    ListView list;
    Spinner type;
    RadioGroup radioGroup;
    ToggleButton toggleelevator, toggledrinkingwater, togglecot, togglecctv, toggleac, toggleelectricity, togglegym, togglehotwater, toggletv, togglecleaning, toggleparking, togglewashingmachine, togglemess, togglestudytable, togglewifi;
    int flag = 0, f, fs = 0, name_length, i = 0;
    RequestQueue queue;
    String url;
    TextView location1;
    String s, name1;
    int serverResponseCode = 0;
    String provider;
    double lat, lag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hostel__registeration);

        //SharedPreferences sp = getSharedPreferences("YourSharedPreference",Activity.MODE_PRIVATE);
        // final String username= sp.getString("USER_NAME",null);
        editText_ownername = (EditText) findViewById(R.id.editTextusername);

        queue = Volley.newRequestQueue(Hostel_Registeration.this);
        url = "http://ostallo.com/ostello/addhostel.php";
        submit = (Button) findViewById(R.id.button1);
        name = (TextView) findViewById(R.id.txtusername);
        typetext = (TextView) findViewById(R.id.txttype);
        mobilenumber1 = (TextView) findViewById(R.id.txtmobilenumber1);
        mobilenumber2 = (TextView) findViewById(R.id.txtmobilenumber2);
        textname = (EditText) findViewById(R.id.editTextusername);
        txtmobilenumber1 = (EditText) findViewById(R.id.editTextmobile1);
        txtmobilenumber2 = (EditText) findViewById(R.id.editTextmobile2);
        hostelname = (TextView) findViewById(R.id.txthostelname);
        city = (TextView) findViewById(R.id.txtcity);
        textCity = (EditText) findViewById(R.id.editTextcity);
        hosteladdr = (TextView) findViewById(R.id.txtaddress);
        price = (TextView) findViewById(R.id.txtprice);
        vacancy = (TextView) findViewById(R.id.textvacancy);
        texthostelname = (EditText) findViewById(R.id.editTexthostelname);
        texthosteladdr = (EditText) findViewById(R.id.editTextaddress);
        textprice = (EditText) findViewById(R.id.editTextprice);
        textvacancy = (EditText) findViewById(R.id.editTextvacancy);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        toggleelevator = (ToggleButton) findViewById(R.id.toggleElevator);
        toggledrinkingwater = (ToggleButton) findViewById(R.id.toggleDrinkingwater);
        togglecot = (ToggleButton) findViewById(R.id.toggleCot);
        togglecctv = (ToggleButton) findViewById(R.id.togglecctv);
        toggleac = (ToggleButton) findViewById(R.id.toggleAc);
        toggleelectricity = (ToggleButton) findViewById(R.id.toggleElectricity);
        togglegym = (ToggleButton) findViewById(R.id.toggleGym);
        togglehotwater = (ToggleButton) findViewById(R.id.toggleHotwater);
        toggletv = (ToggleButton) findViewById(R.id.toggleTV);
        togglecleaning = (ToggleButton) findViewById(R.id.toggleCleaning);
        toggleparking = (ToggleButton) findViewById(R.id.toggleParking);
        togglewashingmachine = (ToggleButton) findViewById(R.id.toggleWashingmachine);
        togglestudytable = (ToggleButton) findViewById(R.id.toggleStudytable);
        togglewifi = (ToggleButton) findViewById(R.id.toggleWifi);
        togglemess = (ToggleButton) findViewById(R.id.toggleMess);
        mobilenumber1 = (TextView) findViewById(R.id.txtmobilenumber1);
        location1 = (TextView) findViewById(R.id.txtlocation);
        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        final String username = sp.getString("USER_NAME", null);
        Log.d("Name", username);
        String number = sp.getString("USER_PHONE", null);
        editText_ownername.setText(username);
        txtmobilenumber1.setText(number);

        type = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setFocusable(true);
        type.setFocusableInTouchMode(true);
        type.requestFocus();
        type.clearFocus();
        radioGroup.setFocusable(true);
        radioGroup.setFocusableInTouchMode(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText_ownername.setText("John");

                sname = textname.getText().toString() + " ";
                smobilenumber1 = txtmobilenumber1.getText().toString() + " ";
                smobilenumber2 = txtmobilenumber2.getText().toString() + " ";
                scity = textCity.getText().toString().replaceAll(" ", "_") + " ";
                stype = type.getSelectedItem().toString() + " ";
                sgender = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString() + " ";
                shostelname = texthostelname.getText().toString() + " ";
                shostelname = shostelname.replaceAll(" ", "_");
                saddress = texthosteladdr.getText().toString() + " ";
                saddress = saddress.replaceAll(" ", "_");
                sprice = textprice.getText().toString() + " ";
                svacancy = textvacancy.getText().toString() + " ";
                i = 0;
                while (i < shostelname.length()) {
                    if (shostelname.charAt(i) == '$') {
                        texthostelname.setError("use of $ is not allowed!");
                        f = 9;
                        flag = 1;
                    }
                    i++;
                }


                i = 0;
                while (i < saddress.length()) {
                    if (saddress.charAt(i) == '$' || saddress.charAt(i) == '&') {
                        texthosteladdr.setError("use of $,& is not allowed!");
                        f = 10;
                        flag = 1;

                    }
                    i++;
                }

                i = 0;
                while (i < scity.length()) {
                    if (scity.charAt(i) == '$' || scity.charAt(i) == '&') {
                        textCity.setError("use of $,& is not allowed!");
                        f = 11;
                        flag = 1;

                    }
                    i++;
                }



                if (textname.getText().toString().length() == 0) {
                    textname.setError("First name is required!");
                    flag = 1;
                    f = 1;
                }


                if (txtmobilenumber1.getText().toString().length() == 0) {
                    txtmobilenumber1.setError("Mobile number is required!");
                    flag = 1;
                    f = 2;
                }

               /* if (txtmobilenumber2.getText().toString().length() == 0) {
                    txtmobilenumber2.setError("Mobile number is required!");
                    flag = 1;
                    f = 3;
                }

                if (txtmobilenumber2.getText().toString().length() < 10) {
                    txtmobilenumber2.setError("Mobile number should be 10 digit!");
                    txtmobilenumber2.requestFocus();
                    flag = 1;
                }*/
                if (txtmobilenumber2.getText().toString().length() != 0 && txtmobilenumber2.getText().toString().length() != 10) {
                    txtmobilenumber2.setError("Mobile number should be 10 digit!");
                    txtmobilenumber2.requestFocus();
                    flag = 1;
                    fs = 1;
                } else
                    smobilenumber2 = "null";


                if (textCity.getText().toString().length() == 0) {
                    textCity.setError("City is required!");
                    flag = 1;
                    f = 4;
                }


                if (texthostelname.getText().toString().length() == 0) {
                    texthostelname.setError("Hostel name is required!");
                    flag = 1;
                    f = 5;
                }

                if (texthosteladdr.getText().toString().length() == 0) {
                    texthosteladdr.setError("Hostel address is required!");
                    flag = 1;
                    f = 6;
                }

                if (textprice.getText().toString().length() == 0) {
                    textprice.setError("Price is required!");
                    flag = 1;
                    f = 7;
                }

                if (textvacancy.getText().toString().length() == 0) {
                    textvacancy.setError("vacancy is required!");
                    flag = 1;
                    f = 8;
                }

                togglestatus = " ";
                if (toggleelevator.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggledrinkingwater.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecot.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecctv.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleac.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleelectricity.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglegym.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglehotwater.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggletv.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecleaning.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleparking.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";
                if (togglewashingmachine.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglemess.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglestudytable.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglewifi.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";


                if (flag == 0) {

                    StringRequest postrequest = new StringRequest(Request.Method.POST, url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + stype.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + scity.trim() + "&type=" + sgender.trim() + "&facilities=" + togglestatus.trim() + "&location=" + lat + "," + lag, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("*****", url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + sgender.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + scity.trim() + "&type=" + stype.trim() + "&facilities=" + togglestatus.trim() + "&location=" + lat + "," + lag);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("***", error.toString());

                        }
                    });
                    queue.add(postrequest);

                   /*AlertDialog.Builder builder1 = new AlertDialog.Builder(Hostel_Registeration.this);
                    builder1.setMessage("hostel registered successfully! To upload images Pls contact ostallohostels@gmail.com");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert11 = builder1.create();
                    alert11.show();
                    alert11.getWindow().setLayout(900,400);*/
                    /*AlertDialog.Builder builder1 = new AlertDialog.Builder(Hostel_Registeration.this);
                       final Dialog dialog=new Dialog(Hostel_Registeration.this);
                    builder1.setContentView(R.layout.dialog);
                    dialog.setCancelMessage((Message) R.string.dialog_text);
*/
                    // txtmobilenumber1.getText().clear();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Hostel_Registeration.this);
                    LayoutInflater inflater = Hostel_Registeration.this.getLayoutInflater();
                    alertDialog.setView(inflater.inflate(R.layout.dialog, null));
                    alertDialog.setMessage("Hostel Register Successfully. To upload hostel images contact ostallohostels@gmail.com ");
                    alertDialog.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    toggleelevator.setChecked(false);
                                    toggledrinkingwater.setChecked(false);
                                    togglecot.setChecked(false);
                                    togglecctv.setChecked(false);
                                    toggleac.setChecked(false);
                                    toggleelectricity.setChecked(false);
                                    togglegym.setChecked(false);
                                    togglehotwater.setChecked(false);
                                    toggletv.setChecked(false);
                                    togglecleaning.setChecked(false);
                                    toggleparking.setChecked(false);
                                    togglewashingmachine.setChecked(false);
                                    togglemess.setChecked(false);
                                    togglestudytable.setChecked(false);
                                    togglewifi.setChecked(false);
                                    type.setSelection(0);
                                    txtmobilenumber2.getText().clear();
                                    texthostelname.getText().clear();
                                    texthosteladdr.getText().clear();
                                    textCity.getText().clear();
                                    textprice.getText().clear();
                                    textvacancy.getText().clear();
                                    togglestatus = "";
                                }
                            });
                    alertDialog.show();


                } else {
                    /*if (f == 3)
                        txtmobilenumber2.requestFocus();*/

                    if (f == 4)
                        textCity.requestFocus();

                    if (f == 5)
                        texthostelname.requestFocus();

                    if (f == 6)
                        texthosteladdr.requestFocus();

                    if (f == 7)
                        textprice.requestFocus();

                    if (f == 8)
                        textvacancy.requestFocus();
                    if (f == 9)
                        texthostelname.requestFocus();
                    if (f == 10)
                        texthosteladdr.requestFocus();

                    if (f == 11)
                        textCity.requestFocus();

                   /* if (txtmobilenumber2.getText().toString().length() < 10) {
                        txtmobilenumber2.setError("Mobile number should be 10 digit!");
                        txtmobilenumber2.requestFocus();
                    }*/
                    flag = 0;
                }

            }

        });


    }

    @Override
    public void onLocationChanged(Location location) {


        location1.setText("" + location.getLatitude() + "," + location.getLongitude());

        lat = location.getLatitude();
        lag = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}