package com.com2027.group03;

/**
 * This class holds all cards and its data
 * Created by Matus on 12-Mar-17.
 */
public class CardsManager {
    // Modify the following if the texture size will change
    // or if the size of the card changes
    public static final int CARDS_PER_ROW = 8;
    public static final int CARDS_PER_COL = 4;
    public static final int CARD_WIDTH = 256;
    public static final int CARD_HEIGHT = 384;

    // Number of types (chair + sofa = one single type ... sunflower + rose = one single type ...)
    public static final int NUM_OF_CARD_TYPES = 13;

    public static final int[] getCardCoords(int col, int row){
        int[] ret = {col, row, col * CARD_WIDTH, row * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT};
        return ret;
    }

    // This is the array that holds all card data, statically
    // The number '42' is just an example how to add any kind of information to the card
    // See the constructor for the Card class
    public static final Card[] cards = {
        new Card(getCardCoords(0, 0), 0, "Back"), // Card back side - col 0 row 0
        new Card(getCardCoords(1, 0), 1, "Chair"), // Card #1  - col 1 row 0
        new Card(getCardCoords(2, 0), 1, "Sofa"), // Card #2  - col 2 row 0
        new Card(getCardCoords(3, 0), 2, "Rose"), // Card #3  - col 3 row 0
        new Card(getCardCoords(4, 0), 2, "Sunflower"), // Card #4  - col 4 row 0
        new Card(getCardCoords(5, 0), 3, "Modern car"), // Card #5  - col 5 row 0
        new Card(getCardCoords(6, 0), 3, "Old car"), // Card #6  - col 6 row 0
        new Card(getCardCoords(7, 0), 4, "Shell"), // Card #7  - col 7 row 0
        new Card(getCardCoords(0, 1), 4, "Fossil"), // Card #8  - col 0 row 1
        new Card(getCardCoords(1, 1), 5, "Crow"), // Card #9  - col 1 row 1
        new Card(getCardCoords(2, 1), 5, "Seagull"), // Card #10 - col 2 row 1
        new Card(getCardCoords(3, 1), 6, "Green tree"), // Card #11 - col 3 row 1
        new Card(getCardCoords(4, 1), 6, "Yellow tree"), // Card #12 - col 4 row 1
        new Card(getCardCoords(5, 1), 7, "Elder leaf"), // Card #13 - col 5 row 1
        new Card(getCardCoords(6, 1), 7, "Chestnut leaf"), // Card #14 - col 6 row 1
        new Card(getCardCoords(7, 1), 8, "Strawberry"), // Card #15 - col 7 row 1
        new Card(getCardCoords(0, 2), 8, "Peach"), // Card #16 - col 0 row 2
        new Card(getCardCoords(1, 2), 9, "Plant"), // Card #17 - col 1 row 2
        new Card(getCardCoords(2, 2), 9, "Plant"), // Card #18 - col 2 row 2
        new Card(getCardCoords(3, 2), 10, "Snowy peak"), // Card #19 - col 3 row 2
        new Card(getCardCoords(4, 2), 10, "Mountain"), // Card #20 - col 4 row 2
        new Card(getCardCoords(5, 2), 11, "Notebook"), // Card #21 - col 5 row 2
        new Card(getCardCoords(6, 2), 11, "Book"), // Card #22 - col 6 row 2
        new Card(getCardCoords(7, 2), 12, "Spoon"), // Card #23 - col 7 row 2
        new Card(getCardCoords(0, 3), 12, "Fork"), // Card #24 - col 0 row 3
        new Card(getCardCoords(1, 3), 13, "Pencil"), // Card #25 - col 1 row 3
        new Card(getCardCoords(2, 3), 13, "Pen"), // Card #26 - col 2 row 3
        // TODO: Add more pictures/cards
        /*new Card(getCardCoords(3, 3), 0, ""), // Card #27 - col 3 row 3
        new Card(getCardCoords(4, 3), 0, ""), // Card #28 - col 4 row 3
        new Card(getCardCoords(5, 3), 0, ""), // Card #29 - col 5 row 3
        new Card(getCardCoords(6, 3), 0, ""), // Card #30 - col 6 row 3
        new Card(getCardCoords(7, 3), 0, ""), // Card #31 - col 7 row 3
        new Card(getCardCoords(0, 4), 0, ""), // Card #32 - col 0 row 4
        new Card(getCardCoords(1, 4), 0, ""), // Card #33 - col 1 row 4
        new Card(getCardCoords(2, 4), 0, ""), // Card #34 - col 2 row 4
        new Card(getCardCoords(3, 4), 0, ""), // Card #35 - col 3 row 4
        new Card(getCardCoords(4, 4), 0, ""), // Card #36 - col 4 row 3
        new Card(getCardCoords(5, 4), 0, ""), // Card #37 - col 5 row 4
        new Card(getCardCoords(6, 4), 0, ""), // Card #38 - col 6 row 4*/


        // Removed to keep cards to even number
        //new Card(getCardCoords(7, 4), "Empty"), // Card #39 - col 7 row 4
    };

    /**
     * Returns the card at specific column and row
     * @param col The column
     * @param row The row
     * @return Reference to the card if col and row are valid numbers, otherwise NULL.
     */
    public static final Card get(int col, int row){
        if(col >= CARDS_PER_ROW || row >= CARDS_PER_COL ||
                row * CARDS_PER_ROW + col >= cards.length){
            return null;
        }
        return cards[row * CARDS_PER_ROW + col];
    }
}
