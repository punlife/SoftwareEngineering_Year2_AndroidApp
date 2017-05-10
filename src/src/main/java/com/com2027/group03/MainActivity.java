package com.com2027.group03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, TestSpritesActivity.class);
        //startActivity(intent);

        Intent intent = new Intent(this, CardsActivity.class);
        intent.putExtra("cardsPerRow", 3); // 3 cards per row, therefore 2 cards per column
        intent.putExtra("initialShowDelay", 6000); // 3 seconds
        startActivity(intent);
    }
}
