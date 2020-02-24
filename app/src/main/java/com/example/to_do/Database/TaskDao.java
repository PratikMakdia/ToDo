package com.example.to_do.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllNotes();

    @Query("SELECT * FROM tasks WHERE id=:noteId")
    LiveData<Task> getNote(int noteId);

    @Update
    void update(Task task);
    @Delete
    int delete(Task task);

    @Query("SELECT MAX(word_order) FROM tasks")
    int getLargestOrder();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<Task> wordEntities);

    @Query("SELECT * FROM tasks ORDER BY word_order ASC")
    LiveData<List<Task>> sortByOrder();

    @Update
    void updateName(Task task);


}
