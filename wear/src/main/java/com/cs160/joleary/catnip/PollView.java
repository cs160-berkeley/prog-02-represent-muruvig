package com.cs160.joleary.catnip;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by vignesh on 3/4/16.
 */
public class PollView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);

        String spaces = "         ";
        Random r = new Random();
        int num = r.nextInt() % 100;

        String finalString = Integer.toString(num) + "%" + spaces + Integer.toString(100 - num) + "%";
        TextView t = (TextView) findViewById(R.id.percent);
        t.setText(finalString);
    }
}