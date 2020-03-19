package com.example.to_do.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.to_do.model.SubTask;

import java.util.List;

public class SubTaskViewModel extends AndroidViewModel {

    private static SubTaskDao subTaskDao;
   // private String TAG = this.getClass().getSimpleName();

    private LiveData<List<SubTask>> mAllSubNote;
    private LiveData<List<SubTask>> getForeignKey;


    public SubTaskViewModel(@NonNull Application application) {
        super(application);

        TaskDatabase taskDB = TaskDatabase.getDatabase(application);
        subTaskDao = taskDB.subTaskDao();
        mAllSubNote = subTaskDao.getAllSubNotes();
        getForeignKey=subTaskDao.getForeignKeyId();

    }

    public LiveData<List<SubTask>> getAllSubNote() {
        return mAllSubNote;
    }


    public LiveData<List<SubTask>> GetForeignKeyId(){
        return  getForeignKey;
    }


   /* public LiveData<SubTask> getSubNote(int noteId) {

        return subTaskDao.getSubNote(noteId);
    }
*/

    public void insert(SubTask subTasks) {
        new InsertAsyncTask(subTaskDao).execute(subTasks);
    }

    public void update(SubTask subTasks) {
        new UpdateAsyncTask(subTaskDao).execute(subTasks);

    }

    public void delete(SubTask subTasks) {
        new DeleteAsyncTask(subTaskDao).execute(subTasks);
    }

    private static class InsertAsyncTask extends AsyncTask<SubTask, Void, Void> {

        SubTaskDao mSubTaskDao;


        InsertAsyncTask(SubTaskDao subTaskDao) {
            this.mSubTaskDao = subTaskDao;
        }

        @Override
        protected Void doInBackground(SubTask... subTasks) {
            mSubTaskDao.insert(subTasks[0]);
            return null;
        }


    }

    private static class UpdateAsyncTask extends AsyncTask<SubTask, Void, Void> {
        SubTaskDao mSubTaskDao;

        UpdateAsyncTask(SubTaskDao subTaskDao) {
            this.mSubTaskDao = subTaskDao;
        }

        @Override
        protected Void doInBackground(SubTask... subTasks) {
            mSubTaskDao.update(subTasks[0]);
            return null;
        }

    }

    private static class DeleteAsyncTask extends AsyncTask<SubTask, Void, Void> {
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

