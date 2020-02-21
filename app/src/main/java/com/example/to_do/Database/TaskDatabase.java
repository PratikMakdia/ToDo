package com.example.to_do.Database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class,SubTask.class},version =1 ,exportSchema = true)

public abstract class TaskDatabase extends RoomDatabase {
    public  abstract TaskDao taskDao();
    public abstract SubTaskDao subTaskDao();
    private static volatile TaskDatabase taskRoomInstance;

    static TaskDatabase getDatabase(final Context context)
    {
        if(taskRoomInstance==null)
        {
            synchronized (TaskDatabase.class)
            {
                if(taskRoomInstance==null)
                {
                    taskRoomInstance= Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,"TaskDB").fallbackToDestructiveMigration().build();
                }
            }
        }
            return taskRoomInstance;
    }
}
