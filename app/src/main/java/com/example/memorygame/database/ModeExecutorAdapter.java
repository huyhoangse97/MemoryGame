package com.example.memorygame.database;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ModeExecutorAdapter {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "ModeExecutorAdapter";
    private final Context context;
    private final ModeDao modeDao;

    public ModeExecutorAdapter(Context context, ModeDao modeDao){
        this.context = context;
        this.modeDao = modeDao;
    }

    public List<Mode> getAll(){
        Callable<List<Mode>> getAll = modeDao::getAll;
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
            return executor.submit(getById).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d(TAG, "ExecutionException");
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "InterruptedException");
            return null;
        } catch (TimeoutException e) {
            Log.d(TAG, "TimeoutException");
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
