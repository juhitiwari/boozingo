package com.vpaliy.loginconcept;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vpaliy.loginconcept.Interfaces.List;
import com.vpaliy.loginconcept.Interfaces.RegisterUSer;
import com.vpaliy.loginconcept.helper.SQLiteHandler;
import com.vpaliy.loginconcept.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    private SQLiteHandler db;
    private SessionManager session;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ROOT_URL = "http://34.199.49.88:3000/";
    private RecyclerView mRecyclerView;
    private PlacesAdapter mPlacesAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    GPSTracker gps;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();

        String token = user.get("token");
//        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
            else {
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    listPlaces();

                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            // Handle the camera action
            logoutUser();
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void listPlaces() {

        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        List api=adapter.create(List.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.show();
        api.list(
                latitude,
                longitude,
                120,
                "distance",

                new retrofit.Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try{
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                       // Toast.makeText(MainActivity.this, output, Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();

                        java.util.List<Places> places=new ArrayList<>();

                        try{

                            JSONArray Array=new JSONArray(output);
                            // in=Array.length();
                            for (int i = 0; i < Array.length(); i++) {


                                JSONObject current = Array.getJSONObject(i);


                                Integer num = current.getInt("id");
                                Integer type = current.getInt("type");

String name=current.getString("name");
                                Double rating=current.getDouble("avg_rating");



                                Places place = new Places(type,rating,name,num);

                                places.add(place);
                                mRecyclerView=(RecyclerView)findViewById(R.id.places_recycler_view);
                                mRecyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                mRecyclerView.setLayoutManager(layoutManager);
                                mPlacesAdapter=new PlacesAdapter(places,getApplicationContext());
                                mRecyclerView.setAdapter(mPlacesAdapter);
                            }
                        }
                        catch (JSONException e){
                            Log.e("QueryUtils", "Problem parsing the JSON results", e);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
}
