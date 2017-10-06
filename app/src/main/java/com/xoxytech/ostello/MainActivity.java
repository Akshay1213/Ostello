package com.xoxytech.ostello;

// TODO: 22/8/17

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private static final int SELECT_PICTURE = 1;
    private static final long ANIM_DURATION = 350;
    public static List<CitySuggetions> cities;
    private static long back_pressed;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    FloatingActionButton fab;
    AlertDialog alertDialog;
    Button btnRetry;
    DrawerLayout drawer;
    ImageView image;
    private RequestQueue queue;
    private FloatingSearchView mSearchView;
    private ColorDrawable mDimDrawable;
    private String mLastQuery="Search...",TAG;
    private View mDimSearchViewBackground;
    private ImageView profile;
    private Toast toast;
    private RelativeLayout relativeLayoutNoInternetCon;
    private ArrayList<Imagemodel> imageModelArrayList;

    private String[] myImageList = new String[]{"http://ostallo.com/ostello/images/1/1.jpg", "http://ostallo.com/ostello/images/1/2.jpg", "http://ostallo.com/ostello/images/1/3.jpg", "http://ostallo.com/ostello/images/1/4.jpg", "http://ostallo.com/ostello/images/1/5.jpg"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageview);
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        queue = Volley.newRequestQueue(getApplicationContext());
        Toast.makeText(MainActivity.this," Welcome to ostallo",Toast.LENGTH_SHORT);
        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        String username = sp.getString("USER_PHONE", null);
        Log.d("*********",username);
        getUserName();

        relativeLayoutNoInternetCon = (RelativeLayout) findViewById(R.id.layouterror);

        relativeLayoutNoInternetCon = (RelativeLayout) findViewById(R.id.layouterror);
        btnRetry = (Button) findViewById(R.id.btnRetry);
        if (CheckInternet.checkinternet(getApplicationContext())) {
            fab.setVisibility(View.VISIBLE);
            relativeLayoutNoInternetCon.setVisibility(View.INVISIBLE);
            findViewById(R.id.mainlayout).setVisibility(View.VISIBLE);

        } else {
            relativeLayoutNoInternetCon.setVisibility(View.VISIBLE);
            findViewById(R.id.mainlayout).setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("wth", "I got executed");
                    if (CheckInternet.checkinternet(getApplicationContext())) {
                        getUserName();
                        relativeLayoutNoInternetCon.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        findViewById(R.id.mainlayout).setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        mSearchView=(FloatingSearchView)findViewById(R.id.floating_search_view);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options=ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.filteropenanim,R.anim.filteropenanim);
                startActivity(new Intent(MainActivity.this,Contactus.class),options.toBundle());

//                Snackbar.make(view, "Contact Us", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mSearchView.attachNavigationDrawerToMenuButton(drawer);
        mSearchView.setDismissOnOutsideClick(true);
        mSearchView.setDismissOnOutsideClick(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView userview=( TextView)hView.findViewById(R.id.Usernamenav);
        userview.setText(username);
        LoadprofileImage(hView);
        navigationView.setNavigationItemSelectedListener(this);

        cities=new ArrayList<>();

        new AsyncFetch().execute();



        mDimSearchViewBackground = findViewById(R.id.dim_background);
        mDimDrawable = new ColorDrawable(Color.BLACK);
        mDimDrawable.setAlpha(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDimSearchViewBackground.setBackground(mDimDrawable);
        } else {
            mDimSearchViewBackground.setBackgroundDrawable(mDimDrawable);
        }
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

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                CitySuggetions citySuggetions = (CitySuggetions) item;

                if (citySuggetions.getBody().equalsIgnoreCase(mLastQuery))
                    citySuggetions.setHistory(true);
                Log.d("zandan", "am called" + itemPosition + "  " + item.getBody());
                if (citySuggetions.isHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.GRAY);
                    leftIcon.setAlpha(1f);
                } else {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_call_received_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.GRAY);
                    leftIcon.setAlpha(.30f);
                }
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                CitySuggetions citySuggetions1 = (CitySuggetions) searchSuggestion;


                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
                /////////////////Broooooooooooo here we can launch new activity///////////////////////////////

                Intent i = new Intent(MainActivity.this, NewMenu.class);


//Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("city", mLastQuery.trim());

//Add the bundle to the intent
                i.putExtras(bundle);

                ActivityOptions options=ActivityOptions.makeScaleUpAnimation(mSearchView.getRootView(),0,0,280,280);
//Fire that second activity
                if(!mLastQuery.contains("No result found")) {
                    startActivity(i, options.toBundle());
                    mSearchView.setSearchBarTitle(mLastQuery);
                    mSearchView.clearSearchFocus();
                }

            }

            @Override
            public void onSearchAction(String currentQuery) {
                List<CitySuggetions> filteredcities=new ArrayList<>();
                for (CitySuggetions i:cities) {
                    if(i.getBody().contains(currentQuery)){
                        filteredcities.add(i);

                    }

                }
                if(filteredcities.size()<1)
                    filteredcities.add(new CitySuggetions("No result found"));
                mSearchView.swapSuggestions(filteredcities);
            }
        });
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);
                headerHeight = findViewById(R.id.header_view).getHeight();
                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        headerHeight, 0);
                anim.setDuration(350);
                fadeDimBackground(0, 150, null);
                anim.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //show suggestions when search bar gains focus (typically history suggestions)
                        if (CheckInternet.checkinternet(getApplicationContext()))
                        new AsyncFetch().execute();
                        else
                            Toast.makeText(MainActivity.this, "Make sure you have Active Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
                anim.start();


            }

            @Override
            public void onFocusCleared() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);
                headerHeight = findViewById(R.id.header_view).getHeight();
                Log.d("****size", "" + headerHeight);

                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        0, headerHeight);
                anim.setDuration(350);
                anim.start();
                fadeDimBackground(150, 0, null);

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);
            }
        });
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                Log.d("************",item.getTitle().toString());
                //// TODO: 21/8/17  can implement location menu item

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




    void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(drawer);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (!mSearchView.setSearchFocused(false)) {

        }
        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            // need to cancel the toast here
            toast.cancel();
            // code for exit
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else
        {
            // ask user to press back button one more time to close app
            toast=  Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();


    }

    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mSearchView.setSearchBarTitle(matches.get(0).toString());

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            //Creating a view to get the dialog box
            View logoutConfirmDialog = li.inflate(R.layout.dialogue_confirm_logout, null);
            TextView txtYes = (TextView) logoutConfirmDialog.findViewById(R.id.txtYes);
            TextView txtNo = (TextView) logoutConfirmDialog.findViewById(R.id.txtNo);
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(logoutConfirmDialog);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            txtYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", null); //username the user has entered
                    editor.putString("USER_PHONE", null); //username the user has entered
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, Login.class));

                }
            });
            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } else if (id == R.id.nav_addhostel) {
            startActivity(new Intent(MainActivity.this, Hostel_Registeration.class));

        } else if (id == R.id.nav_deleteUserAccount) {

            deleteUserAccount();

        } else if (id == R.id.nav_manage) {
            LaunchActivity(new Intent(MainActivity.this, ManageHostels.class));
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.xoxytech.ostello");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_aboutus) {

//            ActivityOptions options=ActivityOptions.makeScaleUpAnimation(getWindow().getDecorView().getRootView(),0,0,100,100);
            startActivity(new Intent(MainActivity.this, Aboutus.class));


        } else if (id == R.id.nav_favourite) {
//            LaunchActivity(new Intent(MainActivity.this, Favourite.class));
            startActivity(new Intent(MainActivity.this, Favourite.class));


        } else if (id == R.id.nav_history) {
            LaunchActivity(new Intent(MainActivity.this, History.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteUserAccount() {
        TextView txtYes, txtNo;
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        //Creating a view to get the dialog box
        View deleteConfirmDialog = li.inflate(R.layout.dialogue_confirm_delete, null);
        txtYes = (TextView) deleteConfirmDialog.findViewById(R.id.txtYes);
        txtNo = (TextView) deleteConfirmDialog.findViewById(R.id.txtNo);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        //Adding our dialog box to the view of alert dialog
        alert.setView(deleteConfirmDialog);
        //Creating an alert dialog
        alertDialog = alert.create();
        alertDialog.show();

        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternet.checkinternet(getApplicationContext())) {
                    deleteAccount();
                    LayoutInflater li = LayoutInflater.from(MainActivity.this);
                    //Creating a view to get the dialog box
                    View deleteConfirmDialog1 = li.inflate(R.layout.dialog_delete_ok, null);
                    Button btnOk = (Button) deleteConfirmDialog1.findViewById(R.id.buttonOk);
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    //Adding our dialog box to the view of alert dialog
                    alert.setView(deleteConfirmDialog1);
                    //Creating an alert dialog
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                        }
                    });

                } else
                    Toast.makeText(MainActivity.this, "Make sure you have Active Internet Connection", Toast.LENGTH_LONG).show();
            }
        });


        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

    }
    private void fadeDimBackground(int from, int to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                mDimDrawable.setAlpha(value);
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    protected void LoadprofileImage(View View) {

    }

    public void getUserName() {
        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        String phone1 = sp.getString("USER_PHONE", null);
        Log.d("response", Config.USERNAME_URL + "?phone=" + phone1);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USERNAME_URL + "?phone=" + phone1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        String[] name = response.split("\n");
                        Log.d("Username", name[1] + "");

                        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", name[1]);
                        // Log.d("@@@",name[1]);//username the user has entered
                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null)
                            return;
//                            Toast.makeText(Registeration.this, "ohh god error   ", Toast.LENGTH_LONG).show();
//                        Toast.makeText(Registeration.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        String body;
                        Log.d("response", error.getMessage());
                        //get status code here

                        try {

                            body = new String(error.networkResponse.data, "UTF-8");
//                            Toast.makeText(Registeration.this, body, Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    void LaunchActivity(final Intent I) {
        if (CheckInternet.checkinternet(getApplicationContext())) {

            relativeLayoutNoInternetCon.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);
            findViewById(R.id.mainlayout).setVisibility(View.VISIBLE);
            startActivity(I);
        } else {
            relativeLayoutNoInternetCon.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
            findViewById(R.id.mainlayout).setVisibility(View.INVISIBLE);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       /* Log.d("wth","nope I got executed");
                        Boolean res=checkInternet();*/
                    if (CheckInternet.checkinternet(getApplicationContext())) {
                        relativeLayoutNoInternetCon.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        findViewById(R.id.mainlayout).setVisibility(View.VISIBLE);
                        startActivity(I);
                    }
                }
            });
        }

    }

    private ArrayList<Imagemodel> populateList() {

        ArrayList<Imagemodel> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Imagemodel imageModel = new Imagemodel();
            //imageModel.setImage(myImageList[i]);
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }
        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.viewpager);


        mPager.setAdapter(new SlidingimageAdapter(MainActivity.this, imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.circlepage);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);
        indicator.setX(5);
        indicator.setY(450);
        indicator.setVisibility(View.GONE);

        NUM_PAGES = imageModelArrayList.size();

        // Auto start of viewpager
        final android.os.Handler handler = new android.os.Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                // handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//                Toast.makeText(MainActivity.this,"position changed "+(position%(NUM_PAGES)),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void deleteAccount() {
        SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        String phone1 = sp.getString("USER_PHONE", null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DELETEACCOUNT_URL + "?phone=" + phone1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Account deleted", response);
                SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                               /* editor.clear();*/
                editor.putString("USER_NAME", null);
                editor.putString("USER_PHONE", null);
                editor.commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error == null || error.networkResponse == null)
                    return;

                String body;
                Log.d("response", error.getMessage());
                //get status code here

                try {

                    body = new String(error.networkResponse.data, "UTF-8");
//
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return super.getParams();
            }
        };

        queue.add(stringRequest);
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


}
