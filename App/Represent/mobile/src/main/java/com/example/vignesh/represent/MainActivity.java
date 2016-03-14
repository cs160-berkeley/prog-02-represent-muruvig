package com.example.vignesh.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "80IPeudcxg8spFXsM0LeBg6a6";
    private static final String TWITTER_SECRET = "ZNMYSZIfTiLYb62odxhqiGd57trN1zg9976rV4lVz4XmExl7Cw";

    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private ImageButton goButton;
    private ImageButton currentLocation;
    private EditText zipcode;
    public GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Double mLatitude;
    private Double mLongitude;
    private Intent sendIntent;

    private HashMap<Integer, HashMap<String, String>> all_rep_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        goButton = (ImageButton) findViewById(R.id.go_button);
        currentLocation = (ImageButton) findViewById(R.id.current_location);
        zipcode = (EditText) findViewById(R.id.editText);


        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String link = url(null, Double.toString(mLatitude), Double.toString(mLongitude));
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("NAMES", names);
//                sendIntent.putExtra("WEBSITES", websites);
//                sendIntent.putExtra("EMAILS", emails);
//                startService(sendIntent);
                Intent activityIntent = new Intent(MainActivity.this, LocalList.class);
//                activityIntent.putExtra("reps", getInfo(MainActivity.this, link));
                activityIntent.putExtra("zipcode", zipcode.getText().toString());
                startActivity(activityIntent);
            }
        });

        zipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcode.setText("");
            }

        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (zipcode.getText().toString().length() != 5) {
//                    Context context = getApplicationContext();
//                    CharSequence text = "Please enter a 5 digit zipcode";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                    return;
//                }
//                String link = url(zipcode.getText().toString(), null, null);
                Intent activityIntent = new Intent(MainActivity.this, LocalList.class);
                try {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
                if (mLastLocation != null) {
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();
                    activityIntent.putExtra("latitude", mLatitude);
                    activityIntent.putExtra("longitude", mLongitude);
                }
                startActivity(activityIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    public HashMap<Integer, HashMap<String, String>> getInfo(Context context, String url){
//        all_rep_map = new HashMap<Integer, HashMap<String, String>>();
//        Ion.with(context)
//                .load(url)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        Log.d("candidate2", "before calling the method");
//                        HashMap<String, String> single_rep_map;
//                        JsonArray representatives = result.getAsJsonArray("results");
//                        JsonObject rep;
//                        int i = 0;
//                        for (JsonElement json_rep : representatives) {
//                            rep = json_rep.getAsJsonObject();
//                            single_rep_map = new HashMap<String, String>();
//                            single_rep_map.put("name", rep.get("first_name").getAsString() + " " + rep.get("last_name").getAsString());
//                            single_rep_map.put("party", rep.get("party").getAsString());
//                            single_rep_map.put("email", rep.get("oc_email").getAsString());
//                            single_rep_map.put("website", rep.get("website").getAsString());
//                            single_rep_map.put("twitter_id", rep.get("twitter_id").getAsString());
//                            single_rep_map.put("start_term", rep.get("term_start").getAsString());
//                            single_rep_map.put("end_term", rep.get("term_end").getAsString());
//                            single_rep_map.put("title", rep.get("title").getAsString());
//                            all_rep_map.put(i, single_rep_map);
//                        }
//                    }
//                });
//        return all_rep_map;
//    }
//
//    private String url(String zipcode, String longitude, String latitude){
//        if(zipcode != null){
//            return "http://congress.api.sunlightfoundation.com/legislators/locate?zip="+zipcode+"&apikey=41e1deffd8124c65911ed01d6aaadc2f";
//        }
//        return "http://congress.api.sunlightfoundation.com/legislators/locate?latitude="+latitude+"&longitude="+longitude+"&apikey=41e1deffd8124c65911ed01d6aaadc2f";
//    }

}