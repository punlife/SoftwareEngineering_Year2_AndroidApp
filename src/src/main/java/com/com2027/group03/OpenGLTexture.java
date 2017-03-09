package com.com2027.group03;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLTexture {
    private int texture;
    private int width;
    private int height;

    public class LoadException extends Exception {
        public LoadException(String msg){
            super(msg);
        }
    }

    public OpenGLTexture(){

    }

    public OpenGLTexture(final Context context, int id) throws LoadException {
        load(context, id);
    }

    public OpenGLTexture(Bitmap bmp) throws LoadException {
        load(bmp);
    }

    public void load(final Context context, int id) throws LoadException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, options);
        if(bmp == null){
            throw new LoadException("Image failed to load from the resource: " + id);
        }
        try {
            load(bmp);
        } catch (LoadException e){
            bmp.recycle();
            throw e;
        }
        bmp.recycle();
    }

    public void load(Bitmap bmp) throws LoadException {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        texture = textures[0];

        if(texture == 0){
            throw new LoadException("Failed to create OpenGL Texture!");
        }

        width = bmp.getWidth();
        height = bmp.getHeight();

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
    }

    public int getHandle() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
