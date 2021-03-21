package com.example.memorygame.resources;

public class PictureResource {
    private String packageName;
        //selected content from ChooseContentActivity.
        //excample: "fruit"
    private String id;
        //generate new id for this class.
    private String fileName; //the name of image file in drawable directory/
        //Example: "fruit_apple1.jpg" => fileName = "fruit_apple1"
    private boolean active;

    public PictureResource(String id, String packageName, String fileName){
        this.packageName = packageName;
        this.id = id;
        this.fileName = fileName;
        active = false;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
