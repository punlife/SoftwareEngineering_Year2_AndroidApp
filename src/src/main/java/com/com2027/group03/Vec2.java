package com.com2027.group03;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Vec2 {
    public int x;
    public int y;

    public Vec2(){
        this.x = 0;
        this.y = 0;
    }

    public Vec2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void rotate(float deg){
        int oldx = x;
        double c = Math.cos(deg);
        double s = Math.sin(deg);
        x = (int)(oldx * c - y * s);
        y = (int)(oldx * s + y * c);
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public long lengthSqrd() {
        return (x*x + y*y);
    }
}
