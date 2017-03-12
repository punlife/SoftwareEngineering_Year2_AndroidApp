package com.com2027.group03;

/**
 * Created by Matus on 12-Mar-17.
 */
public class Card {
    // Add any data here you want
    // but make sure to edit CardsManager accordingly!
    private int someData;

    // Dont remove the uvst!!!!
    private final int u;
    private final int v;
    private final int s;
    private final int t;

    public Card(final int[] coords, int someData){
        this.u = coords[0];
        this.v = coords[1];
        this.s = coords[2];
        this.t = coords[3];
        this.someData = someData;
    }

    public int getSomeData(){
        return this.someData;
    }

    public int getU(){
        return this.u;
    }

    public int getV(){
        return this.v;
    }

    public int getS(){
        return this.s;
    }

    public int getT(){
        return this.t;
    }
}
