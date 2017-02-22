package com.com2027.group03;

import android.app.Activity;
import android.opengl.GLES20;
import android.os.Bundle;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLActivity extends Activity implements GLSurfaceView.Renderer {
    private GLSurfaceView glView;
    private OpenGLShaders.SpriteShader spriteShader;
    private OpenGLShaders.RectangleShader rectangleShader;
    private static final String TAG = "OpenGLActivity";
    private FloatBuffer spriteVbo;
    private OpenGLMatrix projection;
    private int width, height;

    public OpenGLActivity(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        glView = new GLSurfaceView(this);
        glView.setEGLContextClientVersion(2);
        glView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glView.setRenderer(this);
        // Render the view only when there is a change in the drawing data
        //glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glView);
    }

    public void setBackgroundColor(Color color){
        // Set the background frame color
        GLES20.glClearColor(color.r, color.g, color.b, 1.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        width = glView.getWidth();
        height = glView.getHeight();

        try {
            spriteShader = new OpenGLShaders.SpriteShader();
        } catch (OpenGLProgram.CompileException e){
            throw new RuntimeException("Failed to create sprite shader:\n" + e.getMessage());
        }

        try {
            rectangleShader = new OpenGLShaders.RectangleShader();
        } catch (OpenGLProgram.CompileException e){
            throw new RuntimeException("Failed to create rectangle shader:\n" + e.getMessage());
        }

        final float[] vertices = {
                -0.5f, -0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
                -0.5f, 0.5f,
                0.5f, 0.5f
        };

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        spriteVbo = byteBuffer.asFloatBuffer();
        spriteVbo.put(vertices);
        spriteVbo.rewind();

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        setup();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        projection = OpenGLMatrix.makeOrthoMatrix(0, width, height, 0, -1.0f, 1.0f);
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        render();
    }

    public void render(){

    }

    public void setup(){

    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void drawRectangle(Rectangle rectangle){
        GLES20.glUseProgram(rectangleShader.getHandle());
        GLES20.glEnableVertexAttribArray(rectangleShader.uniformVertLoc);
        GLES20.glUniformMatrix4fv(rectangleShader.uniformProjLoc, 1, false, projection.ptr, 0);
        GLES20.glUniform4f(rectangleShader.uniformColorLoc, rectangle.color.r, rectangle.color.g, rectangle.color.b, rectangle.color.a);
        switch(rectangle.origin){
            case TOP_LEFT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, rectangle.width/2, rectangle.height/2);
                break;
            }
            case TOP_CENTER: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, 0, rectangle.height/2);
                break;
            }
            case TOP_RIGHT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, -rectangle.width/2, rectangle.height/2);
                break;
            }
            case LEFT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, rectangle.width/2, 0);
                break;
            }
            case CENTER: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, 0, 0);
                break;
            }
            case RIGHT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, -rectangle.width/2, 0);
                break;
            }
            case BOTTOM_LEFT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, rectangle.width/2, -rectangle.height/2);
                break;
            }
            case BOTTOM_CENTER: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, 0, -rectangle.height/2);
                break;
            }
            case BOTTOM_RIGHT: {
                GLES20.glUniform4f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y, -rectangle.width/2, -rectangle.height/2);
                break;
            }
        }
        GLES20.glUniform3f(rectangleShader.uniformSizeLoc, rectangle.width, rectangle.height, rectangle.rot);
        //GLES20.glUniform2f(rectangleShader.uniformPosLoc, rectangle.x, rectangle.y);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 2, spriteVbo);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDisableVertexAttribArray(rectangleShader.uniformVertLoc);
    }

    public void drawSprite(Sprite sprite){
        if(sprite.getTexture() == null) {
            drawRectangle(sprite);
        }

        GLES20.glUseProgram(spriteShader.getHandle());
        GLES20.glEnableVertexAttribArray(spriteShader.uniformVertLoc);
        GLES20.glUniformMatrix4fv(spriteShader.uniformProjLoc, 1, false, projection.ptr, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sprite.getTexture().getHandle());
        GLES20.glUniform1i(spriteShader.uniformTexLoc, 0);
        GLES20.glUniform4f(spriteShader.uniformColorLoc, sprite.color.r, sprite.color.g, sprite.color.b, sprite.color.a);
        GLES20.glUniform4f(spriteShader.uniformSubLoc, sprite.subx, sprite.suby, sprite.subw, sprite.subh);
        switch(sprite.origin){
            case TOP_LEFT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, sprite.width/2, sprite.height/2);
                break;
            }
            case TOP_CENTER: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, 0, sprite.height/2);
                break;
            }
            case TOP_RIGHT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, -sprite.width/2, sprite.height/2);
                break;
            }
            case LEFT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, sprite.width/2, 0);
                break;
            }
            case CENTER: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, 0, 0);
                break;
            }
            case RIGHT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, -sprite.width/2, 0);
                break;
            }
            case BOTTOM_LEFT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, sprite.width/2, -sprite.height/2);
                break;
            }
            case BOTTOM_CENTER: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, 0, -sprite.height/2);
                break;
            }
            case BOTTOM_RIGHT: {
                GLES20.glUniform4f(spriteShader.uniformPosLoc, sprite.x, sprite.y, -sprite.width/2, -sprite.height/2);
                break;
            }
        }
        GLES20.glUniform3f(spriteShader.uniformSizeLoc, sprite.width, sprite.height, sprite.rot);
        //GLES20.glUniform2f(spriteShader.uniformPosLoc, sprite.x, sprite.y);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 2, spriteVbo);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDisableVertexAttribArray(spriteShader.uniformVertLoc);
    }
}
