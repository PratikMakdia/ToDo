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
    private SubTaskDao subTaskDao;
    private LiveData<List<SubTask>> mAllSubnotes;
    private LiveData<List<Task>> mAllNotes;
    private LiveData<List<Task>> mAllNotesByDate;

    public ViewModel(Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);
        taskDao = taskDB.taskDao();
        subTaskDao = taskDB.subTaskDao();
        mAllNotes = taskDao.getAllNotes();
        mAllNotesByDate = taskDao.getAllNotesByDate();

        mAllSubnotes =subTaskDao.getAllSubNotes();
    }

    public void insert(Task task) {
        new InsertAsyncTask(taskDao).execute(task);
    }


    public LiveData<List<Task>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<List<SubTask>> getAllSubNotes() {
        return mAllSubnotes;
    }



    public void updateName(Task task) {
        new UpdateAsyncTask(taskDao).execute(task);

    }

    public void delete(Task task)
    {
        new DeleteAsyncTask(taskDao).execute(task);
    }

    public LiveData<List<Task>> getmAllNotesByDate() {

        return mAllNotesByDate;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyrd");
    }

    public LiveData<Task> getNote(int noteId) {

        return taskDao.getNote(noteId);
    }


    public LiveData<SubTask> getsubNote(int noteId) {

        return subTaskDao.getSubNote(noteId);
    }
    public int maintaskid(String taskname) {
        return taskDao.GetId(taskname);
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
}
