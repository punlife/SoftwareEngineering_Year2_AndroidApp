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
    private static final String TAG = "OpenGLFont";
    private boolean loaded;
    private int textureSize;
    private float topOffset;

    public static class CharUV {
        public float x;
        public float y;
        public float w;
        public float h;
    }

    public OpenGLFont(){
        this.texture = null;
        this.totalChars = 0xFF;
        this.charWidths = new float[this.totalChars];
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
            int index = (int)c;
            int offsety = index / 16;
            int offsetx = index - offsety*16;
            offsety += 1;
            data.w = getAdvance(c) / (float)(this.textureSize);
            data.h = this.height / (float)(this.textureSize);
            data.x = offsetx / 16.0f;
            data.y = offsety / 16.0f;
            data.y -= data.h;
            data.y += this.topOffset;
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

    public boolean create(String file, Context context, int pt){
        if(context == null) {
            Log.e(TAG, "context is null!");
            return false;
        }

        //final float scale = context.getResources().getDisplayMetrics().density;
        //mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        float density = context.getResources().getDisplayMetrics().density;
        float pixels = (int)(density * (pt * 2.222f) + 0.5f);
        Log.v(TAG, "Creating font of size: " + pt + "pt (" + pixels + "px)");

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

        int cellSize = Math.max(maxWidth, maxHeight);
        this.textureSize = 0;

        Log.v(TAG, "Cell size: " + cellSize + "x" + cellSize);

        if(cellSize <= 256/16){
            this.textureSize = 256;
            cellSize = 256/16;
        }
        else if(cellSize <= 512/16){
            this.textureSize = 512;
            cellSize = 512/16;
        }
        else if(cellSize <= 1024/16){
            this.textureSize = 1024;
            cellSize = 1024/16;
        }
        else if(cellSize <= 2048/16){
            this.textureSize = 2048;
            cellSize = 2048/16;
        }
        else {
            Log.e(TAG, "Cannot create font! Cell is too big!");
            return false;
        }

        Log.v(TAG, "Texture size: " + this.textureSize + "x" + this.textureSize);

        this.topOffset = this.topOffset / this.textureSize;

        // Alpha only
        Bitmap bitmap = Bitmap.createBitmap(this.textureSize, this.textureSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0x00000000);

        float posx = 0.0f;
        float posy = cellSize;
        for(int i = 0; i < this.totalChars; i++){
            str[0] = (char)i;

            canvas.drawText(str, 0, 1, posx, posy, paint);
            posx += cellSize;
            if(posx >= this.textureSize){
                posx = 0.0f;
                posy += cellSize;
            }
        }

        FileOutputStream out = null;
        try {
            String sdcard = Environment.getExternalStorageDirectory().toString();
            out = new FileOutputStream(sdcard + "/TestOpenGLFont.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            texture = new OpenGLTexture();
            texture.load(bitmap);
        } catch (OpenGLTexture.LoadException e){
            Log.e(TAG, "Texture failed to create: " + e.getMessage());
            return false;
        }

        bitmap.recycle();

        this.loaded = true;
        return true;
    }
}
