package com.com2027.group03;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Matus on 12-Mar-17.
 * Updated by Lukas on 14-May-17.
 */
public class CardsActivity extends OpenGLActivity {
    private CardsRenderer cardsRenderer;
    private OpenGLTexture backgroundTexture;
    private OpenGLTexture numbersTexture;
    private OpenGLTexture goTexture;
    private OpenGLTexture buttonRepeatTexture;
    private OpenGLTexture buttonNextTexture;
    private OpenGLTexture opacity50Texture;
    private OpenGLFont scoreFont;
    private Sprite backgroundSprite;
    private Sprite numbersSprite;
    private Sprite goSprite;
    private Text scoreText;
    private Sprite scoreTextBackground;
    private Sprite buttonRepeat;
    private Sprite buttonNext;

    private static class PickedCard {
        public Card card = null;
        public int x = 0;
        public int y = 0;
        public PickedCard(Card card, int x, int y){
            this.card = card;
            this.x = x;
            this.y = y;
        }
    };

    private List<PickedCard> pickedCards = new ArrayList<PickedCard>();
    private AtomicBoolean disableTouch = new AtomicBoolean(false);
    private long elapsedTimeStart = 0;
    private long elapsedTimeShown = 0;
    private int startDelay = 0;
    private AtomicBoolean showTimer = new AtomicBoolean(false);
    private static final String TAG = "CardsActivity";
    private long startTime = System.currentTimeMillis();
    private int difficulty = 0;
    private int cardMatchCounter = 0;
    private List<String> pickedCardTypes = new ArrayList<String>();
    private List<Boolean> nbackAnswers = new ArrayList<Boolean>();
    private final String[] cardTypes = {"Chair","Sofa", "Rose","Sunflower", "Modern car", "Old car", "Shell", "Fossil", "Crow", "Seagull", "Green tree", "Yellow tree", "Elder leaf", "Chestnut leaf",
            "Strawberry", "Peach", "Plant", "Plant", "Snowy peak", "Mountain", "Notebook", "Book", "Spoon", "Fork", "Pencil", "Pen"};


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
        numbersTexture = new OpenGLTexture();
        goTexture = new OpenGLTexture();
        buttonRepeatTexture = new OpenGLTexture();
        buttonNextTexture = new OpenGLTexture();
        opacity50Texture = new OpenGLTexture();
        scoreFont = new OpenGLFont();

        try {
            backgroundTexture.load(getBaseContext(), R.drawable.gamebackground);
            numbersTexture.load(getBaseContext(), R.drawable.numbers);
            goTexture.load(getBaseContext(), R.drawable.go);
            buttonRepeatTexture.load(getBaseContext(), R.drawable.button_repeat);
            buttonNextTexture.load(getBaseContext(), R.drawable.button_next);
            opacity50Texture.load(getBaseContext(), R.drawable.opacity_50);
        } catch(OpenGLTexture.LoadException e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            scoreFont.create("fonts/FreeSans.ttf", getBaseContext(), 18);
        } catch(OpenGLFont.LoadException e) {
            Log.e(TAG, e.getMessage());
        }

        // Create background sprite that will always cover entire screen without
        // leaving blank areas.
        int[] coords = ImageScaller.cover(backgroundTexture.getWidth(),
                backgroundTexture.getHeight(), this.getWidth(), this.getHeight());
        backgroundSprite = new Sprite(coords[0], coords[1], coords[2], coords[3], backgroundTexture);
        backgroundSprite.setOrigin(Sprite.Origin.TOP_LEFT);

        int goWidth = (int)(Math.min(this.getWidth(), this.getHeight()) * 0.5);
        goSprite = new Sprite(this.getWidth()/2, this.getHeight()/2, goWidth, (int)(goWidth * 0.5), goTexture);
        int numWidth = (int)(Math.min(this.getWidth(), this.getHeight()) * 0.3);
        numbersSprite = new Sprite(this.getWidth()/2, this.getHeight()/2, numWidth, numWidth, numbersTexture);

        // Each button is 33% of the screen width
        int buttonWidth = (int)(this.getWidth() * 0.3333333f);
        // Each button is 10% from the bottom
        int buttonBottom = (int)(this.getHeight() - this.getHeight() * 0.1f);
        int buttonMid = (int)(this.getWidth() * 0.5f);
        // Each button's width is 3.877 times bigger than its height
        buttonRepeat = new Sprite(buttonMid - ((buttonWidth/2.0f) * 1.1f), buttonBottom,
                buttonWidth, buttonWidth / 3.877f, buttonRepeatTexture);
        buttonNext = new Sprite(buttonMid + ((buttonWidth/2.0f) * 1.1f), buttonBottom,
                buttonWidth, buttonWidth / 3.877f, buttonNextTexture);

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
            //boolean visible[] = bundle.getBooleanArray("visible");

            cardsRenderer.setNumOfCards(cols, rows);
            cardsRenderer.setPadding((int)(maxSize * CARD_BORDER_PADDING), (int)(maxSize * CARD_BORDER_PADDING));

            // Populate grid
            for(int y = 0; y < rows; y++){
                for(int x = 0; x < cols; x++){
                    int px = posx[y * cols + x];
                    int py = posy[y * cols + x];

                    if(px < 0 || py < 0)continue;

                    //boolean v = visible[y * cols + x];

                    cardsRenderer.addCard(x, y, CardsManager.get(px, py));
                    //(v)cardsRenderer.showCardInstantly(x, y);
                }
            }

        }
        // No saved state, create new grid
        else {
            Intent intent = getIntent();
            if(!intent.hasExtra("cardsPerRow")){
                throw new RuntimeException(
                        "Cannot construct cards grid as I am " +
                                "missing \"cardsPerRow\" value in the intent!");
            }

            constructLevel(
                    intent.getIntExtra("cardsPerRow", 3),
                    intent.getIntExtra("initialShowDelay", 2000)
            );
        }
    }

    private void constructLevel(int cardsPerRow, int initialShowDelay){
        Log.i(TAG, "Generating new grid...");

        scoreText = null;

        // The card grid should be a square (equal sides)
        // However the square should not be bigger than width or height of the screen
        int maxSize = Math.min(this.getWidth(), this.getHeight());

        // The number of cards per row has to be defined manually
        int cols = cardsPerRow;
        // Because each card has 2:3 ratio (256x384) the total number of
        // rows has to be 2/3 of the number of column
        int rows = (int)Math.ceil(cols * 0.666666);

        // Creating grid and the cards
        // We will create a set of 6x4 cards
        cardsRenderer.setNumOfCards(cols, rows);
        cardsRenderer.setPadding((int)(maxSize * CARD_BORDER_PADDING), (int)(maxSize * CARD_BORDER_PADDING));

        int total = cols * rows;
        boolean middleMissing = false;

        // If the total number of cards is odd, remove one card from the middle
        if(total % 2 == 1){
            total--;
            middleMissing = true;
        }

        // Get total number of available cards. The first card is a back side, ignore that
        int totalAvailable = CardsManager.cards.length -1;
        List<Integer> listOfCards = new ArrayList<Integer>();

        Log.i(TAG, "Building card grid...");

        for(int y = 0; y < rows; y++){
            for(int x = 0; x < cols; x++){
                // Ignore middle card?
                if(x == y && middleMissing)continue;

                // Check if we have already added a card here
                if(cardsRenderer.getCard(x, y) != null)continue;

                // Get random number
                int index = -1;
                Card card = null;
                while(true) {
                    index = new Random().nextInt(totalAvailable) + 1;
                    card = CardsManager.cards[index];

                    // Check if this type of card is already in the grid
                    // If yes, pick one randomly again
                    if(listOfCards.contains(index))continue;
                    break;
                }

                Log.i(TAG, "Adding card to " + x + ", " + y);
                cardsRenderer.addCard(x, y, card);

                listOfCards.add(index);

                // Get a random next available space and put the same card
                // over there to create a pair
                // Best case scenario complexity O(1)
                // Worst case scenario complexity O(infinity)
                while(true){

                    int row = new Random().nextInt(rows);
                    int col = new Random().nextInt(cols);
                    //Log.i(TAG, "Testing second place for card to " + col + ", " + row);

                    // Is the position available?
                    if(cardsRenderer.getCard(col, row) == null){
                        cardsRenderer.addCard(col, row, card);
                        break;
                    }
                }
            }
        }

        cardsRenderer.showAll(true);

        startDelay = initialShowDelay;
        elapsedTimeStart = System.currentTimeMillis();

        // Hide all after 2 seconds
        disableTouch.set(true);
        showTimer.set(true);

        Log.d(TAG, "Hiding all cards in " + startDelay + " ms");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Hiding all cards...");
                cardsRenderer.hideAll(false);
                disableTouch.set(false);
                showTimer.set(false);
                elapsedTimeShown = System.currentTimeMillis();
            }
        }, startDelay + 1000);
    }

    @Override
    public void render(){
        this.drawSprite(backgroundSprite);

        // Are there any cards to render?
        if(cardsRenderer.getTotalCardsLeft() > 0) {
            cardsRenderer.drawCards(this);

            // Should we show the initial timer?
            if (showTimer.get()) {
                // Get the current time
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - elapsedTimeStart;

                // Get the number of seconds left until the cards are hidden
                int deltaSeconds = (int) ((startDelay / 1000) - (deltaTime / 1000));
                float frac = deltaSeconds - ((startDelay / 1000.0f) - (deltaTime / 1000.0f));

                // Render the "GO!" texture, otherwise render the numbers...
                if (deltaSeconds == 0) {
                    this.drawSprite(goSprite);
                } else {
                    numbersSprite.scale(1.0f - frac, 1.0f - frac, 1.0f);
                    numbersSprite.setTextureSubsection(deltaSeconds * 128, 0, 128, 128);
                    this.drawSprite(numbersSprite);
                }
            }
        }
        // No cards to render, display score
        else {
            if(scoreText == null){
                long totalTimeTook = System.currentTimeMillis() - elapsedTimeShown;
                int seconds = (int)(totalTimeTook / 1000);

                int stage = cardsRenderer.getNumOfCards().x;
                int stageCounter = (cardsRenderer.getNumOfCards().x+cardsRenderer.getNumOfCards().y)/2;
                int answerCounter=0;
                int answerMultiplier=1;
                int answerScore;
                for (int i = 0;i<nbackAnswers.size();i++){
                    if(nbackAnswers.get(i) == true){
                        answerCounter+=1;
                        if (i+1 < nbackAnswers.size()){
                            if (nbackAnswers.get(i+1) == true){
                                answerMultiplier +=1;
                            }
                        }
                    }
                }
                if (difficulty == 0){
                    difficulty = 1;
                }
                answerScore = ((answerCounter*10)*answerMultiplier)*difficulty;

                int score = Math.max((difficulty * 10) * (stage * stageCounter * 2) - (seconds / 2) + answerScore, 0);

                String filename = "highscores";
                String string = Constants.NICKNAME+","+score+ System.lineSeparator();

                File file = new File(getFilesDir(),filename);


                if(file.exists()){
                    try {
                        FileOutputStream fos=new FileOutputStream(file,true);
                        fos.write(string.getBytes());
                        Log.i("File:",file.toString());
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        FileOutputStream outputStream;
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(string.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }





                Log.d(TAG, "Total time took: " + seconds + " seconds!");
                scoreText = new Text(
                        this.getWidth()/2, this.getHeight()/2,
                        scoreFont, "Total score: " + score
                        );

                scoreTextBackground = new Sprite(
                        this.getWidth()/2, this.getHeight()/2,
                        scoreText.getWidth() * 1.1f, scoreText.getHeight() * 1.1f,
                        opacity50Texture
                );
            }
            this.drawSprite(scoreTextBackground);
            this.drawText(scoreText);
            this.drawSprite(buttonRepeat);
            this.drawSprite(buttonNext);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(disableTouch.get()){
            Log.d(TAG, "Touch disabled until cards are hidden!");
            return true;
        }

        // Has the user touched the screen?
        if(e.getAction() == MotionEvent.ACTION_DOWN) {

            // Any cards to render?
            if(cardsRenderer.getTotalCardsLeft() > 0) {
                // Get coordinates of the selected card
                // Will return [-1, -1] if no card selected
                int[] coords = cardsRenderer.getSelected((int) e.getX(), (int) e.getY());
                if (coords[0] >= 0 && coords[1] >= 0) {

                    // Is the card missing?
                    Card card = cardsRenderer.getCard(coords[0], coords[1]);
                    if (card == null) return true;

                    Log.d(TAG, "Selected card col: " + coords[0] + " row: " + coords[1]);

                    if (cardsRenderer.isVisible(coords[0], coords[1])) {
                        cardsRenderer.hideCard(coords[0], coords[1]);

                        // Find card in the array and remove it
                        for (PickedCard c : pickedCards) {
                            if (c.x == coords[0] && c.y == coords[1]) {
                                pickedCards.remove(c);
                                break;
                            }
                        }
                    } else {
                        cardsRenderer.showCard(coords[0], coords[1]);

                        // Check if we have selected the same card on the same position
                        if (pickedCards.size() > 0 &&
                                pickedCards.get(0).x == coords[0] &&
                                pickedCards.get(0).y == coords[1]) {
                            Log.d(TAG, "Selected same position... ignoring...");
                        } else {
                            pickedCards.add(new PickedCard(card, coords[0], coords[1]));
                        }
                    }

                    Log.d(TAG, "Array debug: " + pickedCards.toString());

                    // Apply rules
                    if (pickedCards.size() == 2) {
                        final PickedCard a = pickedCards.get(0);
                        PickedCard b = pickedCards.get(1);

                        // Are they same cards?
                        if (a.card == b.card) {
                            cardMatchCounter += 1;
                            Log.d(TAG, "Same cards!");
                            disableTouch.set(true);
                            pickedCardTypes.add(a.card.getName());

                            Log.d(TAG, "PickedCardTypes"+ pickedCardTypes.size());
                            Log.d(TAG, "cardMatchCounter"+ cardMatchCounter);
                            if (difficulty != 0 && difficulty <= cardMatchCounter ) {
                                if ((pickedCardTypes.size() - difficulty) >= 0) {
                                    final Dialog dialog = new Dialog(CardsActivity.this);
                                    dialog.setContentView(R.layout.nback_dialog);
                                    dialog.setTitle("Pick a card");
                                    dialog.setCancelable(false);
                                    final TextView tv1 = (TextView) dialog.findViewById(R.id.nbacktext);
                                    final RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.rd_1);
                                    final RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.rd_2);
                                    final RadioButton rd3 = (RadioButton) dialog.findViewById(R.id.rd_3);
                                    final Button b1 = (Button) dialog.findViewById(R.id.dialogbutton);
                                    final RadioButton[] rBs = {rd1,rd2,rd3};
                                    String[] answers = answerShifter(nBack());
                                    rd1.setText(answers[0]);
                                    rd2.setText(answers[1]);
                                    rd3.setText(answers[2]);
                                    tv1.setText("Choose a card you matched " + difficulty + " turns ago:");
                                    b1.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            Boolean buttonStatus = false;
                                            for(int i = 0; i < rBs.length;i++){
                                                if (rBs[i].isChecked() && rBs[i].getText().equals(pickedCardTypes.get(pickedCardTypes.size() - difficulty)))
                                                {
                                                    Log.d("tag","Correct");
                                                    buttonStatus = true;
                                                    nbackAnswers.add(true);


                                                }
                                                else if (rBs[i].isChecked() && !(rBs[i].getText().equals(pickedCardTypes.get(pickedCardTypes.size() - difficulty)))){
                                                    Log.d("tag","Incorrect");
                                                    buttonStatus = false;
                                                    nbackAnswers.add(false);
                                                }
                                                else {
                                                    Log.d("tag","Not picked");
                                                }
                                            }
                                            dialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CardsActivity.this);//Context parameter
                                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //do nothing
                                                }
                                            });
                                            if (buttonStatus == true){
                                                builder.setTitle("Correct!");
                                            }
                                            else {
                                                builder.setTitle("Incorrect!");
                                            }
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    });

                                    dialog.show();
                                    cardMatchCounter = 0;
                                }
                            }

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Removing selected cards...");
                                    PickedCard sa = pickedCards.get(0);
                                    PickedCard sb = pickedCards.get(1);
                                    Log.d(TAG, "Test"+ pickedCardTypes.size());
                                    cardsRenderer.removeCard(sa.x, sa.y);
                                    cardsRenderer.removeCard(sb.x, sb.y);
                                    disableTouch.set(false);
                                    pickedCards.clear();

                                }
                            }, 1000);


                        } else {
                            Log.d(TAG, "Not the same cards!");
                            disableTouch.set(true);

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Hiding all cards...");
                                    cardsRenderer.hideAll(false);
                                    disableTouch.set(false);
                                    pickedCards.clear();
                                }
                            }, 1000);
                        }

                        //pickedCards.clear();
                    }
                }
            }
            // No cards to render, check if buttons have been pressed
            else {
                int x = (int)e.getX();
                int y = (int)e.getY();
                if(buttonNext.isTouched(x, y)){

                    long endTime = System.currentTimeMillis();


                    switch (difficulty){
                        case 0:
                            if (true){
                                difficultyIncrease();
                            }
                            break;
                        case 1:
                            if (true){
                                difficultyIncrease();
                            }
                            break;
                        case 2:
                            if (difficultyCheck(endTime)){
                                difficultyIncrease();
                            }
                            break;
                        case 3:
                            if (difficultyCheck(endTime)){
                                difficultyIncrease();
                            }
                            break;
                        case 4:
                            if (difficultyCheck(endTime)){
                                difficultyIncrease();
                            }
                            break;
                        case 5:
                            if (difficultyCheck(endTime)){
                                difficultyIncrease();
                            }
                            break;
                        case 6:
                            if (difficultyCheck(endTime)){
                                difficultyIncrease();
                            }
                            break;
                        default:
                            Log.i("Time counter","Default case");
                    }



                    // Launch next level
                    constructLevel(
                            cardsRenderer.getNumOfCards().x +1,
                            getIntent().getIntExtra("initialShowDelay", 2000)

                    );
                    cardMatchCounter = 0;
                    startTime = System.currentTimeMillis();
                    pickedCardTypes.clear();
                }
                if(buttonRepeat.isTouched(x, y)){
                    // Repeat current level
                    constructLevel(
                            cardsRenderer.getNumOfCards().x,
                            getIntent().getIntExtra("initialShowDelay", 2000)
                    );
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
        //boolean visible[] = new boolean[size.x * size.y];

        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                Card card = cardsRenderer.getCard(x, y);
                if(card == null){
                    posx[y * size.x + x] = -1;
                    posy[y * size.x + x] = -1;
                } else {
                    posx[y * size.x + x] = card.getPos().x;
                    posy[y * size.x + x] = card.getPos().y;
                    //visible[y * size.x + x] = cardsRenderer.isVisible(x, y);
                }
            }
        }

        // Put it inside of bundle as 1D arrays
        bundle.putIntArray("posx", posx);
        bundle.putIntArray("posy", posy);
        //bundle.putBooleanArray("visible", visible);
    }

    private String[] nBack() {
        String[] temp = new String[3];
        temp[0]= pickedCardTypes.get(pickedCardTypes.size() - difficulty);
        temp[1]= cardTypes[new Random().nextInt(cardTypes.length)];
        temp[2]= cardTypes[new Random().nextInt(cardTypes.length)];
        while (temp[1].equals(temp[0])){
            temp[1]= cardTypes[new Random().nextInt(cardTypes.length)];
        }
        while (temp[2].equals(temp[0]) || temp[2].equals(temp[1]) ){
            temp[2]= cardTypes[new Random().nextInt(cardTypes.length)];
        }
        return temp;
    }
    private int answerCounterCheck(){
        int temp = 0;
        for (int i = 0;i<nbackAnswers.size();i++){
            if (nbackAnswers.get(i) == true){
                temp += 1;
            }
        }
        return temp;
    }
    /**
     *
     * @param endTime time at the end of the stage
     * @param cardNum ammount of cards
     * @return  boolean determining if the player was quick enough
     */
    private boolean timeCheck(long endTime, int cardNum){
        long timeTaken = (endTime-startTime)/1000; //time in seconds
        if (timeTaken < (cardNum*10)){
            return true;
        }
        else {
            return false;
        }
    }
    /**
     *
     * @param endTime   time at the end of the stage
     * @return  a boolean determining if difficulty should be increased
     */
    private boolean difficultyCheck(long endTime){
        boolean temporary = true;
        if (nbackAnswers.size() !=0){
            if ((float)(answerCounterCheck()/nbackAnswers.size()*100)>50){
                switch (cardsRenderer.getNumOfCards().x){
                    case 3:
                        temporary= timeCheck(endTime, 3);
                        break;
                    case 4:
                        temporary= timeCheck(endTime, 4);
                        break;
                    case 5:
                        temporary= timeCheck(endTime, 5);
                        break;
                    case 6:
                        temporary= timeCheck(endTime, 6);
                        break;
                    default:
                    Log.i("Diff check", "default");
                        temporary= false;
                }

            }
        }
        return temporary;
    }
    /**
     * Increment difficulty based on its current value
     */
    private void difficultyIncrease(){
        if (!(difficulty+1 > cardsRenderer.getNumOfCards().x+cardsRenderer.getNumOfCards().y)){
            if (difficulty == 0 || difficulty == 1){
                difficulty = 2;
            }
            else {
                difficulty +=1;
            }
        }

    }

    /**
     *
     * @param temp array of answers
     * @return  shuffled array of answers
     */
    private String[] answerShifter(String[] temp){
        String[] answers = new String[3];
        int shift = new Random().nextInt(7);
        switch (shift){
            case 0:
                answers[0]=temp[2];
                answers[1]=temp[1];
                answers[2]=temp[0];
                break;
            case 1:
                answers[0]=temp[2];
                answers[1]=temp[0];
                answers[2]=temp[1];
                break;
            case 2:
                answers[0]=temp[1];
                answers[1]=temp[0];
                answers[2]=temp[2];
                break;
            case 3:
                answers[0]=temp[1];
                answers[1]=temp[2];
                answers[2]=temp[0];
                break;
            case 4:
                answers[0]=temp[0];
                answers[1]=temp[1];
                answers[2]=temp[2];
                break;
            case 5:
                answers[0]=temp[0];
                answers[1]=temp[2];
                answers[2]=temp[1];
                break;
        }

        return answers;
    }

}
