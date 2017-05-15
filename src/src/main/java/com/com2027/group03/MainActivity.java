package com.com2027.group03;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Shake related fields
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeSensor mShakeSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeSensor = new ShakeSensor();
        mShakeSensor.setOnShakeListener(new ShakeSensor.OnShakeListener() {
            @Override
            public void onShake(int count) {

                handleShakeEvent(count);
            }
        });


        //Intent intent = new Intent(this, TestSpritesActivity.class);
        //startActivity(intent);

        //Button to open help screen.
        ImageView help_iw = (ImageView) findViewById(R.id.help_button);
        help_iw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHelpScreen = new Intent(MainActivity.this, HelpScreen.class);
                startActivity(intentHelpScreen);
            }
        });


        ImageView setting_iw = (ImageView) findViewById(R.id.settings_button);

        //Button to start the game.
        Button play_b = (Button) findViewById(R.id.play_button);
        play_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardsActivity.class);
                intent.putExtra("cardsPerRow", 3); // 3 cards per row, therefore 2 cards per column
                intent.putExtra("initialShowDelay", 6000); // 3 seconds
                startActivity(intent);
            }
        });

        //Button to start Contact Page.
        Button contactUs_b = (Button) findViewById(R.id.contactUs_button);
        contactUs_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactUs = new Intent(MainActivity.this, ContactPage.class);
                startActivity(contactUs);
            }
        });
        //Button to show High scores.
        Button highscore_b = (Button) findViewById(R.id.highScore_button);
        final List<String> test = new ArrayList<String>();
        test.add(" ");
        File file = new File(getFilesDir(),"highscores");
        final ArrayAdapter<String> adapter;
        if (file.exists()) {
            adapter = new HighScoreAdapter(MainActivity.this, readHighscoresFromFile());
            highscore_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View v = inflater.inflate(R.layout.highscore_dialog, null);
                    builder.setView(v)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    ListView lv = (ListView) v.findViewById(R.id.hslistview);
                    lv.setAdapter(adapter);
                    builder.create().show();

                }
            });
        } else {
            if (file.exists()) {
                highscore_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View v = inflater.inflate(R.layout.highscore_dialog, null);
                        builder.setView(v)
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                                .setMessage("No data present");
                        builder.create().show();

                    }
                });
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeSensor, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeSensor);
        super.onPause();
    }
    private List<String> readHighscoresFromFile() {
        List<String> highscores = new ArrayList<String>();
        File file = new File(getFilesDir(), "highscores");
        if (file.exists()) {
            String line;
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                try {
                    while ((line = br.readLine()) != null) {
                        highscores.add(line);
                    }
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return highscores;
    }
    public void handleShakeEvent(int count){
        if (count > 2) {
            android.app.AlertDialog.Builder restartShake = new android.app.AlertDialog.Builder(MainActivity.this);
            restartShake.setTitle("Application restart")
                    .setMessage("Do you want to restart the application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mStartActivity = new Intent(MainActivity.this, MainActivity.class);
                            int mPendingIntentId = 123456;
                            PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });
                    restartShake.create().show();
        }
    }
}
