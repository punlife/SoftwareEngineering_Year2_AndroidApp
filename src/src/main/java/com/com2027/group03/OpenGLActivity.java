package com.com2027.group03;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
    private static final String TAG = "OpenGLActivity";
    private FloatBuffer spriteVbo;
    private OpenGLMatrix projection;
    private OpenGLMatrix defaultModel;
    private int width, height;
    private Bundle savedInstanceState;

    public OpenGLActivity(){
        defaultModel = new OpenGLMatrix(1.0f);
        //Matrix.rotateM(defaultModel.ptr, 0, 10.0f, 0.0f, 0.0f, 1.0f);
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

        Log.e(TAG, "savedInstanceState == null? " + (savedInstanceState == null));
        this.savedInstanceState = savedInstanceState;
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

        // Create and load sprite shader
        try {
            spriteShader = new OpenGLShaders.SpriteShader();
        } catch (OpenGLProgram.CompileException e){
            throw new RuntimeException("Failed to create sprite shader:\n" + e.getMessage());
        }

        // Quad constructed with two triangles.
        // OpenGL ES does not support rendering of GL_QUADS
        final float[] vertices = {
                -0.5f, -0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
                -0.5f, 0.5f,
                0.5f, 0.5f
        };

        // Very weird way of filling GL_VERTEX_BUFFER
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        spriteVbo = byteBuffer.asFloatBuffer();
        spriteVbo.put(vertices);
        spriteVbo.rewind();

        // Enable blending, required by OpenGLFont class
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Call setup
        setup(this.savedInstanceState);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // Recalculate projection matrix
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

    /**
     * This method is called every time a new frame is being rendered.
     * A user should override this method.
     */
    public void render(){

    }

    /**
     * This method is called once the OpenGL context is crated.
     * A user should override this method.
     */
    public void setup(Bundle savedInstanceState){

    }

    /**
     * @return The width of the rendering area in pixels
     */
    public int getWidth(){
        return width;
    }

    /**
     * @return The height of the rendering area in pixels
     */
    public int getHeight(){
        return height;
    }

    /**
     * Draws sprite on the screen
     * @param sprite Non-null reference to the Sprite instance
     */
    public void drawSprite(Sprite sprite){
        if(sprite.getTexture() == null) {
            return;
        }

        // Enable sprite shader
        GLES20.glUseProgram(spriteShader.getHandle());
        GLES20.glEnableVertexAttribArray(spriteShader.uniformVertLoc);

        // Model and projection matrices.
        // View matrix is ignored as the camera position is locked to [0, 0, 0, 0]
        GLES20.glUniformMatrix4fv(spriteShader.uniformProjLoc, 1, false, projection.ptr, 0);
        GLES20.glUniformMatrix4fv(spriteShader.uniformModelLoc, 1, false, sprite.model.ptr, 0);

        // Activate sprite's texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sprite.getTexture().getHandle());

        // Set uniforms
        GLES20.glUniform1i(spriteShader.uniformTexLoc, 0);
        GLES20.glUniform4f(spriteShader.uniformColorLoc, sprite.color.r, sprite.color.g, sprite.color.b, sprite.color.a);
        GLES20.glUniform4f(spriteShader.uniformSubLoc, sprite.subx, sprite.suby, sprite.subw, sprite.subh);

        // Adjust position based on the origin of the sprite
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
        // Set size
        GLES20.glUniform2f(spriteShader.uniformSizeLoc, sprite.width, sprite.height);
        //GLES20.glUniform2f(spriteShader.uniformPosLoc, sprite.x, sprite.y);

        // Bind VBO and render QUAD
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 2, spriteVbo);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDisableVertexAttribArray(spriteShader.uniformVertLoc);
    }

    /**
     * Draws a string on the screen
     * @param x Position X in pixels
     * @param y Position Y in pixels
     * @param font Reference to the font class
     * @param str String to render
     * @param color Color of the string
     */
    public void drawString(float x, float y, OpenGLFont font, String str, Color color){
        if(font == null || !font.isLoaded() || str == null)return;

        // The same principle as in drawSprite

        // Activate sprite shader, we will use it as font shader
        GLES20.glUseProgram(spriteShader.getHandle());
        GLES20.glEnableVertexAttribArray(spriteShader.uniformVertLoc);

        // Model and projection matrices.
        // View matrix is ignored as the camera position is locked to [0, 0, 0, 0]
        GLES20.glUniformMatrix4fv(spriteShader.uniformProjLoc, 1, false, projection.ptr, 0);
        GLES20.glUniformMatrix4fv(spriteShader.uniformModelLoc, 1, false, this.defaultModel.ptr, 0);

        // Bind VBO
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 2, spriteVbo);

        // Activate Font texture (glyph atlas)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, font.getTexture().getHandle());
        GLES20.glUniform1i(spriteShader.uniformTexLoc, 0);

        // Set the color to WHITE if the color was not set
        if(color != null)
            GLES20.glUniform4f(spriteShader.uniformColorLoc, color.r, color.g, color.b, color.a);
        else
            GLES20.glUniform4f(spriteShader.uniformColorLoc, Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);

        // Draw characters one by one
        float posx = 0;
        float posy = 0;
        float fontHeight = font.getHeight();
        OpenGLFont.CharUV uvs = new OpenGLFont.CharUV();
        for(int i = 0; i < str.length(); i++){
            char chr = str.charAt(i);
            float advance = font.getAdvance(chr);

            // On a newline character, move back to X and go down
            if(chr == '\n') {
                posy += fontHeight;
                posx = 0;
                continue;
            }

            font.getCharUV(chr, uvs);
            GLES20.glUniform4f(spriteShader.uniformSubLoc, uvs.x, uvs.y, uvs.w, uvs.h);
            GLES20.glUniform4f(spriteShader.uniformPosLoc, x, y, posx + advance/2, posy + fontHeight/2);
            GLES20.glUniform2f(spriteShader.uniformSizeLoc, advance, fontHeight);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            posx += font.getAdvance(chr);
        }

        GLES20.glDisableVertexAttribArray(spriteShader.uniformVertLoc);
    }

    /**
     * Draws text on the screen
     * @param text Non-null reference to the Text instance
     */
    public void drawText(Text text){
        if(text == null || text.getFont() == null || !text.getFont().isLoaded() || text.getString() == null)return;

        // The same principle as in drawString
        // The difference is that text is treated as sprite, therefor it has an origin
        GLES20.glUseProgram(spriteShader.getHandle());
        GLES20.glEnableVertexAttribArray(spriteShader.uniformVertLoc);
        GLES20.glUniformMatrix4fv(spriteShader.uniformProjLoc, 1, false, projection.ptr, 0);
        GLES20.glUniformMatrix4fv(spriteShader.uniformModelLoc, 1, false, text.model.ptr, 0);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 4 * 2, spriteVbo);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, text.getFont().getTexture().getHandle());
        GLES20.glUniform1i(spriteShader.uniformTexLoc, 0);
        if(text.color != null)
            GLES20.glUniform4f(spriteShader.uniformColorLoc, text.color.r, text.color.g, text.color.b, text.color.a);
        else
            GLES20.glUniform4f(spriteShader.uniformColorLoc, Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, Color.WHITE.a);

        float offsetx = 0.0f;
        float offsety = 0.0f;

        switch(text.origin){
            case TOP_LEFT: {
                offsetx = 0; offsety = 0;
                break;
            }
            case TOP_CENTER: {
                offsetx = -text.width/2; offsety = 0;
                break;
            }
            case TOP_RIGHT: {
                offsetx = -text.width; offsety = 0;
                break;
            }
            case LEFT: {
                offsetx = 0; offsety = -text.height/2;
                break;
            }
            case CENTER: {
                offsetx = -text.width/2; offsety = -text.height/2;
                break;
            }
            case RIGHT: {
                offsetx = -text.width; offsety = -text.height/2;
                break;
            }
            case BOTTOM_LEFT: {
                offsetx = 0; offsety = -text.height;
                break;
            }
            case BOTTOM_CENTER: {
                offsetx = -text.width/2; offsety = -text.height;
                break;
            }
            case BOTTOM_RIGHT: {
                offsetx = -text.width; offsety = -text.height;
                break;
            }
        }

        float posx = 0;
        float posy = 0;
        OpenGLFont font = text.getFont();
        String str = text.getString();

        float fontHeight = text.getFont().getHeight();
        OpenGLFont.CharUV uvs = new OpenGLFont.CharUV();
        for(int i = 0; i < str.length(); i++){
            char chr = str.charAt(i);
            float advance = font.getAdvance(chr);

            if(chr == '\n') {
                posy += fontHeight;
                posx = 0;
                continue;
            }

            font.getCharUV(chr, uvs);
            GLES20.glUniform4f(spriteShader.uniformSubLoc, uvs.x, uvs.y, uvs.w, uvs.h);
            GLES20.glUniform4f(spriteShader.uniformPosLoc, text.x, text.y, posx + advance/2 + offsetx, posy + fontHeight/2 + offsety);
            GLES20.glUniform2f(spriteShader.uniformSizeLoc, advance, fontHeight);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            posx += font.getAdvance(chr);
        }

        GLES20.glDisableVertexAttribArray(spriteShader.uniformVertLoc);
    }
}
