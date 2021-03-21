package com.example.memorygame.card_linking;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MatrixCard implements MatrixCardInterface {
    private final String tag = "MatrixCardTag";
    private ArrayList<Card> matrix;
    private int row;
    private int col;
    private int size;
    private int specialPosistion;

    public MatrixCard(){
        row = 0;
        col = 0;
        size = row * col;
        Card card = new Card(0, 0);
        matrix = new ArrayList<Card>();
        for (int i = 0; i < size; i++){
            matrix.add(card);
        }
        specialPosistion = -1;
    }

    public MatrixCard(int row, int col, Card card){
        this.row = row;
        this.col = col;
        size = row * col;
        matrix = new ArrayList<Card>();
        for (int i = 0; i < size; i++){
            matrix.add(card);
        }
        specialPosistion = -1;
    }

    @Override
    public int getCountOf(String attributeName, int value) {
        switch (attributeName){
            case "value":
                return getCountOfValue(value);
            case "state":
                return getCountOfState(value);
        }
        return -1;
    }

    @Override
    public MatrixPosition getSize(){
        MatrixPosition size = new MatrixPosition(row, col);
        return size;
    }

    @Override
    public int getCountOfValue(int value){
        int count = 0;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                int index = r * col + c;
                Card card = matrix.get(index);
                if (card.getValue() == value){
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public int getCountOfState(int state){
        int count = 0;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                int index = r * col + c;
                Card card = matrix.get(index);
                if (card.getState() == state){
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public Card getCard(int index){
        return matrix.get(index);
    }

    @Override
    public Card getCard(int row, int col){
        int index = row * this.col + col;
        return matrix.get(index);
    }

    @Override
    public int getCard(int index, String attribute){
        int row = index/this.col;
        int col = index - (row * this.col);
        return getCard(row, col, attribute);
    }

    @Override
    public int getCard(int row, int col, String attribute){
        switch (attribute){
            case "value":
                return getCardValue(row, col);
            case "state":
                return getCardState(row, col);
            default:
                return -1;
        }
    }

    @Override
    public ArrayList<Card> getCards(String attribute, int value){
        switch (attribute){
            case "value":
                return getCardsByValue(value);
            case "state":
                return getCardsByState(value);
            default:
                return null;
        }
    }

    @Override
    public ArrayList<Card> getCardsByValue(int value) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int index = 0; index < matrix.size(); index++){
            Card card = matrix.get(index);
            if (card.getValue() == value){
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public ArrayList<Card> getCardsByState(int state) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int index = 0; index < matrix.size(); index++){
            Card card = matrix.get(index);
            if (card.getState() == state){
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public int getCardValue(int row, int col){
        int index = row * this.col + col;
        return this.matrix.get(index).getValue();
    }

    @Override
    public int getCardState(int row, int col){
        int index = row * this.col + col;
        Card card = matrix.get(index);
        return card.getState();
    }

    @Override
    public void setCard(int row, int col, Card card){
        int index = row * this.col + col;
        matrix.set(index, card);
    }

    @Override
    public void setCard(int index, Card card){
        matrix.set(index, card);
    }

    @Override
    public int setCards(String attribute, ArrayList<Integer> values){
        switch (attribute){
            case "value":
                return this.setValues(values);
            case "state":
                return setStates(values);
            default:
                Log.e(tag, "MatrixCard.java, setCard, attribute is not valid");
                return -1;
        }
    }

    @Override
    public void setCard(int row, int col, String attribute, int value){
        switch (attribute){
            case "value":
                setValue(row, col, value);
                break;
            case "state":
                setState(row, col, value);
                break;
            default:
                Log.e(tag, "MatrixCard.java, setCard, attribute is not valid");
        }
    }

    @Override
    public void setValue(int index, int value){
        Card card = matrix.get(index);
        card.setValue(value);
        this.matrix.set(index, card);
    }

    //try with throw exception
    //public void setValue(int row, int col, int value) throws Exception {//code here};

    @Override
    public void setValue(int row, int col, int value){
        int index = row * this.col + col;
        if (index >= this.row * this.col){
            Log.w(tag, "MatrixCard.java, setValue, index out of matrix's range");
        }
        Card card = matrix.get(index);
        card.setValue(value);
        matrix.set(index, card);
    }

    @Override
    public void setState(int index, int state){
        Card card = matrix.get(index);
        card.setState(state);
        matrix.set(index, card);
    }

    @Override
    public void setState(int row, int col, int state){
        int index = row * this.col + col;
        if (index >= this.row * this.col){
            Log.w(tag, "MatrixCard.java, setState, index out of matrix's range");
        }
        Card card = matrix.get(index);
        card.setState(state);
        matrix.set(index, card);
    }

    @Override
    public int setValues(ArrayList<Integer> values) {
        if (values.size() == this.matrix.size()){
            for (int index = 0; index < values.size(); index++){
                Card card = matrix.get(index);
                card.setValue(values.get(index));
                matrix.set(index, card);
//                this.setValue(index, values.get(index));
                Log.d("PlayActivityTag", "Index: " + Integer.toString(index));
                Log.d("PlayActivityTag", Integer.toString(values.get(index)));
            }
            return 0;
        }
        return -1;
    }

    @Override
    public int setStates(ArrayList<Integer> states){
        if (states.size() == this.matrix.size()){
            for (int index = 0; index < matrix.size(); index++){
                setState(index, states.get(index));
            };
            return 0;
        }
        return -1;
    }

    @Override
    public String getStringValue(){
        StringBuilder str = new StringBuilder();
        for (int index = 0; index < matrix.size(); index++){
            Card card = matrix.get(index);
            String node = Integer.toString(index) + ": (" + Integer.toString(card.getValue())
                + "," + Integer.toString(card.getState()) +  ")";
            str.append(node).append("\n");
        }
        return str.toString();
    }

    public void mixValue() {
        for (int i = 0; i < size; i ++){
            Random random = new Random();
            int rand = random.nextInt(size);
            Card temp = matrix.get(i);
            matrix.set(i, matrix.get(rand));
            matrix.set(rand, temp);
            if (i == size-1){
                specialPosistion = rand;
            }
        }
    }

    public int getSpecialPosistion(){
        return specialPosistion;
    }
}
