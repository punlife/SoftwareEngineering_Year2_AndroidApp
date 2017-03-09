package com.com2027.group03;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Matus on 26-Feb-17.
 */
public class OpenGLFont {
    private OpenGLTexture texture;
    private int height;
    private int totalChars;
    private float[] charWidths;
    private float[] charX;
    private float[] charY;
    private static final String TAG = "OpenGLFont";
    private boolean loaded;
    private int textureSize;
    private float topOffset;

    public class LoadException extends Exception {
        public LoadException(String msg){
            super(msg);
        }
    }

    public static class CharUV {
        public float x;
        public float y;
        public float w;
        public float h;
    }

    public OpenGLFont(){
        this.texture = null;
        this.totalChars = 0x80;
        this.charWidths = new float[this.totalChars];
        this.charX = new float[this.totalChars];
        this.charY = new float[this.totalChars];
        this.loaded = false;
    }

    public OpenGLTexture getTexture() {
        return this.texture;
    }

    public boolean isLoaded(){
        return this.loaded;
    }

    public float getAdvance(char c){
        if(!loaded)return 0.0f;
        int index = (int)c;
        if(c > this.totalChars)return this.charWidths[0];
        else return this.charWidths[index];
    }

    public void getCharUV(char c, CharUV data){
        if(data != null){
            /*int index = (int)c;
            int offsety = index / 16;
            int offsetx = index - offsety*16;
            offsety += 1;
            data.w = getAdvance(c) / (float)(this.textureSize);
            data.h = this.height / (float)(this.textureSize);
            data.x = offsetx / 16.0f;
            data.y = offsety / 16.0f;
            data.y -= data.h;
            data.y += this.topOffset;*/

            int index = (int)c;
            data.x = this.charX[index];
            data.y = this.charY[index];
            data.w = this.charWidths[index] / (float)this.textureSize;
            data.h = this.height / (float)this.textureSize;
        }
    }

    public float getHeight(){
        return this.height;
    }

    public void getStringSize(String str, float[] data){
        if(data == null || str == null)return;

        float x = 0.0f;
        float width = 0.0f;
        float y = this.getHeight();

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            float advance = this.getAdvance(c);

            if(c == '\n'){
                width = Math.max(x, width);
                x = 0;
                y += this.getHeight();
                continue;
            }

            x += advance;
        }
        width = Math.max(x, width);

        data[0] = width;
        data[1] = y;
    }

    public void create(String file, Context context, int pt) throws OpenGLFont.LoadException{
        if(context == null) {
            new LoadException("context is null!");
        }

        //final float scale = context.getResources().getDisplayMetrics().density;
        //mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        float density = context.getResources().getDisplayMetrics().density;
        float pixels = (int)(density * (pt * 2.222f) + 0.5f);
        Log.d(TAG, "Creating font of size: " + pt + "pt (" + pixels + "px)");

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), file);
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(pixels);
        paint.setAntiAlias(true);
        paint.setColor(0xffffffff);

        Paint.FontMetrics font = paint.getFontMetrics();
        this.height = (int)Math.ceil(Math.abs(font.bottom) + Math.abs(font.top));
        this.topOffset = (float)Math.abs(font.bottom);

        float[] textSize = new float[2];
        char[] str = new char[1];
        int maxWidth = 0;
        int maxHeight = 0;

        for(int i = 0; i < this.totalChars; i++){
            str[0] = (char)i;
            paint.getTextWidths(str, 0, 1, textSize);
            this.charWidths[i] = textSize[0];
            maxWidth = (int)Math.max(maxWidth, (int)this.charWidths[i]);
        }

        maxHeight = (int)Math.max(this.height, height);

        this.textureSize = 0;

        Log.d(TAG, "Cell size: " + maxWidth + "x" + maxHeight);

        boolean found = true;
        for(int i = 256; i <= 4096; i *= 2){

            float posx = 0.0f;
            float posy = 0.0f;
            this.textureSize = i;
            found = true;

            for(int c = 0; c < this.totalChars; c++){

                if(posx + this.charWidths[c] > i){
                    posx = 0.0f;
                    posy += maxHeight;
                    if(posy > i){
                        Log.d(TAG, "Test: " + i + "x" + i + " failed!");
                        found = false;
                        break;
                    }
                }

                this.charX[c] = posx;
                this.charY[c] = posy;
                posx += this.charWidths[c];
            }

            if(found)break;
        }

        if(!found) {
            throw new LoadException("Cannot create font! Cell of size: " + maxWidth + "x" + maxHeight + " is too big!");
        }

        Log.d(TAG, "Texture size: " + this.textureSize + "x" + this.textureSize);

        this.topOffset = this.topOffset / this.textureSize;

        // Alpha only
        Bitmap bitmap = Bitmap.createBitmap(this.textureSize, this.textureSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0x00000000);

        for(int i = 0; i < this.totalChars; i++){
            str[0] = (char)i;

            canvas.drawText(str, 0, 1, this.charX[i], this.charY[i], paint);
            this.charX[i] = this.charX[i] / (float)this.textureSize;
            this.charY[i] = this.charY[i] / (float)this.textureSize;
            this.charY[i] -= this.height / (float)this.textureSize;
            this.charY[i] += this.topOffset;
        }

        try {
            texture = new OpenGLTexture();
            texture.load(bitmap);
        } catch (OpenGLTexture.LoadException e){
            throw new LoadException("Texture failed to create: " + e.getMessage());
        }

        bitmap.recycle();

        this.loaded = true;
    }
}
