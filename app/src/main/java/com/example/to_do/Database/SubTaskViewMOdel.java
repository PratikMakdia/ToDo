package com.example.to_do.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SubTaskViewMOdel extends AndroidViewModel {

    private static SubTaskDao subTaskDao;
    private String TAG = this.getClass().getSimpleName();

    private LiveData<List<SubTask>> mAllsubNote;


    public SubTaskViewMOdel(@NonNull Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);
        subTaskDao = taskDB.subTaskDao();
        mAllsubNote=subTaskDao.getAllSubNotes();

    }

    public LiveData<List<SubTask>> getmAllsubNote()
    {
        return mAllsubNote;
    }

    public LiveData<SubTask> getsubNote(int noteId) {

        return subTaskDao.getSubNote(noteId);
    }



    public void insert(SubTask subtasks) {
        new InsertAsyncTask(subTaskDao).execute(subtasks);
    }

    public void update(SubTask subtasks) {
        new UpdateAsyncTask(subTaskDao).execute(subtasks);

    }

    public void delete(SubTask subTasks)
    {
        new DeleteAsyncTask(subTaskDao).execute(subTasks);
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

    private class UpdateAsyncTask extends AsyncTask<SubTask, Void, Void> {
        SubTaskDao mSubTaskDao;

        UpdateAsyncTask(SubTaskDao subTaskDao) {
            this.mSubTaskDao = subTaskDao;
        }

        @Override
        protected Void doInBackground(SubTask... subtasks) {
            mSubTaskDao.update(subtasks[0]);
            return null;
        }


    }

    private class DeleteAsyncTask extends AsyncTask<SubTask, Void, Void> {
        SubTaskDao mSubTaskDao;


        DeleteAsyncTask(SubTaskDao subTaskDao) {
            this.mSubTaskDao = subTaskDao;
        }


        @Override
        protected Void doInBackground(SubTask... subTasks) {
            mSubTaskDao.delete(subTasks[0]);
            return null;
        }
    }

  /*  public static LiveData<Task> getNote(int noteId) {

        return subTaskDao.getNote(noteId);
    }*/
}

