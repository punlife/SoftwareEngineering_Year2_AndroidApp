package com.com2027.group03;

/**
 * Created by Matus on 27-Feb-17.
 */
public class Text extends Shape {
    public Text(){
        super(0, 0, 0, 0);
        this.str = null;
        this.font = null;
    }

    public Text(float x, float y, OpenGLFont font, String str){
        super(x, y, 0, 0);
        setString(str);
        setFont(font);
    }

    public void setString(String str){
        this.str = str;
        final float[] data = {0, 0};
        if(font != null){
            font.getStringSize(str, data);
            setSize(data[0], data[1]);
        }
    }

    public void setFont(OpenGLFont font){
        this.font = font;
        final float[] data = {0, 0};
        if(str != null){
            font.getStringSize(str, data);
            setSize(data[0], data[1]);
        }
    }

    public String getString(){
        return str;
    }

    public OpenGLFont getFont(){
        return font;
    }

    private OpenGLFont font;
    private String str;
}
