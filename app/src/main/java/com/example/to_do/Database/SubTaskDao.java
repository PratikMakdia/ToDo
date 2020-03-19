package com.example.to_do.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.to_do.model.SubTask;

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

    @Query("SELECT * FROM SubTask WHERE subId=:noteId")
    LiveData<SubTask> getSubNote(int noteId);


    @Query("SELECT * from subtask INNER JOIN tasks ON id=SubTask.taskId")
    LiveData<List<SubTask>> getForeignKeyId();

}

