package com.example.to_do.Database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface SubTaskDao {


    @Insert
    void insert(SubTask subtask);







/*
    void insert(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllNotes();

    @Query("SELECT * FROM tasks WHERE id=:noteId")
    LiveData<Task> getNote(int noteId);

    @Update
    void update(Task task);
    @Delete
    int delete(Task task);*/
}

