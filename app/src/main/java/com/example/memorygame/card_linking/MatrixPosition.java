package com.example.memorygame.card_linking;

//matrixPosition
public class MatrixPosition {
    private int row;
    private int col;

    public MatrixPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    public MatrixPosition(){
        row = 0;
        col = 0;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public boolean equal(MatrixPosition position) {
        if (this.getRow() == position.getRow()){
            if (this.getCol() == position.getCol()){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("(").append(Integer.toString(this.row)).append(", ").append(Integer.toString(this.col)).append(")");
        return str.toString();
    }
}
