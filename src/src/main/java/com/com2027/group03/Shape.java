package com.com2027.group03;

import android.opengl.Matrix;
import android.util.Log;

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
        model = new OpenGLMatrix(1.0f);
        origin = Origin.CENTER;
        color = Color.WHITE;
        setPos(0, 0);
        setSize(0, 0);
    }

    public Shape(float x, float y, float w, float h){
        model = new OpenGLMatrix(1.0f);
        origin = Origin.CENTER;
        color = Color.WHITE;
        setPos(x, y);
        setSize(w, h);
    }

    public void setPos(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h){
        this.width = w;
        this.height = h;
    }

    public float getPosX(){
        return this.x;
    }

    public float getPosY(){
        return this.y;
    }

    public float getWidth(){
        return this.width;
    }

    public float getHeight(){
        return this.width;
    }

    public void rotateX(float deg){
        Matrix.setRotateM(this.model.ptr, 0, deg, 1.0f, 0.0f, 0.0f);
    }

    public void rotateY(float deg){
        Matrix.setRotateM(this.model.ptr, 0, deg, 0.0f, 1.0f, 0.0f);
    }

    public void rotateZ(float deg){
        Matrix.setRotateM(this.model.ptr, 0, deg, 0.0f, 0.0f, 1.0f);
    }

    public void rotateAxis(float deg, float x, float y, float z){
        Matrix.setRotateM(this.model.ptr, 0, deg, x, y, z);
    }

    public Origin getOrigin(){
        return origin;
    }

    public Color getColor(){
        return color;
    }

    public void setOrigin(Origin origin){
        this.origin = origin;
    }

    public void setColor(com.com2027.group03.Color color){
        this.color = color;
    }

    public Origin origin;
    public com.com2027.group03.Color color;
    public OpenGLMatrix model;
    public float width, height;
    public float x, y;
}
