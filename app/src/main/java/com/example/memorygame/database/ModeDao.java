package com.example.memorygame.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ModeDao {
    @Query("SELECT * FROM MODE")
    public List<Mode> getAll();

    @Query("SELECT * FROM MODE WHERE id = :id LIMIT 1")
    public Mode getById(int id);

    @Insert(onConflict = REPLACE)
    public void insert(Mode mode);

    @Delete
    public void delete(Mode mode);

    @Query("DELETE FROM MODE")
    public void deleteAll();
}
