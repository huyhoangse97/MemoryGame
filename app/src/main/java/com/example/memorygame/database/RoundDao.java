package com.example.memorygame.database;

import android.icu.text.Replaceable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RoundDao {
    @Query("SELECT * FROM ROUND")
    public List<Round> getAll();

    @Query("SELECT * FROM ROUND WHERE modeName = :modeName AND roundName = :roundName LIMIT 1")
    public Round getByModeAndRoundName(String modeName, String roundName);

    @Query("SELECT * FROM ROUND WHERE modeId = :modeId AND roundId = :roundId LIMIT 1")
    Round getByModeAndRoundId(int modeId, int roundId);

    @Query("SELECT * FROM ROUND WHERE modeId = :modeId")
    public List<Round> getByModeId(int modeId);

    @Insert(onConflict = REPLACE)
    public void insert(Round round);

    @Delete
    public void delete(Round round);

    @Query("DELETE FROM ROUND")
    public void deleteAll();

    @Update
    void update(Round round);
}
