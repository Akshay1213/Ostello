package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;


public class Loadhostels extends AppCompatActivity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 25000;
    SearchView searchView = null;
    String city;
    private RecyclerView mRVhostelList;
    private SimpleCursorAdapter myAdapter;
    private Adapterhostel mAdapter;
    private String[] strArrData = {"No Suggestions"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) Loadhostels.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Loadhostels.this.getComponentName()));
            searchView.setIconified(false);
            searchView.setSuggestionsAdapter(myAdapter);
            // Getting selected (clicked) item suggestion
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {

                    // Add clicked text to search box

                    CursorAdapter ca = searchView.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
                    searchView.setQuery(cursor.getString(cursor.getColumnIndex("city")),false);
                    String city=cursor.getString(cursor.getColumnIndex("city")).trim();
                    Intent i = new Intent(Loadhostels.this, Loadhostels.class);

//Create the bundle
                    Bundle bundle = new Bundle();

//Add your data to bundle
                    bundle.putString("city", city);

//Add the bundle to the intent
                    i.putExtras(bundle);

//Fire that second activity
                    startActivity(i);
                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    // Filter data
                    final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, "city" });
                    for (int i=0; i<strArrData.length; i++) {
                        if (strArrData[i].toLowerCase().startsWith(s.toLowerCase()))
                            mc.addRow(new Object[] {i, strArrData[i]});
                    }
                    myAdapter.changeCursor(mc);
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(Loadhostels.this,item.getTitle(),Toast.LENGTH_SHORT);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

            // User entered text and pressed search button. Perform task ex: fetching data from database and display

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadhostels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String[] from = new String[] {"city"};
        final int[] to = new int[] {android.R.id.text1};
        myAdapter = new SimpleCursorAdapter(Loadhostels.this, R.layout.simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //getting id
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        city = bundle.getString("city");

        List<Datahostel> data=new ArrayList<>();

            // Extract data from json and store into ArrayList as class objects
            for(int i=0;i<10;i++){
                Datahostel hostelData = new Datahostel();
                hostelData.HostelImage= "http://hektorhostels.com/wp-content/uploads/2016/07/Hektor_Design_Hostels_tuba_2-le_dbl2.jpg";
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
        LinearLayoutManager llm=new LinearLayoutManager(Loadhostels.this);
            mRVhostelList.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRVhostelList.getContext(),
               llm.getOrientation());
        mRVhostelList.addItemDecoration(dividerItemDecoration);
            mAdapter.notifyDataSetChanged();



        mRVhostelList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view.findViewById(R.id.hiddenid);

//                Toast.makeText(Loadhostels.this, "Card at " + position + " is clicked"+tv.getText(), Toast.LENGTH_SHORT).show();
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
        new AsyncFetchForMENU().execute();
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
                    hostelData.HostelImage = "http://ostallo.com/ostello/images/" + json_data.getString("hostel_id") + "/home.jpg";
                    Log.d("******->", "http://ostallo.com/ostello/images/" + json_data.getString("hostel_id") + "/home.jpg");
                    hostelData.HostelName= json_data.getString("hostelname");
                    hostelData.catName= json_data.getString("category");
                    hostelData.type= json_data.getString("type");
                    hostelData.price= json_data.getInt("rate");
                    hostelData.id=json_data.getString("hostel_id");
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
                Toast.makeText(Loadhostels.this,"Error "+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
    public class AsyncFetchForMENU extends AsyncTask<String, String, String> {

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

                // Enter URL address where your php file resides or your JSON file address
                url = new URL(Config.AutoComplete_URL);

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

            //this method will be running on UI thread
            ArrayList<String> dataList = new ArrayList<String>();
            pdLoading.dismiss();


            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        dataList.add(json_data.getString("city"));
                    }

                    strArrData = dataList.toArray(new String[dataList.size()]);

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(Loadhostels.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(Loadhostels.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
}
