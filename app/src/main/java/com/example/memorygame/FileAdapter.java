package com.example.memorygame;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAdapter {
    private static final String TAG = "FileAdapter";
    String fileName;
    Context context;
    File file;

//    public FileAdapter(Context context, String fileName){
//        this.context = context;
//        this.fileName = fileName;
//        File cacheDir = new File(context.getCacheDir(), "options");
//        cacheDir.mkdirs();
//        File file = new File(cacheDir, ""+ fileName);
//        Log.i(TAG, file.getPath());
//    }

    public FileAdapter(Context context, String fileName){
        this.context = context;
        this.fileName = fileName;
        java.io.File path = context.getFilesDir();
        file = new File(path, fileName);
        Log.i(TAG, file.getPath());
    }

    public void write(String content){
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(){
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
            return new String(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
