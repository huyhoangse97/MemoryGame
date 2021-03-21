package com.example.memorygame.database;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoundExecutorAdapter {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Context context;
    RoundDao roundDao;

    public RoundExecutorAdapter(Context context, RoundDao roundDao){
        this.context = context;
        this.roundDao = roundDao;
    }

    public List<Round> getAll(){
        Callable<List<Round>> getAll = () ->{
            return roundDao.getAll();
        };
        try {
            return executor.submit(getAll).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Round> getByModeId(int modeId){
        Callable<List<Round>> getAll = () ->{
            return roundDao.getByModeId(modeId);
        };
        try {
            return executor.submit(getAll).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Round getByModeAndRoundName(String modeName, String roundName){
        Callable<Round> getByModeAndRoundName = () ->{
            return roundDao.getByModeAndRoundName(modeName, roundName);
        };
        try {
            return executor.submit(getByModeAndRoundName).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Round getByModeAndRoundId(int modeId, int roundId){
        Callable<Round> getByModeAndRoundId = () ->{
            return roundDao.getByModeAndRoundId(modeId, roundId);
        };
        try {
            return executor.submit(getByModeAndRoundId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reset(){
        executor.submit(
            new Runnable() {
                @Override
                public void run() {
                    roundDao.deleteAll();
                }
            }
        );
    }

    public void delete(Round round){
        executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        roundDao.delete(round);
                    }
                }
        );
    }

    public void insert(Round round){
        executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        roundDao.insert(round);
                    }
                }
        );
    }

    public void insertAll(List<Round> rounds){
        for (int idx = 0; idx < rounds.size(); idx++){
            Round round = rounds.get(idx);
            executor.submit(
                    new Runnable() {
                        @Override
                        public void run() {
                            roundDao.insert(round);
                        }
                    }
            );
        }
    }

    public void update(Round round) {
        executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        roundDao.update(round);
                    }
                }
        );
    }
}
