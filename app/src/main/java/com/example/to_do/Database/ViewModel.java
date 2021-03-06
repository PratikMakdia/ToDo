package com.example.to_do.Database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.to_do.model.SubTask;
import com.example.to_do.model.Task;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private TaskDao taskDao;
    private SubTaskDao subTaskDao;

    private LiveData<List<Task>> mAllNotes;
    private LiveData<List<Task>> mAllNotesByDate;

    public ViewModel(Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);
        taskDao = taskDB.taskDao();
        subTaskDao = taskDB.subTaskDao();
        mAllNotes = taskDao.getAllNotes();
        mAllNotesByDate = taskDao.getAllNotesByDate();


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

    public void delete(Task task)

    {
        new DeleteAsyncTask(taskDao).execute(task);
    }

    public LiveData<List<Task>> getAllNotesByDate() {

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


    public LiveData<SubTask> getSubNote(int noteId) {

        return subTaskDao.getSubNote(noteId);
    }



    public LiveData<Task> mainTaskId(String taskName) {
        return taskDao.GetId(taskName);
    }
    @SuppressLint("StaticFieldLeak")
    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

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
    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
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
    private static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {
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
