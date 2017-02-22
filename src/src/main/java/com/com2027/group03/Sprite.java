package com.com2027.group03;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Sprite extends Rectangle {
    public Sprite(){
        super(0, 0, 1, 1);
        setSize(1, 1);
        this.texture = null;
    }

    public Sprite(int x, int y, int w, int h, OpenGLTexture texture){
        super(x, y, w, h);
        setTexture(texture);
    }

    public void setTexture(OpenGLTexture texture) {
        this.texture = texture;
        subx = 0.0f;
        suby = 0.0f;
        subw = 1.0f;
        subh = 1.0f;
    }

    public void setTextureSubsection(int startx, int starty, int subwidth, int subheight){
        subx = startx / (float)texture.getWidth();
        suby = starty / (float)texture.getHeight();
        subw = subwidth / (float)texture.getWidth();
        subh = subheight / (float)texture.getHeight();
    }

    public OpenGLTexture getTexture() {
        return texture;
    }

    public float getSubX(){
        return subx;
    }

    public float getSubY(){
        return suby;
    }

    public float getSubW(){
        return subw;
    }

    public float getSubH(){
        return subh;
    }

    public OpenGLTexture texture;
    public float subx;
    public float suby;
    public float subw;
    public float subh;
}
