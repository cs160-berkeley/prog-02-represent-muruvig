package com.example.vignesh.represent;

/**
 * Created by vignesh on 3/3/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailedView extends Activity implements ThreadCompleteListener {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private HashMap<String, String> map;
    private ArrayList<String> committees;
    private ArrayList<String> bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);
        Intent intent = getIntent();
        map = (HashMap<String, String>) intent.getSerializableExtra("map");
        committees = (ArrayList<String>) intent.getSerializableExtra("committees");
        bills = (ArrayList<String>) intent.getSerializableExtra("bills");

        String party_string = "";

        ListView committee_list = (ListView) findViewById(R.id.committees_list);
        ListView bills_list = (ListView) findViewById(R.id.bills_list);

        ArrayAdapter<String> committees_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                committees);

        ArrayAdapter<String> bills_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                bills);

        if (map.get("party").equals("D")) {
            party_string = "Democrat";
        } else if (map.get("party").equals("R")) {
            party_string = "Republican";
        } else if (map.get("party").equals("I")) {
            party_string = "Independent";
        }

        ((TextView) findViewById(R.id.name)).setText(map.get("name"));
        ((TextView) findViewById(R.id.party)).setText(party_string);
        ((TextView) findViewById(R.id.email)).setText(map.get("email"));
        ((TextView) findViewById(R.id.website)).setText(map.get("website"));
        ((TextView) findViewById(R.id.tweet)).setText(map.get("tweet"));
        ((TextView) findViewById(R.id.term)).setText(map.get("start_term") + " to " + map.get("end_term"));

        bills_list.setAdapter(bills_adapter);
        committee_list.setAdapter(committees_adapter);

        ImageView imageView = (ImageView) findViewById(R.id.picture);
        Picasso.with(this).load(map.get("photo_url")).into(imageView);

        Log.d("detailed", committees.toString());
        Log.d("detailed", bills.toString());
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

    public void notifyOfThreadComplete(Thread t) {
        Log.d("detailed_complete", committees.toString());

    }
}