package com.example.memorygame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
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
    private final String TAG = "ButtonListenerTag";
    private Context context;
    private final int row; //currentRow;
    private final int col; //currentCol;
    private PlayActivity playActivity;
    private ImageButton  clickedFrontButton, clickedBackButton;
    Animator.AnimatorListener flipReverseListener, flipListener;

    public ImageButtonClickListener(){
        this.row = 0;
        this.col = 0;
    }

    public ImageButtonClickListener(PlayActivity playActivity, Context context, int row, int col) {
        this.row = row;
        this.col = col;
        this.playActivity = playActivity;
        this.context = context;
    }

    private void init() {
        MatrixPosition tableSize = playActivity.getSize();
        int index = row * tableSize .getRow() + col;
        clickedFrontButton = playActivity.frontButtons.get(index);
        clickedBackButton = playActivity.backButtons.get(index);
        animationSet();
    }

    private void animationSet() {
        //init Animation Listener;
        flipReverseListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //set all button Enabled is false;
                for (int i = 0; i < playActivity.frontButtons.size(); i++){
                    ImageButton frontButton = playActivity.frontButtons.get(i);
                    frontButton.setClickable(false);
                    ImageButton backButton = playActivity.frontButtons.get(i);
                    backButton.setClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //set all buttons is enable;
                for (int i = 0; i < playActivity.frontButtons.size(); i++){
                    ImageButton frontButton = playActivity.frontButtons.get(i);
                    frontButton.setClickable(true);
                    ImageButton backButton = playActivity.frontButtons.get(i);
                    backButton.setClickable(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        flipListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //set all button Enabled is false;
                for (int i = 0; i < playActivity.frontButtons.size(); i++){
                    ImageButton frontButton = playActivity.frontButtons.get(i);
                    frontButton.setClickable(false);
                    ImageButton backButton = playActivity.frontButtons.get(i);
                    backButton.setClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //set All buttons is enable
                for (int i = 0; i < playActivity.frontButtons.size(); i++){
                    ImageButton frontButton = playActivity.frontButtons.get(i);
                    frontButton.setClickable(true);
                    ImageButton backButton = playActivity.frontButtons.get(i);
                    backButton.setClickable(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v){
        init();

        MatrixPosition currentPosition = new MatrixPosition(row, col);
        updateView(clickedFrontButton, clickedBackButton, 1);
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
            updateView(clickedFrontButton, clickedBackButton, 2);
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
            ImageButton frontButton = playActivity.getFirstFrontQueueElement();//get first and remove
            ImageButton backButton = playActivity.getFirstBackQueueElement();//get first and remove
            Log.d(TAG, "Queue - frontButton Id: " + Integer.toString(frontButton.getId()));
            MatrixPosition position = playActivity.getFirstMatrixPosition();
            if (frontButton != null){
                restoreView(frontButton, backButton);
                restoreMode(position);
            }
        }
    }

    private void updateAll(int state) {
        ImageButton frontButton, backButton;
        MatrixPosition position;
        //search all image currently active, set Visible GONE
        for (int i = 0; i < playActivity.getMatchingImageNum(); i++){
            frontButton = playActivity.getFirstFrontQueueElement();
            backButton = playActivity.getFirstBackQueueElement();
            Log.d(TAG, "updateAll - frontButtonId: " + Integer.toString(frontButton.getId()));
            position = playActivity.getFirstMatrixPosition();
            if (frontButton != null){
                updateView(frontButton, backButton, state);
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
        playActivity.addTemporaryActivedButton(clickedFrontButton, clickedBackButton);//add current button to queue;
        MatrixPosition position = new MatrixPosition(row, col);
        playActivity.addMatrixPosition(position);
    }

    private boolean isSpecial() {
        int imageValue = playActivity.getMatrixCard(this.row, this.col, "value");
        int defaultImageValue = playActivity.getMatrixDefaultValueId();

        return imageValue == defaultImageValue;
    }

    @SuppressWarnings("deprecation")
    private void updateView(ImageButton frontButton, ImageButton backButton, int state){

        Log.d(TAG, "state: " + Integer.toString(state) );
        switch (state){
            case 1:
                //init animation
                AnimatorSet frontAnimation, backAnimation;
                frontAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.front_animation);
                backAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.back_animation);
                frontAnimation.addListener(flipListener);
                //apply animation
                frontAnimation.setTarget(frontButton);
                frontAnimation.start();
                backAnimation.setTarget(backButton);
                backAnimation.start();
                break;
            case 2:
                Handler handler = new Handler();
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "UpdateView state 2");
                        frontButton.setEnabled(false);
                        frontButton.animate()
                                .translationY(frontButton.getHeight())
                                .alpha(0.0f)
                                .setDuration(500);
                        backButton.setEnabled(false);
                        backButton.animate()
                                .translationY(frontButton.getHeight())
                                .alpha(0.0f)
                                .setDuration(500);
                    }
                };
                handler.postDelayed(myRunnable, 1000);
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
        Queue<MatrixPosition> positionTempActived = new LinkedList<MatrixPosition>(playActivity.getButtonTempActivedPosition());
        MatrixPosition firstPosition = positionTempActived.poll();
        int firstValue =  playActivity.getMatrixCard(firstPosition.getRow(), firstPosition.getCol(), "value");

        for (int i = 0; i <  playActivity.getMatchingImageNum()-1; i++){
            MatrixPosition position = positionTempActived.poll();
            int value =  playActivity.getMatrixCard(position.getRow(), position.getCol(), "value");
            if (!(firstValue == value)){
                return false;
            }
        }
        return true;
    }

    private boolean isEndGame(){
        ArrayList<Card> activedCards =  playActivity.getCards("state", 2);
        MatrixPosition tableSize =  playActivity.getSize();
        int row = tableSize.getRow();
        int col = tableSize.getCol();
        int numberOfCells = row * col;
        return activedCards.size() == numberOfCells;
    }

    private void restoreView(ImageButton frontButton, ImageButton backButton){
        //start animation flip-reverse
        Log.d(TAG, "RestoreView - Queue - frontButton Id: " + Integer.toString(frontButton.getId()));
        if (frontButton != null){
            //init animation
            AnimatorSet frontAnimationRe, backAnimationRe;
            frontAnimationRe = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.front_animation_reverse);
            backAnimationRe = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.back_animation_reverse);
            frontAnimationRe.addListener(flipReverseListener);
            //apply animation
            frontAnimationRe.setTarget(frontButton);
            backAnimationRe.setTarget(backButton);
            frontAnimationRe.start();
            backAnimationRe.start();
            Log.d(TAG, "RestoreView");
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
