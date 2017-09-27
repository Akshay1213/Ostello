package com.xoxytech.ostello;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ManageHostels2 extends AppCompatActivity {
    Bundle b;
    String sname, smobilenumber1, stype, sgender = "", shostelname, saddress, sprice, svacancy, togglestatus = "", scity, hostel_id, vacancy, data, category, status, res;
    EditText textname, txtmobilenumber1, texthostelname, texthosteladdr, textprice, textvacancy, textCity, editText_ownername;
    ToggleButton toggleelevator, toggledrinkingwater, togglecot, togglecctv, toggleac, toggleelectricity, togglegym, togglehotwater, toggletv, togglecleaning, toggleparking, togglewashingmachine, togglemess, togglestudytable, togglewifi;
    int flag = 1, f, i = 0;
    Spinner type;
    RadioGroup radioGroup;
    Button update, delete;
    RadioButton radiogirls, radioboys, radiocoed;
    RequestQueue queue, queue1;
    String url, urldelete;
    JSONArray jArray;
    JSONObject json_data;
    List<String> hostel_list;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_hostels2);
        queue1 = Volley.newRequestQueue(ManageHostels2.this);
        urldelete = "http://ostallo.com/ostello/deleteSpecificHostel.php";
        url = "http://ostallo.com/ostello/updatehostel.php";
        queue = Volley.newRequestQueue(ManageHostels2.this);

        update = (Button) findViewById(R.id.btnupdate);
        delete = (Button) findViewById(R.id.btndelete);
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
        editText_ownername = (EditText) findViewById(R.id.editTextusername);
        txtmobilenumber1 = (EditText) findViewById(R.id.editTextmobile1);
        textCity = (EditText) findViewById(R.id.editTextcity);
        texthostelname = (EditText) findViewById(R.id.editTexthostelname);
        texthosteladdr = (EditText) findViewById(R.id.editTextaddress);
        textprice = (EditText) findViewById(R.id.editTextprice);
        textvacancy = (EditText) findViewById(R.id.editTextvacancy);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioboys = (RadioButton) findViewById(R.id.male);
        radiogirls = (RadioButton) findViewById(R.id.female);
        radiocoed = (RadioButton) findViewById(R.id.coed);

        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        final String username = sp.getString("USER_NAME", null);
        Log.d("Name", username);
        String number = sp.getString("USER_PHONE", null);
        editText_ownername.setText(username);
        txtmobilenumber1.setText(number);


        type = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManageHostels2.this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setFocusable(true);
        type.setFocusableInTouchMode(true);
        type.requestFocus();
        type.clearFocus();
        radioGroup.setFocusable(true);
        radioGroup.setFocusableInTouchMode(true);


        b = new Bundle();
        b = getIntent().getExtras();
        String name = b.getString("data");
        String arr[] = name.split(" ");
        Log.d("name", name);

        editText_ownername.setText(username);
        txtmobilenumber1.setText(number);
        texthostelname.setText(arr[0].replaceAll("_", " "));
        texthosteladdr.setText(arr[4].replaceAll("_", " "));
        textCity.setText(arr[5]);
        textprice.setText(arr[3]);
        textvacancy.setText(arr[2]);
        hostel_id = arr[8];

        if (arr[1].equalsIgnoreCase("cot-basis")) {
            type.setSelection(0);
        } else if (arr[1].equalsIgnoreCase("1BHK")) {
            type.setSelection(1);
        } else if (arr[1].equalsIgnoreCase("2BHK")) {
            type.setSelection(2);
        } else if (arr[1].equalsIgnoreCase("3BHK")) {
            type.setSelection(3);
        } else if (arr[1].equalsIgnoreCase("PG")) {
            type.setSelection(4);
        }


        if (arr[6].equalsIgnoreCase("Girls")) {
            radiogirls.setChecked(true);
        } else if (arr[6].equalsIgnoreCase("Boys")) {
            radioboys.setChecked(true);
        } else if (arr[6].equalsIgnoreCase("Co-ed")) {
            radiocoed.setChecked(true);
        }


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
            if (arr[7].charAt(i) == '1') {
                tb[i].setChecked(true);
                Log.d("********", "" + i + ") " + arr[7].charAt(i));

            }
            i++;
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();

                togglestatus = "";
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

                    StringRequest postrequest = new StringRequest(Request.Method.POST, url + "?hostel_id=" + hostel_id.trim() + "&hostel_name=" + texthostelname.getText().toString().replaceAll(" ", "_") + "&category=" + type.getSelectedItem().toString().trim() + "&vacancy=" + textvacancy.getText().toString().trim() + "&rate=" + textprice.getText().toString().trim() + "&address=" + texthosteladdr.getText().toString().replaceAll(" ", "_").trim() + "&city=" + textCity.getText().toString().trim() + "&type=" + ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim() + "&facilities=" + togglestatus.trim(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            res = response;

                            Log.d("*****", url + "?hostel_id=" + hostel_id.trim() + "&hostel_name=" + texthostelname.getText().toString().replaceAll(" ", "_").trim() + "&category=" + type.getSelectedItem().toString().trim() + "&vacancy=" + textvacancy.getText().toString().trim() + "&rate=" + textprice.getText().toString().trim() + "&address=" + texthosteladdr.getText().toString().replaceAll(" ", "_").trim() + "&city=" + textCity.getText().toString().trim() + "&type=" + ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim() + "&facilities=" + togglestatus.trim());


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("***error", url + "?hostel_id=" + hostel_id.trim() + "&hostel_name=" + texthostelname.getText().toString().replaceAll(" ", "_").trim() + "&category=" + type.getSelectedItem().toString().trim() + "&vacancy=" + textvacancy.getText().toString().trim() + "&rate=" + textprice.getText().toString().trim() + "&address=" + texthosteladdr.getText().toString().replaceAll(" ", "_").trim() + "&city=" + textCity.getText().toString().trim() + "&type=" + ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().trim() + "&facilities=" + togglestatus.trim());


                        }
                    });
                    queue.add(postrequest);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageHostels2.this);

                    alertDialog.setMessage("Hostel update Successfully. ");

                    alertDialog.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    onBackPressed();

                                }
                            });

                    alertDialog.show();
                } else {
                    if (f == 1)
                        editText_ownername.requestFocus();
                    if (f == 2)
                        txtmobilenumber1.requestFocus();

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

                    if (txtmobilenumber1.getText().toString().length() < 10) {
                        txtmobilenumber1.setError("Mobile number should be 10 digit!");
                        txtmobilenumber1.requestFocus();
                    }
                    flag = 0;
                }


            }


        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageHostels2.this);

                alertDialog.setMessage("Are you want to delete hostel?");

                alertDialog.setPositiveButton(
                        "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                StringRequest postrequest = new StringRequest(Request.Method.POST, urldelete + "?hostel_id=" + hostel_id.trim(), new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("*****", urldelete + "?hostel_id=" + hostel_id.trim());


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("***error", urldelete + "?hostel_id=" + hostel_id.trim());


                                    }
                                });
                                queue1.add(postrequest);
                                dialog.cancel();
                                finish();
                                onBackPressed();

                            }
                        });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


                alertDialog.show();

            }
        });
    }

    public void validate() {
        if (editText_ownername.getText().toString().length() == 0) {
            editText_ownername.setError("First name is required!");
            flag = 1;
            f = 1;
        }


        if (txtmobilenumber1.getText().toString().length() == 0) {
            txtmobilenumber1.setError("Mobile number is required!");
            flag = 1;
            f = 2;
        }


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
    }
}