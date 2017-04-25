package com.com2027.group03;

/**
 * Created by Matus on 12-Mar-17.
 */
public class Card {
    // Add any data here you want
    // but make sure to edit CardsManager accordingly!
    private String name;
    private int type;

    // Dont remove the uvst!!!!
    private final int u;
    private final int v;
    private final int s;
    private final int t;

    private final Vec2 pos = new Vec2();

    /**
     * The main constructor
     * @param coords Coordinates of the card as an array of 4 ints, use getCardCoord
     *               in CardsManager class
     * @param name Any user defined data... Add more parameters if necessary
     */
    public Card(final int[] coords, int type, String name){
        this.pos.set(coords[0], coords[1]);
        this.u = coords[2];
        this.v = coords[3];
        this.s = coords[4];
        this.t = coords[5];
        this.type = type;
        this.name = name;
    }

    /**
     * @return The integer type of the card
     */
    public int getType() {
        return this.type;
    }

    /**
     * @return The name of the card
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return The position (col/row) of the card
     */
    public Vec2 getPos() {
        return this.pos;
    }

    /**
     * @return The U (pos x) texture coordinate
     */
    public int getU(){
        return this.u;
    }
    /**
     * @return The V (pos y) texture coordinate
     */
    public int getV(){
        return this.v;
    }
    /**
     * @return The S (width) texture coordinate
     */
    public int getS(){
        return this.s;
    }
    /**
     * @return The T (height) texture coordinate
     */
    public int getT(){
        return this.t;
    }
}
