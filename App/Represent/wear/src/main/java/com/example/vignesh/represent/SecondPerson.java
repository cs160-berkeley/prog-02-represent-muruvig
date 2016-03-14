package com.example.vignesh.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by vignesh on 3/4/16.
 */
public class SecondPerson extends Activity {

//    private String[] candidates = {"Barbara Boxer", "Diane Feinstein", "Doug LaMalfa"};
//    private String[] parties = {"Democrat", "Democrat", "Republican"};
//    private int[] pictures = {R.drawable.barbara_boxer, R.drawable.diane_feinstein, R.drawable.doug_lamalfa};
//    private LinearLayout screen;
//    private TextView name;
    private ImageButton picture;
    private Button details;
//    private TextView party;
//    private int i = 0;
//    private String candidateName;
//    private String partyName;
//    private int picture_id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_person);

        details = (Button) findViewById(R.id.details);
        picture = (ImageButton) findViewById(R.id.picture);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

//        if (extras != null) {
//            candidateName = extras.getString("CANDIDATE_NAME");
//            partyName = extras.getString("PARTY");
//            picture_id = extras.getString("PICTURE");
//        }

//        details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
//                Intent detailedViewIntent = new Intent(MainActivity.this, PollView.class);
//                startActivity(detailedViewIntent);
//            }
//        });

        addListenerOnPicture();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 12) {
                int rand = (int) Math.ceil(Math.random() * 100);
                String toPass = "\n" + Integer.toString(rand);
                Intent sendZip = new Intent(SecondPerson.this, PollView.class);
                sendZip.putExtra("rep", toPass);
                startActivity(sendZip);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    //    public void getOtherView(MainActivity v, String name, String party, String picture) {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.this);
//
//        startActivity(intent);
//    }
//    public void getNextView(MainActivity v) {
//        Intent intent = new Intent(getApplicationContext(), PollView.class);
//        startActivity(intent);
//    }
    public void addListenerOnPicture() {
        picture = (ImageButton) findViewById(R.id.picture);
        picture.setOnTouchListener(new SwipeListener(SecondPerson.this) {
            @Override
            public void onSwipeTop() {
                Toast.makeText(SecondPerson.this, "top", Toast.LENGTH_SHORT).show();
//                i += 1;
//                name.setText(candidates[i % 3]);
//                party.setText(parties[i % 3]);
//                Log.d("test_if_working", Integer.toString(i));
//                picture.setImageResource(pictures[i % 3]);
            }

            public void onSwipeRight() {
                Toast.makeText(SecondPerson.this, "right", Toast.LENGTH_SHORT).show();
                Intent original = new Intent(SecondPerson.this, MainActivity.class);
                startActivity(original);
            }

            public void onSwipeLeft() {
                Toast.makeText(SecondPerson.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(SecondPerson.this, "bottom", Toast.LENGTH_SHORT).show();
//                i -= 1;
//                name.setText(candidates[i % 3]);
//                party.setText(parties[i % 3]);
//                Log.d("test_if_working", Integer.toString(i));
//                picture.setImageResource(pictures[i % 3]);
            }

        });
    }
}
