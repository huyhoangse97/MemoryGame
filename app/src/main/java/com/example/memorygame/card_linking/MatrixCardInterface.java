package com.example.memorygame.card_linking;

import java.util.ArrayList;

public interface MatrixCardInterface {

    //getSize
    public MatrixPosition getSize();

    //getCount attribute's value == value
    public int getCountOf(String attributeName, int value);

    //get count value == value
    public int getCountOfValue(int value);

    //get count of state == state;
    public int getCountOfState(int state);

    //getCard at index;
    public Card getCard(int index);

    //get card at position(row, col)
    public Card getCard(int row, int col);

    //get value/ state of card at index;
    public int getCard(int index, String attribute);

    //get value/ state of card at position(row, col);
    public int getCard(int row, int col, String attribute);

    //get Cards with value/state equal value;
    public ArrayList<Card> getCards(String attribute, int value);

    //get cards with value equal value
    public ArrayList<Card> getCardsByValue(int value);

    //get cards with state equal value
    public ArrayList<Card> getCardsByState(int state);

    //get value of card at position(row, col)
    public int getCardValue(int row, int col);

    //get state of card at position(row, col)
    public int getCardState(int row, int col);

    //set card at position(row, col) = card
    public void setCard(int row, int col, Card card);

    //set card at index;
    public void setCard(int index, Card card);

    //set cards state/value by an array value;
    public int setCards(String attribute, ArrayList<Integer> values);

    //set card at position(row, col): card's state/value equal value;
    public void setCard(int row, int col, String attribute, int value);

    //set value at index;
    public void setValue(int index, int value);

    //set value at position(row, col)
    public void setValue(int row, int col, int value);

    //set State at index
    public void setState(int index, int state);

    //set state at position(row, col)
    public void setState(int row, int col, int state);

    //set values by an array;
    public int setValues(ArrayList<Integer> values) ;

    //set States by an array;
    public int setStates(ArrayList<Integer> states);

    //convert an Object value to string;
    public String getStringValue();
}
