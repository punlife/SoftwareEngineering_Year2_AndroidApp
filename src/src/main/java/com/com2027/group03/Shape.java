package com.com2027.group03;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Shape {
    public enum Origin {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        LEFT, CENTER, RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    };

    public Shape(){
        origin = Origin.CENTER;
        color = Color.WHITE;
        rot = 0.0f;
        setPos(0, 0);
    }

    public Shape(int x, int y){
        origin = Origin.CENTER;
        color = Color.WHITE;
        rot = 0.0f;
        setPos(x, y);
    }

    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getPosX(){
        return x;
    }

    public int getPosY(){
        return y;
    }

    public Origin getOrigin(){
        return origin;
    }

    public Color getColor(){
        return color;
    }

    public float getRotation(){
        return rot;
    }

    public float getRot(){
        return rot;
    }

    public void setOrigin(Origin origin){
        this.origin = origin;
    }

    public void setRotation(float rot){
        this.rot = rot;
    }

    public void setColor(com.com2027.group03.Color color){
        this.color = color;
    }

    public int x;
    public int y;
    public Origin origin;
    public float rot;
    public com.com2027.group03.Color color;
}
