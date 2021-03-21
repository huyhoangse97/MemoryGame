package com.example.memorygame.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModeExecutorAdapter {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Context context;
    ModeDao modeDao;

    public ModeExecutorAdapter(Context context, ModeDao modeDao){
        this.context = context;
        this.modeDao = modeDao;
    }

    public List<Mode> getAll(){
        Callable<List<Mode>> getAll = () ->{
            return modeDao.getAll();
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

    public Mode getById(int modeId){
        Callable<Mode> getById = () ->{
            return modeDao.getById(modeId);
        };
        try {
            return executor.submit(getById).get();
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
                    modeDao.deleteAll();
                }
            }
        );
    }

    public void delete(Mode mode){
        executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        modeDao.delete(mode);
                    }
                }
        );
    }

    public void insert(Mode mode){
        executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        modeDao.insert(mode);
                    }
                }
        );
    }

    public void insertAll(List<Mode> modes){
        for (int idx = 0; idx < modes.size(); idx++){
            Mode mode = modes.get(idx);
            executor.submit(
                    new Runnable() {
                        @Override
                        public void run() {
                            modeDao.insert(mode);
                        }
                    }
            );
        }
    }
}
