package com.example.memorygame.resources;

import android.app.Activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class GameResource extends Activity {
    private String packageName;
    private ArrayList<PictureResource> pictureResources;

    public int getSizePictureResources(){
        return pictureResources.size();
    }

    public ArrayList<PictureResource> getPictureResources() {
        return pictureResources;
    }

    public PictureResource getPictureResources(int index) {
        return pictureResources.get(index);
    }

    public void setPictureResources(int index, PictureResource setter){
        this.pictureResources.set(index, setter);
    }

    public GameResource(String packageName){
        init(packageName);
    }

    //result is first index found the ID
    //not found result for -1
    public int findId(String id){
        for (int index = 0; index < pictureResources.size(); index++){
            if (pictureResources.get(index).getId() == id)
            return index;
        }
        return -1;
    }

    public boolean isExistingId(String id){
        if (findId(id) == -1)
            return false;
        return true;
    }

    public void init(String packageName){
        this.packageName = packageName;
        ArrayList<String> filenames = getDrawableResourceFilesName(packageName);
        pictureResources = new ArrayList<>();
        for (int i = 0; i < filenames.size(); i++){
            String id = generatePictureResourceId();
            PictureResource pictureResource = new PictureResource(id, packageName, filenames.get(i));
            pictureResources.add(pictureResource);
        }
    }

    public ArrayList<String> getDrawableResourceFilesName(String packageName){
        ArrayList<String> filenames = new ArrayList<>();
        Field[] drawablesFields = com.example.memorygame.R.drawable.class.getFields();
        for (int i = 0; i < drawablesFields.length; i++){
            String filename = drawablesFields[i].getName();
            if (filename.startsWith(packageName + "_")){
                filenames.add(filename);
            }
        }
        return filenames;
    }

    public String generatePictureResourceId(){
        String uniqueID;
        do {
            uniqueID = UUID.randomUUID().toString();
        }
        while (isExistingId(uniqueID));
        return uniqueID;
    }
}
