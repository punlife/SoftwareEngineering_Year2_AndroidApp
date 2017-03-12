package com.com2027.group03;

/**
 * Created by Matus on 12-Mar-17.
 */
public class CardsManager {
    public static final int CARDS_PER_ROW = 8;
    public static final float CARD_WIDTH_HOMOGEN = 1.0f / (float)CARDS_PER_ROW;
    public static final float CARD_HEIGHT_HOMOGEN = 384.0f / 2048.0f;

    public static float[] getCardUvs(int col, int row){
        float[] ret = {0, 0, CARD_WIDTH_HOMOGEN, CARD_HEIGHT_HOMOGEN};
        ret[0] = col * CARD_WIDTH_HOMOGEN;
        ret[1] = col * CARD_WIDTH_HOMOGEN;
        return ret;
    }
}
