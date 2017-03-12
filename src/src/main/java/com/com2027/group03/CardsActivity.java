package com.com2027.group03;

import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by Matus on 12-Mar-17.
 */
public class CardsActivity extends OpenGLActivity {
    private CardsRenderer cardsRenderer;

    private static final String TAG = "CardsActivity";

    // The padding from the border of the screen
    // In percentages!
    private static final float SCREEN_BORDER_PADDING = 0.05f; // 5%

    @Override
    public void setup(){
        this.setBackgroundColor(Color.WHITE);

        // Create a new card renderer with a specific image of cards
        cardsRenderer = new CardsRenderer(getBaseContext(), R.drawable.cards_test);

        // The card grid should be a square (equal sides)
        // However the square should not be bigger than width or height of the screen
        int maxSize = Math.min(getWidth(), getHeight());

        // This will try to fit a rectangle of size maxSize/maxSize to a screen
        int[] coords = ImageScaller.contain(maxSize, maxSize, getWidth(), getHeight());

        // This will add a padding from the border of the screen
        coords[0] += maxSize * SCREEN_BORDER_PADDING;
        coords[1] += maxSize * SCREEN_BORDER_PADDING;
        coords[2] -= maxSize * SCREEN_BORDER_PADDING *2;
        coords[3] -= maxSize * SCREEN_BORDER_PADDING *2;

        Log.d(TAG, "Resolution: " + getWidth() + "x" + getHeight());
        Log.d(TAG, "Coords of the cards grid: " + coords[0] + "x" + coords[1] + " and size: " + coords[2] + "x" + coords[3]);

        // Set renderer properties
        cardsRenderer.setGridPos(coords[0], coords[1]);
        cardsRenderer.setGridSize(coords[2], coords[3]);

        // Creating grid and the cards
        // We will create a set of 6x4 cards
        cardsRenderer.setNumOfCards(6, 4);
        cardsRenderer.setPadding(0, 0);

        // First row
        cardsRenderer.addCard(0, 0, CardsManager.get(1, 2));
        cardsRenderer.addCard(1, 0, CardsManager.get(5, 0));
        cardsRenderer.addCard(2, 0, CardsManager.get(2, 1));
        cardsRenderer.addCard(3, 0, CardsManager.get(3, 2));
        cardsRenderer.addCard(4, 0, CardsManager.get(5, 1));
        cardsRenderer.addCard(5, 0, CardsManager.get(0, 3));

        // Second row
        cardsRenderer.addCard(0, 1, CardsManager.get(1, 3));
        cardsRenderer.addCard(1, 1, CardsManager.get(5, 2));
        cardsRenderer.addCard(2, 1, CardsManager.get(2, 0));
        cardsRenderer.addCard(3, 1, CardsManager.get(3, 0));
        cardsRenderer.addCard(4, 1, CardsManager.get(5, 1));
        cardsRenderer.addCard(5, 1, CardsManager.get(0, 1));

        // Third row
        cardsRenderer.addCard(0, 2, CardsManager.get(1, 1));
        cardsRenderer.addCard(1, 2, CardsManager.get(5, 1));
        cardsRenderer.addCard(2, 2, CardsManager.get(2, 2));
        cardsRenderer.addCard(3, 2, CardsManager.get(3, 3));
        cardsRenderer.addCard(4, 2, CardsManager.get(5, 1));
        cardsRenderer.addCard(5, 2, CardsManager.get(0, 2));

        // Fourth row
        cardsRenderer.addCard(0, 3, CardsManager.get(1, 0));
        cardsRenderer.addCard(1, 3, CardsManager.get(5, 0));
        cardsRenderer.addCard(2, 3, CardsManager.get(2, 3));
        cardsRenderer.addCard(3, 3, CardsManager.get(3, 1));
        cardsRenderer.addCard(4, 3, CardsManager.get(5, 0));
        cardsRenderer.addCard(5, 3, CardsManager.get(0, 2));
    }

    @Override
    public void render(){
        cardsRenderer.drawCards(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Has the user touched the screen?
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            // Get coordinates of the selected card
            // Will return [-1, -1] if no card selected
            int[] coords = cardsRenderer.getSelected((int) e.getX(), (int) e.getY());
            if (coords[0] >= 0 && coords[1] >= 0) {
                Log.d(TAG, "Selected card col: " + coords[0] + " row: " + coords[1]);

                // Is the card showing? (not showing = back side is visible)
                if(cardsRenderer.isVisible(coords[0], coords[1])){
                    cardsRenderer.hideCard(coords[0], coords[1]);
                } else {
                    cardsRenderer.showCard(coords[0], coords[1]);
                }
            }
        }

        // Always return true because Android whatever.
        return true;
    }
}
