package com.com2027.group03;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by Matus on 22-Feb-17.
 */
public class TestOpenGLSprites extends OpenGLActivity {
    private OpenGLTexture stag;
    private Sprite[] sprites;

    @Override
    public void setup(Bundle savedInstanceState){
        this.setBackgroundColor(Color.BLACK);

        try {
            stag = new OpenGLTexture(getApplicationContext(), R.drawable.stag);
        } catch(OpenGLTexture.LoadException e){
            throw new RuntimeException(e.getMessage());
        }

        sprites = new Sprite[20];
        Random r = new Random();
        for(int i = 0; i < 20; i++){
            sprites[i] = new Sprite();
            sprites[i].setPos(r.nextInt(this.getWidth() - 160) + 80, r.nextInt(this.getHeight()*2 - 160) - this.getHeight()*2);
            sprites[i].setSize(160, 160);
            sprites[i].setTexture(stag);
            sprites[i].rotateZ(r.nextInt(360));
            sprites[i].setColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
    }

    @Override
    public void render(){
        for(int i = 0; i < 20; i++) {
            drawSprite(sprites[i]);
            sprites[i].y += 10;
            if(sprites[i].y > this.getHeight() + 160){
                Random r = new Random();
                sprites[i].setPos(r.nextInt(this.getWidth() - 160) + 80, -160);
                sprites[i].rotateZ(r.nextInt(360));
                sprites[i].setColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();

        return true;
    }
}