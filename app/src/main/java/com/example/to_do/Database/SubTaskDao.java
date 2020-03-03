package com.example.to_do.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubTaskDao {


    @Insert
    void insert(SubTask subtask);

    @Update
    void update(SubTask subtask);

    @Delete
    Void delete(SubTask subTask);

    @Query("SELECT * FROM SubTask")
    LiveData<List<SubTask>> getAllSubNotes();

    @Query("SELECT * FROM SubTask WHERE sub_id=:noteId")
    LiveData<SubTask> getSubNote(int noteId);










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

