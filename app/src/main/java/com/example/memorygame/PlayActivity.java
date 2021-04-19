package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.memorygame.card_linking.Card;
import com.example.memorygame.card_linking.MatrixCard;
import com.example.memorygame.card_linking.MatrixPosition;
import com.example.memorygame.database.Round;
import com.example.memorygame.resources.GameResource;
import com.example.memorygame.resources.PictureResource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
     //const
    private int defaultValue;
    private int matchingNum; //number of images that they matching together;

    //variable
    private final String tag = "PlayActivityTag";
    private Game game;
    private GameResource gameResource;
    private MatrixCard matrixCard;
    private ArrayList<ImageButton> frontButtons, backButtons;
    private int secondsRemaining;

    //current variable (stored state button temp active)
    private int currentNumberButtonTempActive;
    private Queue<ImageButton> buttonTempActived = new LinkedList<ImageButton>();
    private Queue<MatrixPosition> buttonTempActivedPosition = new LinkedList<MatrixPosition>();
    TextView tv_timer, tv_round, tv_bottomMessage;
    ProgressBar pb_timer;
    CountDownTimer timer;
    Button btn_play;
    TableLayout tableLayout;
    int matrixId;

    //get - set method

    public int getMatrixDefaultValueId() {
        return defaultValue;
    }

    public Queue<MatrixPosition> getButtonTempActivedPosition(){
        return buttonTempActivedPosition;
    };

    public int getCurrentNumberButtonActive() {
        return currentNumberButtonTempActive;
    }

    public void setCurrentNumberButtonActive(int currentNumberButtonTempActive) {
        this.currentNumberButtonTempActive = currentNumberButtonTempActive;
    }

    public int getMatchingImageNum() {
        return matchingNum;
    }

    public Game getGame(){
        return game;
    }

    public MatrixPosition getSize(){
        MatrixPosition tableSize = new MatrixPosition(game.getTableRow(), game.getTableCol());
        return tableSize;
    }

    public void setMatrixCard(int row, int col, String attr, int i) {
        matrixCard.setCard(row, col, attr, i);
    }

    public int getMatrixCard(int row, int col, String attr) {
        return matrixCard.getCard(row, col, attr);
    }

    public ArrayList<Card> getCards(String attr, int value){
        return matrixCard.getCards(attr, value);
    }

    public PictureResource setResources(){
        PictureResource selectedPictureResource;
        Random random = new Random();
        int gameResourceIndex;
        do {
            gameResourceIndex = random.nextInt((gameResource.getPictureResources()).size());
            selectedPictureResource = gameResource.getPictureResources().get(gameResourceIndex);
        }
        while (selectedPictureResource.getActive());

        selectedPictureResource.setActive(true);
        gameResource.setPictureResources(gameResourceIndex, selectedPictureResource);
        return selectedPictureResource;
    }

    //QUEUE method;
    //add an Button to queue
    public void addTemporaryActivedButton(ImageButton button){
        buttonTempActived.add(button);
    }

    public void addMatrixPosition(MatrixPosition matrixPosition){
        buttonTempActivedPosition.add(matrixPosition);
    }

    //size
    public int getSizeQueue(){
        return buttonTempActived.size();
    }

    //poll
    public ImageButton getFirstQueueElement(){
        return buttonTempActived.poll();
    }

    //Poll
    public MatrixPosition getFirstMatrixPosition(){
        return buttonTempActivedPosition.poll();
    }

    //INIT method
    //init buttons
    public void getButtons(){
        frontButtons = getFrontButtons();
        backButtons = getBackButtons();
    }

    private ArrayList<ImageButton> getFrontButtons() {
        ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
        for (int row = 0; row < game.getTableRow(); row++) {
            for (int col = 0; col < game.getTableCol(); col++) {
                int btnId = getResources().getIdentifier("front_card_"
                        + Integer.toString(row) + "_" + Integer.toString(col), "id", getPackageName());
                ImageButton button = findViewById(btnId);
                button.setEnabled(false);
                buttons.add(button);
            }
        }
        return buttons;
    }
    
    private ArrayList<ImageButton> getBackButtons() {
        ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
        for (int row = 0; row < game.getTableRow(); row++) {
            for (int col = 0; col < game.getTableCol(); col++) {
                int btnId = getResources().getIdentifier("front_card_"
                        + Integer.toString(row) + "_" + Integer.toString(col), "id", getPackageName());
                ImageButton button = findViewById(btnId);
                button.setEnabled(false);
                buttons.add(button);
            }
        }
        return buttons;
    }
    
    public void initMatrixCard(ArrayList<Integer> imageIds){
        ArrayList<Integer> imageIds2 = new ArrayList<Integer>();
        imageIds2 = imageIds;
        imageIds.addAll(imageIds2);

        if ((game.getTableRow()*game.getTableCol()) % 2 == 1){
            imageIds.add(defaultValue);
        }

        int size = game.getTableRow()  * game.getTableCol();
        for (int index = 0; index < size; index++){
            Card card = new Card(imageIds.get(index),0);
            matrixCard.setCard(index, card);
        }
        matrixCard.mixValue();
    }

    //init
    public void init(Context context){
        int emptyImageId = getResources().getIdentifier("empty_card", "drawable", getPackageName());
        this.defaultValue = emptyImageId;
        matchingNum = 2;// will get input from choose type activity;

        this.currentNumberButtonTempActive = 0;

        Round round = new Round();
//        round = getSelectedRound();
        this.game = new Game(round.getRoundValue());

        Card card = new Card(defaultValue, 0);
        matrixCard = new MatrixCard(game.getTableRow(), game.getTableCol(), card);
        this.gameResource = new GameResource("flower");
        
        FrameLayout fl_main_content = findViewById(R.id.fl_main_content);
        inflateLayout(fl_main_content);

        getButtons();
        initButtonLayout();
        linkImageSource();

        try {
            shouldKnowWhenOnInterceptTouchEventWasCalled(tableLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        secondsRemaining = game.getTime().getLimitTime();
        timer = initCountDownTimer();
    }

    private Round getSelectedRound() {
        FileAdapter fileAdapter = new FileAdapter(getApplicationContext(), "selected_round.json");
        String jsonString = fileAdapter.read();
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Round.class);
    }

    private void initButtonLayout() {
        Log.i(tag, "settingButtonLayout: margin, width, height");
        initFrontButtons();
        initBackButtons();
    }

    private void initFrontButtons() {
        for (int index = 0; index < frontButtons.size(); index++){
            ImageButton frontButton = frontButtons.get(index);

            setButtonParameters(frontButton);
        }
    }

    private void initBackButtons() {
        for (int index = 0; index < backButtons.size(); index++){
            ImageButton backButton = backButtons.get(index);
            setButtonParameters(backButton);
        }
    }

    private void setButtonParameters(ImageButton imageButton) {
        int leftBtnMargin = ((ViewGroup.MarginLayoutParams) imageButton.getLayoutParams()).leftMargin;
        int rightBtnMargin = ((ViewGroup.MarginLayoutParams) imageButton.getLayoutParams()).rightMargin;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int btnWidth = (int)(dm.widthPixels - (leftBtnMargin + rightBtnMargin)* game.getTableRow())/game.getTableCol();
        int btnHeight = btnWidth;

        android.view.ViewGroup.LayoutParams imageButtonParams = imageButton.getLayoutParams();
        imageButtonParams.height = btnHeight;
        imageButtonParams.width = btnWidth;
        imageButton.setLayoutParams(imageButtonParams);
    }

    private CountDownTimer initCountDownTimer(){
        int limitTimer = game.getTime().getLimitTime();
        CountDownTimer timer = new CountDownTimer(limitTimer*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining--;
                tv_timer.setText(Integer.toString(secondsRemaining) + "Sec");
                pb_timer.setProgress(limitTimer - secondsRemaining);
            }

            @Override
            public void onFinish() {
                tv_bottomMessage.setText("~ Time is up! ~");
                Intent intent = new Intent();
                intent.setClass(PlayActivity.this, ResultActivity.class);
//                intent.putExtra("Game", g.toJSon());
//                intent.putExtra("score", g.getScore());
//                intent.putExtra("totalQuestion", g.getTotalQuestion());
//                intent.putExtra("correctAnswer", g.getCorrectAnswer());
//                intent.putExtra("incorrectAnswer", g.getIncorrectAnswer());
                startActivity(intent);
            }
        };
        return timer;
    }

    public void shouldKnowWhenOnInterceptTouchEventWasCalled(TableLayout tableLayout) throws Exception {
        MotionEvent touchEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0, 0, 0);
        tableLayout.onInterceptTouchEvent(touchEvent);
    }

    private void inflateLayout(FrameLayout fl_main_content) {
        inflateBackLayout(fl_main_content);
        inflateFrontLayout(fl_main_content);
        inflateTransparentOverlay(fl_main_content);
    }

    private void inflateFrontLayout(FrameLayout fl_main_content) {
        Log.i(tag, "Inflate front layout");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        int matrixLayoutId = getResources().getIdentifier("front_matrix_" + Integer.toString(game.getTableRow())
                + "x" + Integer.toString(game.getTableCol()), "layout", getPackageName());
        View view = layoutInflater.inflate(matrixLayoutId, fl_main_content, true);
    }

    private void inflateBackLayout(FrameLayout fl_main_content) {
        Log.i(tag, "Inflate back layout");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        int matrixLayoutId = getResources().getIdentifier("back_matrix_" + Integer.toString(game.getTableRow())
                + "x" + Integer.toString(game.getTableCol()), "layout", getPackageName());
        View view = layoutInflater.inflate(matrixLayoutId, fl_main_content, true);
    }

    private void inflateTransparentOverlay(FrameLayout fl_main_content) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.transparent_overlay, fl_main_content, true);
    }


    //Connect each button with an image resource;
    private void linkImageSource() {
        int numImage = (game.getTableRow() * game.getTableCol())/matchingNum;  //Số cặp image giống nhau trong table;
        Log.d("PlayTag", Integer.toString(numImage));
        ArrayList<Integer> imageIds = new ArrayList<Integer>();
        for (int index = 0; index < numImage; index++){
            PictureResource selectedPictureResource = setResources();
            int drawableId = getResources().getIdentifier(
                    selectedPictureResource.getFileName(), "drawable", getPackageName());
            imageIds.add(drawableId);
        }

        initMatrixCard(imageIds);

        setBackCardResource();
    }

    private void setBackCardResource() {
        for (int row = 0; row < game.getTableRow(); row++){
            for (int col = 0; col < game.getTableCol(); col++){
                int resourceId = matrixCard.getCardValue(row, col);
                int index = row * game.getTableCol() + col;
                backButtons.get(index).setBackgroundResource(resourceId);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        tv_timer = findViewById(R.id.tv_timer);
        pb_timer = findViewById(R.id.pb_timer);

        init(getApplicationContext());

        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startGame();
            }

            private void startGame() {
                enablePlayMode();
                settingParams();
            }

            private void settingParams() {
                timer.start();
            }

            private void enablePlayMode() {

                btn_play.setVisibility(View.GONE);
                RelativeLayout transparent_overlay;
                transparent_overlay = findViewById(R.id.transparent_overlay);
                transparent_overlay.setVisibility(View.GONE);
                for (int row = 0; row < game.getTableRow(); row++){
                    for (int col = 0; col < game.getTableCol(); col++){
                        int index = row * game.getTableCol() + col;
                        ImageButton frontButton = frontButtons.get(index);
                        frontButton.setEnabled(true);
                    }
                }
            }
        });

        //Set Button onClickListener;
        for (int row = 0; row < game.getTableRow(); row++){
            for (int col = 0; col < game.getTableCol(); col++){
                int index = row * game.getTableCol() + col;
                ImageButton frontButton = frontButtons.get(index);
                frontButton.setOnClickListener(new ImageButtonClickListener(row, col, this, PlayActivity.this));
            }
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}