package com.example.memorygame.card_linking;

public class Card {
    private int value;//Image source id;
    private int state;//0: non-active, 1: temp active, 2: actived;

    public Card(){
        value = 0;
        state = 0;
    }

    public Card(int value, int state){
        this.value = value;
        this.state = state;
    }

    public int getValue(){
        return this.value;
    }

    public int getState(){
        return this.state;
    }

    public void setValue(int value){
        this.value = value;
    }

    public void setState(int state) {
        this.state = state;
    }
}
