package com.com2027.group03;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import java.util.TimerTask;
import java.util.Timer;

import java.util.Random;

/**
 * Created by Matus on 12-Mar-17.
 */
public class CardsActivity extends OpenGLActivity {
    private CardsRenderer cardsRenderer;
    private OpenGLTexture backgroundTexture;
    private Sprite backgroundSprite;

    private static final String TAG = "CardsActivity";

    // The padding from the border of the screen
    // In percentages!
    private static final float SCREEN_BORDER_PADDING = 0.05f; // 5%
    private static final float CARD_BORDER_PADDING = 0.01f; // 1%

    @Override
    public void setup(Bundle bundle){
        this.setBackgroundColor(Color.WHITE);

        // Create a new card renderer with a specific image of cards
        cardsRenderer = new CardsRenderer(getBaseContext(), R.drawable.cards_test);

        // Load background texture
        backgroundTexture = new OpenGLTexture();
        try {
            backgroundTexture.load(getBaseContext(), R.drawable.cards_background);
        } catch(OpenGLTexture.LoadException e) {
            Log.e(TAG, e.getMessage());
        }

        // Create background sprite that will always cover entire screen without
        // leaving blank areas.
        int[] coords = ImageScaller.cover(backgroundTexture.getWidth(),
                backgroundTexture.getHeight(), this.getWidth(), this.getHeight());
        backgroundSprite = new Sprite(coords[0], coords[1], coords[2], coords[3], backgroundTexture);
        backgroundSprite.setOrigin(Sprite.Origin.TOP_LEFT);

        // The card grid should be a square (equal sides)
        // However the square should not be bigger than width or height of the screen
        int maxSize = Math.min(this.getWidth(), this.getHeight());

        // This will try to fit a rectangle of size maxSize/maxSize to a screen
        coords = ImageScaller.contain(maxSize, maxSize, this.getWidth(), this.getHeight());

        // This will add a padding from the border of the screen
        coords[0] += maxSize * SCREEN_BORDER_PADDING;
        coords[1] += maxSize * SCREEN_BORDER_PADDING;
        coords[2] -= maxSize * SCREEN_BORDER_PADDING *2;
        coords[3] -= maxSize * SCREEN_BORDER_PADDING *2;

        Log.d(TAG, "Resolution: " + this.getWidth() + "x" + this.getHeight());
        Log.d(TAG, "Coords of the cards grid: " + coords[0] + "x" + coords[1] + " and size: " + coords[2] + "x" + coords[3]);

        // Set renderer properties
        cardsRenderer.setGridPos(coords[0], coords[1]);
        cardsRenderer.setGridSize(coords[2], coords[3]);

        // Do we have saved state?
        if(bundle != null && bundle.containsKey("cols") && bundle.containsKey("rows")){
            Log.i(TAG, "Restoring state...");
            int cols = bundle.getInt("cols");
            int rows = bundle.getInt("rows");

            // State was saved, load previous cards and its state
            int posx[] = bundle.getIntArray("posx");
            int posy[] = bundle.getIntArray("posy");
            boolean visible[] = bundle.getBooleanArray("visible");

            cardsRenderer.setNumOfCards(cols, rows);
            cardsRenderer.setPadding((int)(maxSize * CARD_BORDER_PADDING), (int)(maxSize * CARD_BORDER_PADDING));

            // Populate grid
            for(int y = 0; y < rows; y++){
                for(int x = 0; x < cols; x++){
                    int px = posx[y * cols + x];
                    int py = posy[y * cols + x];

                    if(px < 0 || py < 0)continue;

                    boolean v = visible[y * cols + x];

                    cardsRenderer.addCard(x, y, CardsManager.get(px, py));
                    if(v)cardsRenderer.showCardInstantly(x, y);
                }
            }

        }
        // No saved state, create new grid
        else {
            Log.i(TAG, "Generating new grid...");
            // Creating grid and the cards
            // We will create a set of 6x4 cards
            cardsRenderer.setNumOfCards(6, 4);
            cardsRenderer.setPadding((int)(maxSize * CARD_BORDER_PADDING), (int)(maxSize * CARD_BORDER_PADDING));

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

            cardsRenderer.showAll(true);

            // Hide all after 2 seconds
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "Hiding all cards...");
                    cardsRenderer.hideAll(false);
                }
            }, 2000);
        }
    }

    @Override
    public void render(){
        this.drawSprite(backgroundSprite);
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

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.d(TAG, "Saved state");

        Vec2 size = cardsRenderer.getNumOfCards();

        bundle.putInt("cols", size.x);
        bundle.putInt("rows", size.y);

        // Remember card position and its state
        int posx[] = new int[size.x * size.y];
        int posy[] = new int[size.x * size.y];
        boolean visible[] = new boolean[size.x * size.y];

        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                Card card = cardsRenderer.getCard(x, y);
                if(card == null){
                    posx[y * size.x + x] = -1;
                    posy[y * size.x + x] = -1;
                } else {
                    posx[y * size.x + x] = card.getPos().x;
                    posy[y * size.x + x] = card.getPos().y;
                    visible[y * size.x + x] = cardsRenderer.isVisible(x, y);
                }
            }
        }

        // Put it inside of bundle as 1D arrays
        bundle.putIntArray("posx", posx);
        bundle.putIntArray("posy", posy);
        bundle.putBooleanArray("visible", visible);
    }


}
