package com.example.to_do.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.to_do.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllNotes();

    @Query("SELECT * FROM tasks WHERE id=:noteId")
    LiveData<Task> getNote(int noteId);


    @Query("SELECT * from tasks WHERE name=:taskName")
    LiveData<Task> GetId(String taskName);

    @Delete
    int delete(Task task);


    @Query("SELECT * FROM tasks ORDER BY Date ASC")
    LiveData<List<Task>> getAllNotesByDate();


    @Update
    void updateName(Task task);


}
