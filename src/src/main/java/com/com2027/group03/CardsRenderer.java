package com.com2027.group03;

import android.content.Context;
import android.util.Log;

/**
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

    private static final int STATUS_VISIBLE = 0;
    private static final int STATUS_HIDDEN = 1;
    private static final int STATUS_ROTATING_TO_HIDE_0 = 2;
    private static final int STATUS_ROTATING_TO_HIDE_1 = 3;
    private static final int STATUS_ROTATING_TO_SHOW_0 = 4;
    private static final int STATUS_ROTATING_TO_SHOW_1 = 5;

    private static final float ROTATION_DEG_STEP = 2.0f;
    private static final float ROTATION_SWITCH_FRONT_TO_BACK = 90.0f;
    private static final float ROTATION_AXIS_X = 0.0f;
    private static final float ROTATION_AXIS_Y = 1.0f;
    private static final float ROTATION_AXIS_Z = 0.0f;


    public CardsRenderer(Context context, int id){
        try {
            texture = new OpenGLTexture(context, id);
        } catch (OpenGLTexture.LoadException e){
            throw new RuntimeException(e.getMessage());
        }

        backSide = CardsManager.get(0, 0);
    }

    public void setGridSize(int width, int height){
        size.set(width, height);
    }

    public void setGridPos(int x, int y){
        pos.set(x, y);
    }

    public void setPadding(int x, int y){
        padding.set(x, y);
    }

    public void setNumOfCards(int cols, int rows){
        grid.set(cols, rows);
        cards = new Card[grid.x][grid.y];
        degs = new float[grid.x][grid.y];
        status = new int[grid.x][grid.y];
        sprites = new Sprite[grid.x][grid.y];
    }

    public void addCard(int col, int row, final Card card){
        if(cards != null && col < grid.x && row < grid.y){
            cards[col][row] = card;

            int scalledw = (int)(size.x / (float)grid.x);
            int scalledh = (int)(size.y / (float)grid.y);

            int posx = pos.x + col * scalledw;
            int posy = pos.y + row * scalledh;

            int padx = padding.x * (grid.x -1);
            int pady = padding.y * (grid.y -1);

            scalledw -= padx;
            scalledh -= pady;

            posx += padx / 2;
            posy += pady / 2;

            sprites[col][row] = new Sprite(posx + scalledw/2, posy + scalledh/2, scalledw, scalledh, texture);
            sprites[col][row].setTextureSubsection(backSide.getU(), backSide.getV(), backSide.getS(), backSide.getT());
            degs[col][row] = 0.0f;
            status[col][row] = STATUS_HIDDEN;

            Log.d(TAG, "Adding new card to col/row: " + col + ", " + row + " at pos: "
                    + posx + "x" + posy + " with size: " + scalledw + "x" + scalledh);
        }
    }

    public void drawCards(OpenGLActivity activity){
        if(sprites == null)return;

        for(int y = 0; y < grid.y; y++){
            for(int x = 0; x < grid.x; x++){
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

    public void hideCard(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y){
            status[col][row] = STATUS_ROTATING_TO_HIDE_0;
            degs[col][row] = 0.0f;
        }
    }

    public void showCard(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y){
            status[col][row] = STATUS_ROTATING_TO_SHOW_0;
            degs[col][row] = 0.0f;
        }
    }

    public boolean isVisible(int col, int row){
        if(col >= 0 && col < grid.x && row >= 0 && row < grid.y){
            return status[col][row] == STATUS_VISIBLE;
        }
        return false;
    }

    public final Card getCard(int col, int row){
        return cards[col][row];
    }
}
