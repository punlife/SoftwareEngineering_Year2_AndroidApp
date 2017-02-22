package com.com2027.group03;

import android.util.Log;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Rectangle extends Shape {


    public Rectangle(){
        super();
        setSize(1, 1);
    }

    public Rectangle(int x, int y, int w, int h){
        super(x, y);
        setSize(w, h);
    }

    public void setSize(int w, int h){
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vec2 mapCoords(int posx, int posy){
        double s = Math.sin(-rot * 0.01745329251);
        double c = Math.cos(-rot * 0.01745329251);
        int mx = (int)((posx - x) * c - (posy - y) * s);
        int my = (int)((posx - x) * s + (posy - y) * c);

        s = Math.sin(rot * 0.01745329251);
        c = Math.cos(rot * 0.01745329251);

        return new Vec2(mx, my);
    }

    public boolean isInside(int posx, int posy){
        double s = Math.sin(-rot * 0.01745329251);
        double c = Math.cos(-rot * 0.01745329251);
        int mx = (int)((posx - x) * c - (posy - y) * s);
        int my = (int)((posx - x) * s + (posy - y) * c);

        switch(origin){
            case TOP_LEFT: {
                break;
            }
            case TOP_CENTER: {
                mx += width/2; break;
            }
            case TOP_RIGHT: {
                mx += width; break;
            }
            case LEFT: {
                my += height/2; break;
            }
            case CENTER: {
                mx += width/2; my += height/2; break;
            }
            case RIGHT: {
                mx += width; my += height/2; break;
            }
            case BOTTOM_LEFT: {
                my += height; break;
            }
            case BOTTOM_CENTER: {
                mx += width/2; my += height; break;
            }
            case BOTTOM_RIGHT: {
                mx += width; my += height; break;
            }
        }

        if(mx > 0 && mx < width && my > 0 && my < height)return true;
        return false;
    }

    public int width;
    public int height;
}
