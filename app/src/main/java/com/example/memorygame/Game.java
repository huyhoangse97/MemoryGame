package com.example.memorygame;

public class Game {
    private int tried;
    private int round;
    private Timer timer;
    private int tableRow;
    private int tableCol;

    public Game(int round){
        this.tried = 0;
        this.round = round;
        getMatrixSize();
        timer = new Timer(tableCol * tableRow);
    }

    private void getMatrixSize() {
        if (round <= 1){
            this.tableRow = 3;
            this.tableCol = 3;
        }
        if (round == 2){
            this.tableRow = 4;
            this.tableCol = 3;
        }
        if (round == 3){
            this.tableRow = 4;
            this.tableCol = 4;
        }
        if (round == 4){
            this.tableRow = 5;
            this.tableCol = 4;
        }
        if (round == 5){
            this.tableRow = 5;
            this.tableCol = 5;
        }
        if (round == 6){
            this.tableRow = 6;
            this.tableCol = 5;
        }
        if (round == 7){
            this.tableRow = 7;
            this.tableCol = 5;
        }
    }

    public Timer getTime(){
        return timer;
    }

    public int getTableRow() {
        return tableRow;
    }

    public int getTableCol() {
        return tableCol;
    }
}
