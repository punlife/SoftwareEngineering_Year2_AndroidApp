package com.com2027.group03;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    public Color(){
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;
    }

    public Color(float gray){
        this.r = gray;
        this.g = gray;
        this.b = gray;
        this.a = 1.0f;
    }

    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
    }

    public Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    static public Color grayscale(int gray){
        return new Color(gray / 255.0f, gray / 255.0f, gray / 255.0f, 1.0f);
    }

    static public Color rgb(int r, int g, int b){
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    static public Color rgba(int r, int g, int b, int a){
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    static public Color grayscale(long hex){
        return new Color(
                (hex & 0xFF0000) / 255.0f,
                (hex & 0xFF00) / 255.0f,
                (hex & 0xFF) / 255.0f, 1.0f);
    }

    static public Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    static public Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    static public Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    static public Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    static public Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    static public Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    static public Color YELLOW = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    static public Color CYAN = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    static public Color MAGENTA = new Color(1.0f, 0.0f, 1.0f, 1.0f);
}
