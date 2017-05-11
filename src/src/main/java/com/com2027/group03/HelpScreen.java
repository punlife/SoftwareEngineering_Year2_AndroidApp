package com.com2027.group03;


import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by amoshgurung on 25/04/2017.
 */
public class HelpScreen extends OpenGLActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen);

        //Getting screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Default height and width of the screen
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //New values for the screen
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        //Textview for description
        TextView description_text1 = (TextView) findViewById(R.id.help_description1);
        description_text1.setText(Html.fromHtml(getString(R.string.help_screen_description1)));

        TextView description_text2 = (TextView) findViewById(R.id.help_description2);
        description_text2.setText(Html.fromHtml(getString(R.string.help_screen_description2)));

        //button to close popup
        ImageView closeButton = (ImageView) findViewById(R.id.imageView_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
