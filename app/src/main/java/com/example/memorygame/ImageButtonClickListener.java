package com.example.memorygame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import android.os.Handler;
import android.widget.TextView;

import com.example.memorygame.card_linking.Card;
import com.example.memorygame.card_linking.MatrixPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ImageButtonClickListener implements View.OnClickListener {
    private final String tag = "ButtonListenerTag";
    private final int row; //currentRow;
    private final int col; //currentCol;
    private PlayActivity playActivity;
    private Context context;
    private ImageButton clickedButton;

    public ImageButtonClickListener(){
        row = 0;
        col = 0;
        playActivity = null;
        context = null;
    }

    public ImageButtonClickListener(int row, int col, PlayActivity playActivity, Context context) {
        this.row = row;
        this.col = col;
        this.playActivity = playActivity;
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v){
        clickedButton = (ImageButton) v;
        MatrixPosition currentPosition = new MatrixPosition(row, col);
        updateView(clickedButton, currentPosition, 1);
        updateMode(currentPosition, 1);

        if (!isSpecial()){
            addOnQueue();
            if (checkCount()){
                if (isMatching()){
                    updateAll(2); //update and remove element out off queue;
                    Log.d("PlayActivityTag", "Matching");
                }
                else {
                    Handler handler = new Handler();
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            restoreAll();
                            Log.d("PlayActivityTag", "UnMatch");
                        }
                    };
                    handler.postDelayed(myRunnable, 1000);
                }
            }
        }
        else {
            updateView(clickedButton, currentPosition, 2);
            updateMode(currentPosition, 2);
        }

        if (isEndGame()){
            TextView tv_round = playActivity.findViewById(R.id.tv_round);
            tv_round.setText("Win");
            tv_round.setTextColor(R.color.teal_700);
        }
    }

    private void restoreAll() {
        for (int i = 0; i < playActivity.getMatchingImageNum(); i++){
            ImageButton button = playActivity.getFirstQueueElement();
            MatrixPosition position = playActivity.getFirstMatrixPosition();
            if (button != null){
                restoreView(button);
                restoreMode(position);
            }
        }
    }

    private void updateAll(int state) {
        ImageButton button;
        MatrixPosition position;
        for (int i = 0; i < playActivity.getMatchingImageNum(); i++){
            button = playActivity.getFirstQueueElement();
            position = playActivity.getFirstMatrixPosition();
            if (button != null){
                updateView(button, position, state);
                updateMode(position, state);
            }
        }
    }

    private boolean checkCount() {
        int size = playActivity.getSizeQueue();
        int numMatching = playActivity.getMatchingImageNum();
        return size % numMatching == 0;
    }

    private void addOnQueue() {
        int count = playActivity.getCurrentNumberButtonActive();
        playActivity.setCurrentNumberButtonActive(count+1);
        playActivity.addTemporaryActivedButton(clickedButton);//add current button to queue;
        MatrixPosition position = new MatrixPosition(row, col);
        playActivity.addMatrixPosition(position);
    }

    private boolean isSpecial() {
        int imageValue = playActivity.getMatrixCard(this.row, this.col, "value");
        int defaultImageValue = playActivity.getMatrixDefaultValueId();

        if (imageValue == defaultImageValue){
            return true;
        }
        else return false;
    }

//    private void updateView(ImageButton button, MatrixPosition position, int state){
//        int row = position.getRow();
//        int col = position.getCol();
//
//        Log.d(tag, "state: " + Integer.toString(state) );
//        switch (state){
//            case 1:
//                int imageId = playActivity.getMatrixCard(row, col, "value");
//                if (imageId != 0){
//                    button.setBackgroundResource(imageId);
//                }
//                button.setClickable(false);
//                Log.d("PlayActivityTag", "updateView(): " + Integer.toString(imageId) );
//                break;
//            case 2:
//                button.setAlpha((float)0.5);
//                break;
//            default:
//                break;
//        }
//    };

    private void updateView(ImageButton button, MatrixPosition position, int state){
        int row = position.getRow();
        int col = position.getCol();

        Log.d(tag, "state: " + Integer.toString(state) );
        switch (state){
            case 1:
                int imageId = playActivity.getMatrixCard(row, col, "value");
                if (imageId != 0){
                    button.setBackgroundResource(imageId);
                }
                button.setClickable(false);
                Log.d("PlayActivityTag", "updateView(): " + Integer.toString(imageId) );
                break;
            case 2:
                button.setAlpha((float)0.5);
                break;
            default:
                break;
        }
    };

    private void updateMode(MatrixPosition position, int state){
        int row = position.getRow();
        int col = position.getCol();
        switch (state){
            case 1:
                playActivity.setMatrixCard(row, col, "state", 1);
                break;
            case 2:
                playActivity.setMatrixCard(row, col, "state", 2);
                break;
            default:
                break;
        }
    }

    private boolean isMatching(){
        Queue<MatrixPosition> positionTempActived = new LinkedList<MatrixPosition>();
        positionTempActived.addAll(playActivity.getButtonTempActivedPosition());
        MatrixPosition firstPosition = positionTempActived.poll();
        int firstValue = playActivity.getMatrixCard(firstPosition.getRow(), firstPosition.getCol(), "value");

        for (int i = 0; i < playActivity.getMatchingImageNum()-1; i++){
            MatrixPosition position = positionTempActived.poll();
            int value = playActivity.getMatrixCard(position.getRow(), position.getCol(), "value");
            if (!(firstValue == value)){
                return false;
            }
        }
        return true;
    }

    private boolean isEndGame(){
        ArrayList<Card> activedCards = playActivity.getCards("state", 2);
        MatrixPosition tableSize = playActivity.getSize();
        int row = tableSize.getRow();
        int col = tableSize.getCol();
        int numberOfCells = row * col;
        if (activedCards.size() == numberOfCells) {
            return true;
        }
        else {
                return false;
        }
    }

    private void restoreView(ImageButton button){
        if (button != null){
            @SuppressLint("UseCompatLoadingForDrawables") Drawable defaultImage = playActivity.getResources().getDrawable(R.drawable.hidden);
            button.setBackground(defaultImage);
            button.setClickable(true);
            Log.d("PlayActivityTag", "RestoreView");
        }
    }

    private void restoreMode(MatrixPosition position){
        if (position != null){
            int row = position.getRow();
            int col = position.getCol();
            playActivity.setMatrixCard(row, col, "state", 0);
        }
    }

//    private void updatePoint(){
//
//    }
}
