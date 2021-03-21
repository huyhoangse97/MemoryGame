package com.example.memorygame.card_linking;

public class MatrixSize extends MatrixPosition {
    private int row;

    @Override
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int col;

    public MatrixSize(int row, int col){
        this.row = row;
        this.col = col;
    }
}
