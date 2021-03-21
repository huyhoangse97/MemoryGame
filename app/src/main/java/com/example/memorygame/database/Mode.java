package com.example.memorygame.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MODE")
public class Mode {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String name;

    private int id;

    private int roundCount;

    public Mode(){
        this.name = "simple";
        this.id = 1;
        this.roundCount = 50;
    }

    public Mode(String modeName, int buttonId){
        this.name = modeName;
        this.id = buttonId;
        this.roundCount = 50;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }
}
