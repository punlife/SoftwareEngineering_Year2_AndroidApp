package com.com2027.group03;

import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by Matus on 22-Feb-17.
 */
public class TestOpenGLTexture extends OpenGLActivity {
    private OpenGLTexture uvgrid;
    private Sprite[] sprites;

    @Override
    public void setup(){
        this.setBackgroundColor(Color.CYAN);

        try {
            uvgrid = new OpenGLTexture(getApplicationContext(), R.drawable.uvgrid);
        } catch(OpenGLTexture.LoadException e){
            throw new RuntimeException(e.getMessage());
        }

        sprites = new Sprite[4];

        int width = getWidth();
        int height = getHeight();

        sprites[0] = new Sprite(width/2 - 144, height/2 - 144, 200, 200, uvgrid);
        sprites[0].setRotation(-45.0f);
        sprites[0].setTextureSubsection(0, 0, 256, 256);

        sprites[1] = new Sprite(width/2 + 144, height/2 - 144, 200, 200, uvgrid);
        sprites[1].setRotation(+45.0f);
        sprites[1].setTextureSubsection(256, 0, 256, 256);

        sprites[2] = new Sprite(width/2 + 144, height/2 + 144, 200, 200, uvgrid);
        sprites[2].setRotation(-45.0f);
        sprites[2].setTextureSubsection(256, 256, 256, 256);

        sprites[3] = new Sprite(width/2 - 144, height/2 + 144, 200, 200, uvgrid);
        sprites[3].setRotation(+45.0f);
        sprites[3].setTextureSubsection(0, 256, 256, 256);

        for(int i = 0; i < 4; i++) {
            sprites[i].setOrigin(Sprite.Origin.CENTER);
            //sprites[i].setColor(Color.rgb(255, 255, 255));
            //sprites[i].setTexture(uvgrid);
        }
    }

    @Override
    public void render(){
        //Log.v("Hello", "Rendering");
        for(int i = 0; i < 4; i++) {
            drawSprite(sprites[i]);
            float rot = sprites[i].getRotation();
            rot += 0.2f;
            if(rot > 360.0f)rot -= 360.0f;
            sprites[i].setRotation(rot);
        }
    }
}
