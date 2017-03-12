package com.com2027.group03;

/**
 * Created by Matus on 12-Mar-17.
 */
public class CardsManager {
    // Modify the following if the texture size will change
    // or if the size of the card changes
    public static final int CARDS_PER_ROW = 8;
    public static final int CARDS_PER_COL = 4;
    public static final int CARD_WIDTH = 256;
    public static final int CARD_HEIGHT = 384;

    public static final int[] getCardCoords(int col, int row){
        int[] ret = {col * CARD_WIDTH, row * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT};
        return ret;
    }

    // This is the array that holds all card data, statically
    private static final Card[] cards = {
        new Card(getCardCoords(0, 0), 42), // Card back side - col 0 row 0
        new Card(getCardCoords(1, 0), 42), // Card #1  - col 1 row 0
        new Card(getCardCoords(2, 0), 42), // Card #2  - col 2 row 0
        new Card(getCardCoords(3, 0), 42), // Card #3  - col 3 row 0
        new Card(getCardCoords(4, 0), 42), // Card #4  - col 4 row 0
        new Card(getCardCoords(5, 0), 42), // Card #5  - col 5 row 0
        new Card(getCardCoords(6, 0), 42), // Card #6  - col 6 row 0
        new Card(getCardCoords(7, 0), 42), // Card #7  - col 7 row 0
        new Card(getCardCoords(0, 1), 42), // Card #8  - col 0 row 1
        new Card(getCardCoords(1, 1), 42), // Card #9  - col 1 row 1
        new Card(getCardCoords(2, 1), 42), // Card #10 - col 2 row 1
        new Card(getCardCoords(3, 1), 42), // Card #11 - col 3 row 1
        new Card(getCardCoords(4, 1), 42), // Card #12 - col 4 row 1
        new Card(getCardCoords(5, 1), 42), // Card #13 - col 5 row 1
        new Card(getCardCoords(6, 1), 42), // Card #14 - col 6 row 1
        new Card(getCardCoords(7, 1), 42), // Card #15 - col 7 row 1
        new Card(getCardCoords(0, 2), 42), // Card #16 - col 0 row 2
        new Card(getCardCoords(1, 2), 42), // Card #17 - col 1 row 2
        new Card(getCardCoords(2, 2), 42), // Card #18 - col 2 row 2
        new Card(getCardCoords(3, 2), 42), // Card #19 - col 3 row 2
        new Card(getCardCoords(4, 2), 42), // Card #20 - col 4 row 2
        new Card(getCardCoords(5, 2), 42), // Card #21 - col 5 row 2
        new Card(getCardCoords(6, 2), 42), // Card #22 - col 6 row 2
        new Card(getCardCoords(7, 2), 42), // Card #23 - col 7 row 2
        new Card(getCardCoords(0, 3), 42), // Card #24 - col 0 row 3
        new Card(getCardCoords(1, 3), 42), // Card #25 - col 1 row 3
        new Card(getCardCoords(2, 3), 42), // Card #26 - col 2 row 3
        new Card(getCardCoords(3, 3), 42), // Card #27 - col 3 row 3
        new Card(getCardCoords(4, 3), 42), // Card #28 - col 4 row 3
        new Card(getCardCoords(5, 3), 42), // Card #29 - col 5 row 3
        new Card(getCardCoords(6, 3), 42), // Card #30 - col 6 row 3
        new Card(getCardCoords(7, 3), 42), // Card #31 - col 7 row 3
        new Card(getCardCoords(0, 4), 42), // Card #32 - col 0 row 4
        new Card(getCardCoords(1, 4), 42), // Card #33 - col 1 row 4
        new Card(getCardCoords(2, 4), 42), // Card #34 - col 2 row 4
        new Card(getCardCoords(3, 4), 42), // Card #35 - col 3 row 4
        new Card(getCardCoords(4, 4), 42), // Card #36 - col 4 row 3
        new Card(getCardCoords(5, 4), 42), // Card #37 - col 5 row 4
        new Card(getCardCoords(6, 4), 42), // Card #38 - col 6 row 4
        new Card(getCardCoords(7, 4), 42), // Card #39 - col 7 row 4
    };

    public static final Card get(int col, int row){
        if(col >= CARDS_PER_ROW || row >= CARDS_PER_COL){
            return null;
        }
        return cards[row * CARDS_PER_ROW + col];
    }
}
