package com.xoxytech.ostello;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

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

public class NewMenu extends Activity {
    public static final int CONNECTION_TIMEOUT = 50000;
    public static final int READ_TIMEOUT = 25000;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private static final int FILTER_REQUEST_CODE = 1;
    public static List<CitySuggetions> cities;
    private static long back_pressed;
    SearchView searchView = null;
    String city;
    private FloatingSearchView mSearchView;
    private ColorDrawable mDimDrawable;
    private String mLastQuery="Search...",TAG;
    private RecyclerView mRVhostelList;
    private SimpleCursorAdapter myAdapter;
    private AppBarLayout mAppBar;
    private Adapterhostel mAdapter;
    private String[] strArrData = {"No Suggestions"};
    private Toast toast;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);
        mSearchView=(FloatingSearchView)findViewById(R.id.floating_search_view);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mSearchView.setTranslationY(verticalOffset);
            }
        });

        mSearchView.setDismissOnOutsideClick(true);
        mSearchView.setDismissOnOutsideClick(true);
        cities=new ArrayList<>();
        TAG="**********";
        new AsyncFetch().execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options=ActivityOptions.makeCustomAnimation(NewMenu.this,R.anim.filteropenanim,R.anim.filteropenanim);
                Intent i = new Intent(NewMenu.this, FilterNew.class);

                startActivityForResult(i, FILTER_REQUEST_CODE, options.toBundle());


                Snackbar.make(view, "Filter Applied successfully ", Snackbar.LENGTH_LONG).setAction("Action", null).show();


//                Snackbar.make(view, "Filter", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        city = bundle.getString("city");
        List<Datahostel> data=new ArrayList<>();


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

//                LinearLayoutManager llm = new LinearLayoutManager(NewMenu.this);
//                llm.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter = new Adapterhostel(NewMenu.this, data);
        mRVhostelList.setAdapter(mAdapter);
        LinearLayoutManager llm=new LinearLayoutManager(NewMenu.this);
        mRVhostelList.setLayoutManager(llm);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRVhostelList.getContext(),
//                llm.getOrientation());
//        mRVhostelList.addItemDecoration(dividerItemDecoration);
        mAdapter.notifyDataSetChanged();


        mRVhostelList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view.findViewById(R.id.hiddenid);

//                Toast.makeText(NewMenu.this, "Card at " + position + " is clicked"+tv.getText(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NewMenu.this, HostelDetails.class);

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
        new NewMenu.AsyncFetch().execute();
        new NewMenu.AsyncFetchLoadHostels().execute();


        mSearchView.swapSuggestions(cities);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                mSearchView.showProgress();
                List<CitySuggetions> filteredcities=new ArrayList<>();

                for (CitySuggetions i:cities) {
                    Log.d("**********",String.valueOf(i.getBody().contains(newQuery))+"******"+newQuery);
                    if(i.getBody().toLowerCase().contains(newQuery.toLowerCase() )){
                        filteredcities.add(i);
                        Log.d("**********",i.getBody());
                    }

                }
                mSearchView.swapSuggestions(filteredcities);

                mSearchView.hideProgress();
            }



        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                CitySuggetions citySuggetions = (CitySuggetions) searchSuggestion;

                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
                /////////////////Broooooooooooo here we can launch new activity///////////////////////////////

                Intent i = new Intent(NewMenu.this, NewMenu.class);


//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("city", mLastQuery.trim());

//Add the bundle to the intent
                i.putExtras(bundle);

                ActivityOptions options=ActivityOptions.makeScaleUpAnimation(mSearchView.getRootView(),0,0,280,280);
//Fire that second activity
                startActivity(i,options.toBundle());
                mSearchView.setSearchBarTitle(mLastQuery);
                mSearchView.clearSearchFocus();
            }

            @Override
            public void onSearchAction(String currentQuery) {
                List<CitySuggetions> filteredcities=new ArrayList<>();
                for (CitySuggetions i:cities) {
                    if(i.getBody().contains(currentQuery)){
                        filteredcities.add(i);
                    }

                }
                mSearchView.swapSuggestions(filteredcities);
            }
        });
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {


            }

            @Override
            public void onFocusCleared() {
                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);
            }
        });
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                Log.d("************",item.getTitle().toString());
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Search for place or area");
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

            }
        });
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                //to sync recycler
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        if (!mSearchView.setSearchFocused(false)) {
//
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.setSearchBarTitle(matches.get(0).toString());
        }

        if (requestCode == FILTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("data");
                //Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                // TODO: 12/9/17  str contains filter data filter funn to be appended here to refresh RV
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        mSearchView.setSearchBarTitle(cities.get(id).getBody());
        mSearchView.clearSearchFocus();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides or your JSON file address
                url = new URL("http://ostallo.com/ostello/fetchcities.php");

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

                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

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
            cities=new ArrayList<>();
            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        cities.add(new CitySuggetions(json_data.getString("city")));
                    }

                    mSearchView.swapSuggestions(cities);

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    e.printStackTrace();
                }

            }

        }

    }
    private class AsyncFetchLoadHostels extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(NewMenu.this);
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
            Log.d("*******************",result);
            pdLoading.dismiss();
            List<Datahostel> data=new ArrayList<>();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Datahostel hostelData = new Datahostel();
                    hostelData.HostelImage= "http://janaipackaging.com/ostello/images/"+json_data.getString("hostel_id")+"/home.jpg";
                    Log.d("******->","http://janaipackaging.com/ostello/images/"+json_data.getString("hostel_id")+"/home.jpg");
                    hostelData.HostelName= json_data.getString("hostelname");
                    hostelData.catName= json_data.getString("category");
                    hostelData.type= json_data.getString("type");
                    hostelData.price= json_data.getInt("rate");
                    hostelData.id=json_data.getString("hostel_id");
                    data.add(hostelData);
                }

                // Setup and Handover data to recyclerview
                mRVhostelList = (RecyclerView)findViewById(R.id.hostelList);
//                LinearLayoutManager llm = new LinearLayoutManager(NewMenu.this);
//                llm.setOrientation(LinearLayoutManager.VERTICAL);

                mAdapter = new Adapterhostel(NewMenu.this, data);
                mRVhostelList.setAdapter(mAdapter);
                mRVhostelList.setLayoutManager(new LinearLayoutManager(NewMenu.this));
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(NewMenu.this,"Error "+ e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
  
}


