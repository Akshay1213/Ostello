package com.xoxytech.ostello;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ManageHostels extends AppCompatActivity {


    final String url = "http://ostallo.com/ostello/fetchownerhostels.php";
    String category, vacancy, rate, address, city, type, facilities, data = "";
    String name, name1;
    List<String> hostel_list;
    ArrayAdapter adapter;
    ListView listView;
    ArrayList<String> list;
    TextView txtview;
    JSONArray jArray;
    JSONObject json_data;
    long hostel_id;
    int i;
    private String number = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_hostels);
        listView = (ListView) findViewById(R.id.listview);

        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        number = sp.getString("USER_PHONE", null);
        Log.d("phone", number);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                RequestQueue queue = Volley.newRequestQueue(ManageHostels.this);

                StringRequest getrequest = new StringRequest(Request.Method.GET, url + "?phone=" + number, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            jArray = new JSONArray(response);

                            json_data = jArray.getJSONObject(position);

                            data = json_data.getString("hostelname").replaceAll(" ", "_") + " ";
                            data += json_data.getString("category") + " ";
                            data += json_data.getString("vacancy") + " ";
                            data += json_data.getString("rate") + " ";
                            data += json_data.getString("address").replaceAll(" ", "_") + " ";
                            data += json_data.getString("city") + " ";
                            data += json_data.getString("type") + " ";
                            data += json_data.getString("facilities") + " ";
                            data += json_data.getString("hostel_id") + " ";

                            Intent intent = new Intent(ManageHostels.this, ManageHostels2.class);
                            intent.putExtra("data", data);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());

                    }
                });
                queue.add(getrequest);


            }
        });

        load_data();

    }

    @Override
    protected void onResume() {
        super.onResume();

        load_data();
    }

    public void load_data() {
        RequestQueue queue = Volley.newRequestQueue(ManageHostels.this);

        StringRequest getrequest = new StringRequest(Request.Method.GET, url + "?phone=" + number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {

                    jArray = new JSONArray(response);

                    hostel_list = new ArrayList<String>(jArray.length());

                    for (i = 0; i < jArray.length(); i++) {
                        json_data = jArray.getJSONObject(i);

                        hostel_list.add(json_data.getString("hostelname").replaceAll("_", " "));
                        category = json_data.getString("category");
                        vacancy = json_data.getString("vacancy");
                        rate = json_data.getString("rate");
                        address = json_data.getString("address");
                        city = json_data.getString("city");
                        type = json_data.getString("type");
                        facilities = json_data.getString("facilities");
                        hostel_id = json_data.getLong("hostel_id");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (hostel_list != null) {
                    ArrayAdapter adapter = new ArrayAdapter(ManageHostels.this, android.R.layout.simple_list_item_1, hostel_list);
                    listView.setAdapter(adapter);
                } else {
                    findViewById(R.id.textViewError).setVisibility(View.VISIBLE);
                    findViewById(R.id.listview).setVisibility(View.INVISIBLE);
                    Snackbar.make(findViewById(R.id.manage_Hostel_layout), " ", Snackbar.LENGTH_LONG).setAction("Action", null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());

            }
        });
        queue.add(getrequest);


    }

}

