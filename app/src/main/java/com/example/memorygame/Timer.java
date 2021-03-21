package com.example.memorygame;

public class Timer {
    private final int timeUnit = 2;
    private int limitTime;

    public Timer(int count){
        limitTime = count * timeUnit * 2;
    }

    public int getPenalTime(int count){
        return (count * timeUnit);
    }

    public int getLimitTime(){
        return limitTime;
    }
}
