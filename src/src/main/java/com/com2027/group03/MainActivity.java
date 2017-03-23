package com.com2027.group03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button help = (Button) findViewById(R.id.helpButton);

        assert help != null;
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HelpPopUp.class));
            }
        });


        //Intent intent = new Intent(this, TestSpritesActivity.class);
        //startActivity(intent);

        // testing git
    }
}
