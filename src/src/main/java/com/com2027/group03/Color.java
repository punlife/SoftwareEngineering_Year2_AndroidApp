package com.com2027.group03;

/**
 * Created by Matus on 21-Feb-17.
 */
public class Color {
    // These fields are made public because it is easier to access them.
    // There is no reason to make them private as this class does not perform any validation
    // Simply speaking, this class is only used as a data structure
    public float r;
    public float g;
    public float b;
    public float a;

    /**
     * Default constructor, sets the color to white with 100% alpha
     */
    public Color(){
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;
    }

    /**
     * Constructs a color from a shade of gray with 100% alpha
     * @param gray Grey value between 0.0 - 1.0
     */
    public Color(float gray){
        this.r = gray;
        this.g = gray;
        this.b = gray;
        this.a = 1.0f;
    }

    /**
     * Construct a RGB color with 100% alpha
     * @param r Red value between 0.0 - 1.0
     * @param g Green value between 0.0 - 1.0
     * @param b Blue value between 0.0 - 1.0
     */
    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
    }

    /**
     * Construct a RGBA color
     * @param r Red value between 0.0 - 1.0
     * @param g Green value between 0.0 - 1.0
     * @param b Blue value between 0.0 - 1.0
     * @param a Alpha value between 0.0 - 1.0
     */
    public Color(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Creates a new color from a shade of gray with 100% alpha
     * @param gray Grey value between 0 - 255
     * @return New color instance
     */
    static public Color grayscale(int gray){
        return new Color(gray / 255.0f, gray / 255.0f, gray / 255.0f, 1.0f);
    }

    /**
     * Creates a new RGB color with 100% alpha
     * @param r Red value between 0 - 255
     * @param g Green value between 0 - 255
     * @param b Blue value between 0 - 255
     * @return New color instance
     */
    static public Color rgb(int r, int g, int b){
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    /**
     * Creates a new RGB color with 100% alpha
     * @param r Red value between 0 - 255
     * @param g Green value between 0 - 255
     * @param b Blue value between 0 - 255
     * @param a Alpha value between 0 - 255
     * @return New color instance
     */
    static public Color rgba(int r, int g, int b, int a){
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    /**
     * Creates a new color from hex value with 100% alpha
     * @param hex Hex value, example: 0xFF0080 = 100% red + 50% blue
     * @return New color instance
     */
    static public Color rgb(long hex){
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
