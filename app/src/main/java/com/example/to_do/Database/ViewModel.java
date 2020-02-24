package com.example.to_do.Database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private TaskDao taskDao;
    private LiveData<List<Task>> mAllNotes;

    public ViewModel(Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);
        taskDao = taskDB.taskDao();
        mAllNotes = taskDao.getAllNotes();
    }

    public void insert(Task task) {
        new InsertAsyncTask(taskDao).execute(task);
    }


    public LiveData<List<Task>> getAllNotes() {
        return mAllNotes;
    }

    public void updateName(Task task) {
        new UpdateAsyncTask(taskDao).execute(task);

    }

    public void delete(Task task) {
        new DeleteAsyncTask(taskDao).execute(task);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyrd");
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

        TaskDao mTaskDao;

        InsertAsyncTask(TaskDao mTaskDao) {
            this.mTaskDao = mTaskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.insert(tasks[0]);
            return null;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        TaskDao mTaskDao;

        UpdateAsyncTask(TaskDao mTaskDao) {
            this.mTaskDao = mTaskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.updateName(tasks[0]);
            return null;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {
        TaskDao mTaskDao;

        DeleteAsyncTask(TaskDao mTaskDao) {
            this.mTaskDao = mTaskDao;
        }


        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.delete(tasks[0]);
            return null;
        }
    }

    public LiveData<Task> getNote(int noteId) {

        return taskDao.getNote(noteId);
    }
}
