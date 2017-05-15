package com.com2027.group03;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class StartUpScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startupscreen);

        //Thread to start Main Activity and close StartUpScreen after 4 seconds
        Thread showLogo = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(4000);
                    Intent intent = new Intent(StartUpScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
            showLogo.start();
        }
    }

