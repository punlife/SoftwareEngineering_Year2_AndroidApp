package com.com2027.group03;

/**
 * Created by Matus on 26-Feb-17.
 */
public class TestOpenGLFont extends OpenGLActivity {
    private OpenGLFont font;
    private Text[] texts;
    private float degrees;

    @Override
    public void setup() {
        font = new OpenGLFont();
        font.create("fonts/FreeSans.ttf", getBaseContext(), 11);

        float posx = this.getWidth()/2;
        float posy = this.getHeight()/2;

        // some changes

        texts = new Text[5];

        texts[0] = new Text(posx, posy, font, "Hello World!");
        texts[0].setOrigin(Shape.Origin.BOTTOM_RIGHT);
        texts[0].setColor(Color.YELLOW);
        texts[0].rotateZ(15.0f);

        texts[1] = new Text(posx, posy, font, "Hello World!");
        texts[1].setOrigin(Shape.Origin.BOTTOM_LEFT);
        texts[1].setColor(Color.CYAN);
        texts[1].rotateZ(-15.0f);

        texts[2] = new Text(posx, posy, font, "Hello World!");
        texts[2].setOrigin(Shape.Origin.TOP_LEFT);
        texts[2].setColor(Color.MAGENTA);
        texts[2].rotateZ(15.0f);

        texts[3] = new Text(posx, posy, font, "Hello World!");
        texts[3].setOrigin(Shape.Origin.TOP_RIGHT);
        texts[3].setColor(Color.GRAY);
        texts[3].rotateZ(-15.0f);

        texts[4] = new Text(posx/2, posy, font, "Rotating\nText");
        texts[4].setOrigin(Shape.Origin.CENTER);
        texts[4].setColor(Color.RED);
    }

    @Override
    public void render() {
        for(int i = 0; i < 5; i++){
            drawText(texts[i]);
        }

        degrees += 1.0f;
        if(degrees > 360.0f)degrees -= 360.0f;
        texts[4].rotateZ(degrees);
    }
}
