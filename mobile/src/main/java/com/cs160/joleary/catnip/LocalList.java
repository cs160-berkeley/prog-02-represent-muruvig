package com.cs160.joleary.catnip;

/**
 * Created by vignesh on 3/2/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class LocalList extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private ImageButton barbaraBoxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_list);
        getActionBar().setTitle("Candidates List");

        barbaraBoxer = (ImageButton) findViewById(R.id.barbara_boxer);

        barbaraBoxer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(LocalList.this, DetailedView.class);
                startActivity(sendIntent);
            }
        });

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
}