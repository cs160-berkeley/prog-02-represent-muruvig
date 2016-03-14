package com.example.vignesh.represent;

/**
 * Created by vignesh on 3/2/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class LocalList extends Activity implements ThreadCompleteListener {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private ImageButton barbaraBoxer;
    private String name;
    private Drawable image;
    private String party;
    private Drawable box;
    private String email;
    private String website;
    private String tweet;
    private String twitterImageURL;

    private ScrollView scrollView;

    private ArrayList<RelativeLayout> relativeLayouts;
    private RelativeLayout r;
    private RelativeLayout.LayoutParams rlp;
    private LinearLayout ll;
    private HashMap<Integer, HashMap<String, String>> all_rep_map;

    private Double latitude;
    private Double longitude;
    private String zipcode;

    private String[] twitterData;

    private ArrayList<String> committees;
    private ArrayList<String> bills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_list);
//        getActionBar().setTitle("Candidates List");

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 1000);
        longitude = intent.getDoubleExtra("longitude", 1000);
        zipcode = intent.getStringExtra("zipcode");

        getInfo(this, url(zipcode, String.valueOf(latitude), String.valueOf(longitude)));

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

    public void getInfo(Activity context, String url_arg){
        all_rep_map = new HashMap<Integer, HashMap<String, String>>();
        final Activity ctx = context;
        final String url = url_arg;
        Log.d("url", url);
        NotifyingThread t = new NotifyingThread() {
            @Override
            public void doRun() {
                Ion.with(ctx)
                        .load(url)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                HashMap<String, String> single_rep_map;
                                JsonArray representatives = result.getAsJsonArray("results");
                                JsonObject rep;
                                int i = 0;
                                for (JsonElement json_rep : representatives) {
                                    single_rep_map = new HashMap<String, String>();
                                    rep = json_rep.getAsJsonObject();
                                    single_rep_map.put("name", rep.get("first_name").getAsString() + " " + rep.get("last_name").getAsString());
                                    single_rep_map.put("party", rep.get("party").getAsString());
                                    single_rep_map.put("email", rep.get("oc_email").getAsString());
                                    single_rep_map.put("website", rep.get("website").getAsString());
                                    single_rep_map.put("twitter_id", rep.get("twitter_id").getAsString());
                                    single_rep_map.put("bioguide_id", rep.get("bioguide_id").getAsString());
                                    single_rep_map.put("start_term", rep.get("term_start").getAsString());
                                    single_rep_map.put("end_term", rep.get("term_end").getAsString());
                                    single_rep_map.put("title", rep.get("title").getAsString());
                                    all_rep_map.put(i, single_rep_map);
                                    i++;
                                }
                                getTwitterInfo(all_rep_map);
                            }
                        });
            }
        };
        t.addListener(this);
        t.start();

    }

    private String url(String zipcode, String latitude, String longitude){
        if(zipcode != null){
            return "http://congress.api.sunlightfoundation.com/legislators/locate?zip="+zipcode+"&apikey=58e3054dfa3c4013b7678add12fd74f4";
        }
        return "http://congress.api.sunlightfoundation.com/legislators/locate?latitude="+latitude+"&longitude="+longitude+"&apikey=58e3054dfa3c4013b7678add12fd74f4";
    }

    public void getTwitterInfo(final HashMap<Integer, HashMap<String, String>> all_rep_map) {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("sb52UFykkwSwbQihDEVDPCdPV");
        cb.setOAuthConsumerSecret("Me35mCXKfS67VFK9kUFL9aJNBn5xbDSL7M5GrZxx0it0PqDom7");
        cb.setOAuthAccessToken("708221428512862208-NHmx62uqUkvNmkzAvLQuP0IE5aFaaTA");
        cb.setOAuthAccessTokenSecret("1RhgBQLcw2evfQfW0WiohaueFwEOxuRnY4uLdLFPb5FQt");
        NotifyingThread nt = new NotifyingThread() {
            @Override
            public void doRun() {
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();
                User user = null;
                try {
                    Status status;
                    for (Integer candidate_int : all_rep_map.keySet()) {
                        user = twitter.showUser(all_rep_map.get(candidate_int).get("twitter_id"));
                        status = user.getStatus();
                        tweet = status.getText();
                        twitterImageURL = user.getOriginalProfileImageURL();
                        all_rep_map.get(candidate_int).put("tweet", tweet);
                        all_rep_map.get(candidate_int).put("photo_url", twitterImageURL);
                    }
                } catch (TwitterException e) {
                    Log.d("twitter", "goes into the catch");
                    e.printStackTrace();
                }
            }
        };
        nt.addListener(this);
        nt.start();

        String names_parties = "";

        for (Integer i : all_rep_map.keySet()) {
            names_parties += all_rep_map.get(i).get("name") + "," + all_rep_map.get(i).get("party") + "," + all_rep_map.get(i).get("photo_url") + ";";
        }

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("names_parties", names_parties);
        startService(sendIntent);
    }

    public void notifyOfThreadComplete(Thread t) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAdapter la = new ListAdapter(LocalList.this, new ArrayList<Integer>(all_rep_map.keySet()), all_rep_map);
                ListView lv = (ListView) findViewById(R.id.listView);

                AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        committees = new ArrayList<String>();
                        bills = new ArrayList<String>();
                        final int which = position;
                        Ion.with(LocalList.this)
                                .load("http://congress.api.sunlightfoundation.com/committees?member_ids=" + all_rep_map.get(position).get("bioguide_id") + "&apikey=58e3054dfa3c4013b7678add12fd74f4")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject jsonResponse) {
                                        JsonArray results = jsonResponse.get("results").getAsJsonArray();
                                        for (JsonElement result : results) {
                                            committees.add(result.getAsJsonObject().get("name").getAsString());
                                        }
                                        Ion.with(LocalList.this)
                                                .load("http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + all_rep_map.get(which).get("bioguide_id") + "&apikey=58e3054dfa3c4013b7678add12fd74f4")
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject jsonResponse) {
                                                        JsonArray results = jsonResponse.get("results").getAsJsonArray();
                                                        for (JsonElement result : results) {
                                                            if (result.getAsJsonObject().get("short_title") != null) {
                                                                bills.add(result.getAsJsonObject().get("short_title").toString());
                                                            }
                                                        }
                                                        Intent activityIntent = new Intent(LocalList.this, DetailedView.class);
                                                        activityIntent.putExtra("map", all_rep_map.get(which));
                                                        activityIntent.putExtra("committees", committees);
                                                        activityIntent.putExtra("bills", bills);
                                                        startActivity(activityIntent);
                                                    }
                                                });
                                    }
                                });

                    }
                };

                lv.setAdapter(la);
                lv.setOnItemClickListener(listener);
            }
        });
    }
}
