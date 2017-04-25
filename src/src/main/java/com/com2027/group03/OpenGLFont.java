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

    /**
     * Character data
     */
    public static class CharUV {
        public float x;
        public float y;
        public float w;
        public float h;
    }

    /**
     * Default constructor
     */
    public OpenGLFont(){
        this.texture = null;
        this.totalChars = 0x80;
        this.charWidths = new float[this.totalChars];
        this.charX = new float[this.totalChars];
        this.charY = new float[this.totalChars];
        this.loaded = false;
    }

    /**
     * @return Reference to the texture atlas of glyphs
     */
    public OpenGLTexture getTexture() {
        return this.texture;
    }

    /**
     * @return Returns true of font as been loaded
     */
    public boolean isLoaded(){
        return this.loaded;
    }

    /**
     * Returns an advance of specific character
     * @param c Char to get advance of
     * @return Advance in pixels
     */
    public float getAdvance(char c){
        if(!loaded)return 0.0f;
        int index = (int)c;
        if(c > this.totalChars)return this.charWidths[0];
        else return this.charWidths[index];
    }

    /**
     * Gets a UV coordinates of specific character and puts it in CharUV
     * @param c Char to get UV of
     * @param data Non-null reference to CharUV
     */
    public void getCharUV(char c, CharUV data){
        if(data != null){
            int index = (int)c;
            data.x = this.charX[index];
            data.y = this.charY[index];
            data.w = this.charWidths[index] / (float)this.textureSize;
            data.h = this.height / (float)this.textureSize;
        }
    }

    /**
     * @return Height of the font in pixels
     */
    public float getHeight(){
        return this.height;
    }

    /**
     * Calculates size of the string that would be occupied if the string was rendered
     * on the screen.
     * @param str String to get size from.
     * @param data Array of 2 floats to put the result in.
     */
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

    /**
     * Loads a true type font
     * @param file Name of the file
     * @param context Reference to the Activity context
     * @param pt Size of the desired font in points
     * @throws OpenGLFont.LoadException If loading failed
     */
    public void create(String file, Context context, int pt) throws OpenGLFont.LoadException{
        if(context == null) {
            new LoadException("context is null!");
        }

        //final float scale = context.getResources().getDisplayMetrics().density;
        //mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        float density = context.getResources().getDisplayMetrics().density;
        float pixels = (int)(density * (pt * 2.222f) + 0.5f);
        Log.d(TAG, "Creating font of size: " + pt + "pt (" + pixels + "px)");

        // Load true type font
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), file);

        // Font will be rendered glyph by glyph by Paint class
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(pixels);
        paint.setAntiAlias(true);
        paint.setColor(0xffffffff);
        Paint.FontMetrics font = paint.getFontMetrics();

        // Calculate font height
        this.height = (int)Math.ceil(Math.abs(font.bottom) + Math.abs(font.top));
        this.topOffset = (float)Math.abs(font.bottom);

        float[] textSize = new float[2];
        char[] str = new char[1];
        int maxWidth = 0;
        int maxHeight = 0;

        // Calculate height of all characters
        for(int i = 0; i < this.totalChars; i++){
            str[0] = (char)i;
            paint.getTextWidths(str, 0, 1, textSize);
            this.charWidths[i] = textSize[0];
            maxWidth = (int)Math.max(maxWidth, (int)this.charWidths[i]);
        }

        // Get the maximum height of all glyphs
        maxHeight = (int)Math.max(this.height, height);

        this.textureSize = 0;

        // The cell size is a rectangle that can hold any glyph from this TTF font
        Log.d(TAG, "Cell size: " + maxWidth + "x" + maxHeight);

        // Try to fit all glyphs into the texture.
        // Start with texture size of 256 and double it each time.
        // Continue until 4096 which is the "optimal" limit of OpenGL texture size of android phones
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
                        // Could not fit any more glyphs
                        Log.d(TAG, "Test: " + i + "x" + i + " failed!");
                        found = false;
                        break;
                    }
                }

                this.charX[c] = posx;
                this.charY[c] = posy;
                posx += this.charWidths[c];
            }

            // All glyphs fit?
            if(found)break;
        }

        // Throw if any of the available texture sizes were too small to fit all glyphs.
        if(!found) {
            throw new LoadException("Cannot create font! Cell of size: " + maxWidth + "x" + maxHeight + " is too big!");
        }

        Log.d(TAG, "Texture size: " + this.textureSize + "x" + this.textureSize);

        this.topOffset = this.topOffset / this.textureSize;

        // Create bitmap as RGBA 8-bit per channel.
        // Bitmap.Config.ALPHA_8 would fit better, however it seems to not to work at all.
        // Bitmap.Config.ALPHA_8 would also fit better with GL_RED or GL_LUMINANCE_8 or GL_ALPHA_8
        Bitmap bitmap = Bitmap.createBitmap(this.textureSize, this.textureSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0x00000000);

        // Render all glyphs into temporary bitmap
        for(int i = 0; i < this.totalChars; i++){
            str[0] = (char)i;

            canvas.drawText(str, 0, 1, this.charX[i], this.charY[i], paint);
            this.charX[i] = this.charX[i] / (float)this.textureSize;
            this.charY[i] = this.charY[i] / (float)this.textureSize;
            this.charY[i] -= this.height / (float)this.textureSize;
            this.charY[i] += this.topOffset;
        }

        // Create texture from the bitmap
        try {
            texture = new OpenGLTexture();
            texture.load(bitmap);
        } catch (OpenGLTexture.LoadException e){
            throw new LoadException("Texture failed to create: " + e.getMessage());
        }

        // Free bitmap
        bitmap.recycle();

        this.loaded = true;
    }
}
