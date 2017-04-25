package com.com2027.group03;

import android.content.Context;
import android.util.Log;

/**
 * This class is used to render cards on the screen via OpenGLActivity
 * Created by Matus on 12-Mar-17.
 */
public class CardsRenderer {
    private OpenGLTexture texture;
    private String TAG = "CardsRenderer";

    private Vec2 pos = new Vec2();
    private Vec2 size = new Vec2();
    private Vec2 grid = new Vec2();
    private Vec2 padding = new Vec2();

    private Card[][] cards = null;
    private float[][] degs = null;
    private int[][] status = null;
    private Sprite[][] sprites = null;
    private Card backSide = null;

    // Status flags, don't change any values
    private static final int STATUS_VISIBLE = 0;
    private static final int STATUS_HIDDEN = 1;
    private static final int STATUS_ROTATING_TO_HIDE_0 = 2;
    private static final int STATUS_ROTATING_TO_HIDE_1 = 3;
    private static final int STATUS_ROTATING_TO_SHOW_0 = 4;
    private static final int STATUS_ROTATING_TO_SHOW_1 = 5;

    // The amount of degrees to rotate card per frame
    private static final float ROTATION_DEG_STEP = 4.0f;
    // Rotation axis of the card
    // X is horizontal
    // Y is top
    // Z is front (pointing away from screen)
    private static final float ROTATION_AXIS_X = 0.0f;
    private static final float ROTATION_AXIS_Y = 1.0f;
    private static final float ROTATION_AXIS_Z = 0.0f;

    /**
     * The main constructor
     * @param context The context of the OpenGL activity class
     * @param id The ID of the texture file (example: R.drawable.texture_name)
     */
    public CardsRenderer(Context context, int id){
        try {
            texture = new OpenGLTexture(context, id);
        } catch (OpenGLTexture.LoadException e){
            throw new RuntimeException(e.getMessage());
        }

        backSide = CardsManager.get(0, 0);
    }

    /**
     * Sets the grid size in pixels
     * @param width Width of the grid
     * @param height height of the grid
     */
    public void setGridSize(int width, int height){
        size.set(width, height);
    }

    /**
     * Sets the grid left corner position in pixels
     * @param x Top left corner X position
     * @param y Top left corner Y position
     */
    public void setGridPos(int x, int y){
        pos.set(x, y);
    }

    /**
     * Sets the padding for all cards in pixels
     * @param x Padding for X
     * @param y Padding for Y
     */
    public void setPadding(int x, int y){
        padding.set(x, y);
    }

    /**
     * Sets the number of cards
     * @param cols Number of columns
     * @param rows Number of rows
     */
    public void setNumOfCards(int cols, int rows){
        grid.set(cols, rows);
        cards = new Card[grid.x][grid.y];
        degs = new float[grid.x][grid.y];
        status = new int[grid.x][grid.y];
        sprites = new Sprite[grid.x][grid.y];
    }

    /**
     * @return Number of cards that the grid can hold
     */
    public Vec2 getNumOfCards() {
        return grid;
    }

    /**
     * Adds a card to specific column and row
     * @param col Column position
     * @param row Row position
     * @param card An instance of the card
     */
    public void addCard(int col, int row, final Card card){
        if(cards != null && col < grid.x && row < grid.y){
            cards[col][row] = card;

            int scalledw = (int)(size.x / (float)grid.x);
            int scalledh = (int)(size.y / (float)grid.y);

            int posx = pos.x + col * scalledw;
            int posy = pos.y + row * scalledh;

            scalledw -= padding.x;
            scalledh -= padding.y;

            posx += padding.x / 2;
            posy += padding.y / 2;

            sprites[col][row] = new Sprite(posx + scalledw/2, posy + scalledh/2, scalledw, scalledh, texture);
            sprites[col][row].setTextureSubsection(backSide.getU(), backSide.getV(), backSide.getS(), backSide.getT());
            degs[col][row] = 0.0f;
            status[col][row] = STATUS_HIDDEN;

            Log.d(TAG, "Adding new card to col/row: " + col + ", " + row + " at pos: "
                    + posx + "x" + posy + " with size: " + scalledw + "x" + scalledh);
        }
    }

    /**
     * Draws cards on the screen
     * @param activity Instance of the OpenGLActivity activity
     */
    public void drawCards(OpenGLActivity activity){
        if(sprites == null)return;

        for(int y = 0; y < grid.y; y++){
            for(int x = 0; x < grid.x; x++){
                if(cards[x][y] == null)continue;

                Sprite spr = sprites[x][y];

                activity.drawSprite(spr);

                if(status[x][y] == STATUS_ROTATING_TO_SHOW_0){
                    degs[x][y] += ROTATION_DEG_STEP;

                    if(degs[x][y] >= 90.0f){
                        status[x][y] = STATUS_ROTATING_TO_SHOW_1;
                        degs[x][y] = 90.0f;

                        final Card card = cards[x][y];
                        spr.setTextureSubsection(card.getU(), card.getV(), card.getS(), card.getT());
                    }
                    spr.rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                }

                else if(status[x][y] == STATUS_ROTATING_TO_SHOW_1){
                    degs[x][y] -= ROTATION_DEG_STEP;

                    if(degs[x][y] <= 0.0f){
                        degs[x][y] = 0.0f;
                        status[x][y] = STATUS_VISIBLE;
                    }
                    spr.rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                }

                else if(status[x][y] == STATUS_ROTATING_TO_HIDE_0) {
                    degs[x][y] += ROTATION_DEG_STEP;

                    if(degs[x][y] >= 90.0f){
                        status[x][y] = STATUS_ROTATING_TO_HIDE_1;
                        degs[x][y] = 90.0f;

                        spr.setTextureSubsection(backSide.getU(), backSide.getV(), backSide.getS(), backSide.getT());
                    }
                    spr.rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                }

                else if(status[x][y] == STATUS_ROTATING_TO_HIDE_1){
                    degs[x][y] -= ROTATION_DEG_STEP;

                    if(degs[x][y] <= 0.0f){
                        status[x][y] = STATUS_HIDDEN;
                        degs[x][y] = 0.0f;
                    }
                    spr.rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                }
            }
        }
    }

    /**
     * Checks if the card is selected
     * @param x Touch position X in pixels
     * @param y Touch position Y in pixels
     * @return Col/row coordinates of the card
     */
    public final int[] getSelected(int x, int y){
        int[] ret = {-1, -1};
        if(x >= pos.x && x <= pos.x + size.x &&
                y >= pos.y && y <= pos.y + size.y){
            x = x - pos.x;
            y = y - pos.y;

            ret[0] = (int)((x / (float)size.x) * grid.x);
            ret[1] = (int)((y / (float)size.y) * grid.y);
        }
        return ret;
    }

    /**
     * Shows all cards
     * @param instant Perform rotation instantly when true
     */
    public void showAll(boolean instant){
        for(int y = 0; y < grid.y; y++){
            for(int x = 0; x < grid.x; x++){
                if(cards[x][y] == null)continue;

                if(instant){
                    status[x][y] = STATUS_VISIBLE;
                    final Card card = cards[x][y];
                    sprites[x][y].setTextureSubsection(card.getU(), card.getV(), card.getS(), card.getT());
                    sprites[x][y].rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                } else {
                    if (status[x][y] != STATUS_HIDDEN) {
                        continue;
                    }

                    status[x][y] = STATUS_ROTATING_TO_SHOW_0;
                    degs[x][y] = 0.0f;
                }
            }
        }
    }

    /**
     * Hides all cards
     * @param instant Perform rotation instantly when true
     */
    public void hideAll(boolean instant){
        for(int y = 0; y < grid.y; y++){
            for(int x = 0; x < grid.x; x++){
                if(cards[x][y] == null)continue;

                if(instant){
                    status[x][y] = STATUS_HIDDEN;
                    sprites[x][y].setTextureSubsection(backSide.getU(), backSide.getV(), backSide.getS(), backSide.getT());
                    sprites[x][y].rotateAxis(degs[x][y], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
                } else {
                    if (status[x][y] != STATUS_VISIBLE) {
                        continue;
                    }

                    status[x][y] = STATUS_ROTATING_TO_HIDE_0;
                    degs[x][y] = 0.0f;
                }
            }
        }
    }

    /**
     * Hides a card at col/row position with animation
     * @param col Column
     * @param row Row
     */
    public void hideCard(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y
                && cards[col][row] != null && status[col][row] == STATUS_VISIBLE){
            status[col][row] = STATUS_ROTATING_TO_HIDE_0;
            degs[col][row] = 0.0f;
        }
    }

    /**
     * Hides a card at col/row position
     * @param col
     * @param row
     */
    public void hideCardInstantly(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y && cards[col][row] != null){
            status[col][row] = STATUS_HIDDEN;
            degs[col][row] = 0.0f;
            sprites[col][row].setTextureSubsection(backSide.getU(), backSide.getV(), backSide.getS(), backSide.getT());
            sprites[col][row].rotateAxis(degs[col][row], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
        }
    }

    /**
     * Shows a card at col/row position with animation
     * @param col Column
     * @param row Row
     */
    public void showCard(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y
                && cards[col][row] != null && status[col][row] == STATUS_HIDDEN){
            status[col][row] = STATUS_ROTATING_TO_SHOW_0;
            degs[col][row] = 0.0f;
        }
    }

    /**
     * Shows a card at col/row position
     * @param col Column
     * @param row Row
     */
    public void showCardInstantly(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y && cards[col][row] != null){
            status[col][row] = STATUS_VISIBLE;
            degs[col][row] = 0.0f;
            final Card card = cards[col][row];
            sprites[col][row].setTextureSubsection(card.getU(), card.getV(), card.getS(), card.getT());
            sprites[col][row].rotateAxis(degs[col][row], ROTATION_AXIS_X, ROTATION_AXIS_Y, ROTATION_AXIS_Z);
        }
    }

    /**
     * Checks if a card is visible
     * @param col Column
     * @param row Row
     * @return True if card is visible
     */
    public boolean isVisible(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y && cards[col][row] != null){
            return status[col][row] == STATUS_VISIBLE ||
                    status[col][row] == STATUS_ROTATING_TO_SHOW_0 ||
                    status[col][row] == STATUS_ROTATING_TO_SHOW_1;
        }
        return false;
    }

    /**
     * @param col Column pos
     * @param row Row pos
     * @return A reference to the card at position col/row
     */
    public final Card getCard(int col, int row){
        return cards[col][row];
    }
}
