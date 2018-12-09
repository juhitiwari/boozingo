package com.vpaliy.loginconcept;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.vpaliy.loginconcept.Interfaces.DetailList;
import com.vpaliy.loginconcept.Interfaces.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {
    int id=PlacesAdapter.id;
    public static final String ROOT_URL = "http://34.199.49.88:3000/";
    private FloatingActionMenu menuYellow;

    String lat,longt,phone;
    TextView mname,mrating,mtiming,mtype,mmap,mmenu,mreviews,mphotos,mcost,maddress,mphone,mbooze,msitting,mmusic,mpayment,mmapbtn;
ImageView mphonebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

com.github.clans.fab.FloatingActionButton fab1=(com.github.clans.fab.FloatingActionButton)findViewById(R.id.menu1);
fab1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(DetailActivity.this, "Camera Activity to be opened", Toast.LENGTH_SHORT).show();
    }
});

        mname=(TextView)findViewById(R.id.name);
        mrating=(TextView)findViewById(R.id.rating);
        mtiming=(TextView)findViewById(R.id.timings);
        mtype=(TextView)findViewById(R.id.type);
        mmap=(TextView)findViewById(R.id.directions);
        mmenu=(TextView)findViewById(R.id.menu);
        mreviews=(TextView)findViewById(R.id.reviews);
        mphotos=(TextView)findViewById(R.id.photos);
        maddress=(TextView)findViewById(R.id.address);
        mphone=(TextView)findViewById(R.id.phone);
        mcost=(TextView)findViewById(R.id.cost);
        mbooze=(TextView)findViewById(R.id.booze);
        msitting=(TextView)findViewById(R.id.sitting);
        mmusic=(TextView)findViewById(R.id.music);
        mpayment=(TextView)findViewById(R.id.payment);
        mphonebtn=(ImageView) findViewById(R.id.phone_btn);










        mmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(lat),Float.parseFloat(longt));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getApplicationContext().startActivity(intent);
            }
        });
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        2);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mphonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
showDetails();

    }

    private void showDetails() {
        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        DetailList api=adapter.create(DetailList.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(DetailActivity.this);
        progressDoalog.show();
        api.details(

                id,





                new retrofit.Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try{
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                            if(progressDoalog.isShowing()) {
                                progressDoalog.dismiss();
                            }

                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }

                        try {
                            JSONObject jobj = new JSONObject(output);

                            JSONArray obj=jobj.getJSONArray("result");
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject current = obj.getJSONObject(i);
                                lat=current.getString("longitude");
                                longt=current.getString("latitude");
mname.setText(current.getString("name"));
mtiming.setText(current.getString("time"));
mrating.setText(current.getString("avg_rating"));
mtype.setText(current.getString("details"));
mcost.setText(current.getString("cost"));
maddress.setText(current.getString("address"));
phone=current.getString("contact");
mphone.setText(current.getString("contact"));
mbooze.setText(current.getString("booze_served"));
msitting.append(current.getString("sitting facility"));
mmusic.append(current.getString("music"));
mpayment.append(current.getString("payment"));
                            }
                            // Now store the user in SQLite



                            // Inserting row in users table

                            // Launch main activity


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();                        if(progressDoalog.isShowing()) {
                            progressDoalog.dismiss();
                        }

                    }
                }
        );

    }

}
