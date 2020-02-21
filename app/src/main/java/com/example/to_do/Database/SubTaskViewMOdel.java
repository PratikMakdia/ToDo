package com.example.to_do.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubTaskViewMOdel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private TaskDao taskDao;

    private LiveData<List<SubTask>> mAllNote;
    private static SubTaskDao subTaskDao;


    public SubTaskViewMOdel(@NonNull Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);

        subTaskDao = taskDB.subTaskDao();

    }

    public void insert(SubTask subtasks) {
        new InsertAsyncTask(subTaskDao).execute(subtasks);
    }


    private class InsertAsyncTask extends AsyncTask<SubTask, Void, Void> {


        SubTaskDao mSubTaskDao;


        InsertAsyncTask(SubTaskDao subTaskDao) {
            this.mSubTaskDao = subTaskDao;
        }

        @Override
        protected Void doInBackground(SubTask... subtasks) {
            mSubTaskDao.insert(subtasks[0]);
            return null;
        }


    }
  /*  public static LiveData<Task> getNote(int noteId) {

        return subTaskDao.getNote(noteId);
    }*/
}

