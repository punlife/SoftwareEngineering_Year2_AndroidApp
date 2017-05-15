package com.com2027.group03;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.ScriptGroup;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Intent intent = new Intent(this, TestSpritesActivity.class);
        //startActivity(intent);

        preferences = this.getSharedPreferences("Spark", Context.MODE_PRIVATE);

        if (preferences.getBoolean("firstrun", true)) {

            try {
                InputStream inputStream = this.getAssets().open("Terms_Conditions.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                List<String> lines = new LinkedList<>();
                String line = bufferedReader.readLine();

                while (line != null) {
                    lines.add(line);
                    line = bufferedReader.readLine();
                }

                String separator = System.getProperty("line.separator");
                String contents = TextUtils.join(separator, lines);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Terms and Conditions");
                builder.setMessage(contents);
                builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences.edit().putBoolean("firstrun", false).commit();
                    }
                });
                builder.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences.edit().putBoolean("firstrun", true).commit();
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //Button to open help screen.
        ImageView help_iw = (ImageView)findViewById(R.id.help_button);
        help_iw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHelpScreen = new Intent(MainActivity.this, HelpScreen.class);
                startActivity(intentHelpScreen);
            }
        });


        ImageView setting_iw = (ImageView)findViewById(R.id.settings_button);

        //Button to start the game.
        Button play_b = (Button)findViewById(R.id.play_button);
        play_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardsActivity.class);
                intent.putExtra("cardsPerRow", 3); // 3 cards per row, therefore 2 cards per column
                intent.putExtra("initialShowDelay", 6000); // 3 seconds
                startActivity(intent);
            }
        });
        Button highscore_b = (Button)findViewById(R.id.highScore_button);

        //Button to start Contact Page.
        Button contactUs_b = (Button)findViewById(R.id.contactUs_button);
        contactUs_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactUs = new Intent(MainActivity.this, ContactPage.class);
                startActivity(contactUs);
            }
        });

    }
}
