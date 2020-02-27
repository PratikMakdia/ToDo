package com.example.to_do.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.AddMainTaskActivity;
import com.example.to_do.AddSubTaskActivity;
import com.example.to_do.MainActivity;
import com.example.to_do.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> implements List<Object> {

    private List<Task> mtaskes;
    private List<SubTask> msubtaskes;
    private LayoutInflater layoutInflater;
    private Context mcontext;
    private OnDeleteClickListener onDeleteClickListener;
    private CardView cardView,subcardVIew;


    public TaskListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mcontext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = layoutInflater.inflate(R.layout.list_item, parent, false);
        TaskViewHolder taskholder = new TaskViewHolder(itemview);

        return taskholder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        if (mtaskes != null) {
            Task task = mtaskes.get(position);
            holder.setData(task.getName(), position);
            holder.setListners();

        } else {
            // Covers the case of data not being ready yet.
            holder.TaskItemView.setText(R.string.no_note);
        }



        if(msubtaskes!=null)
        {
            SubTask subTaskask = msubtaskes.get(position);
            holder.setSubData(subTaskask.getSub_name(), position);
            holder.setListners();

        }
    }

    public void setIndexInDatabase() {
        TaskDao taskDao = TaskDatabase.getDatabase(mcontext).taskDao();
        SubTaskDao subTaskDao=TaskDatabase.getDatabase(mcontext).subTaskDao();
        for (Task task : mtaskes) {
            task.setOrder(mtaskes.indexOf(task));
            taskDao.insert(task);
        }


    }

    @Override
    public int getItemCount() {
        if (mtaskes != null)
            return mtaskes.size();

        if(msubtaskes!=null)
            return msubtaskes.size();
        return 0;
    }

    public void setNotes(List<Task> tasks) {
        mtaskes = tasks;
        notifyDataSetChanged();
    }

    public void setSubNotes(List<SubTask> subTasks)
    {
        msubtaskes=subTasks;
        notifyDataSetChanged();

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<Object> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Object> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return null;
    }

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Task myTask);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {


        private TextView TaskItemView,SubTaskItemVIew;
        private ImageView imgDelete, subimgdelete;
        private int mPosition;


        public TaskViewHolder(View itemView) {
            super(itemView);

            TaskItemView = itemView.findViewById(R.id.txvNote);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);

            cardView = itemView.findViewById(R.id.cardview);
            subcardVIew=itemView.findViewById(R.id.subcardview);
            subimgdelete=itemView.findViewById(R.id.subivRowDelete);
            SubTaskItemVIew=itemView.findViewById(R.id.subtxNote);
        }

        public void setData(String note, int position) {
            TaskItemView.setText(note);
            mPosition = position;
        }

        public void setSubData(String note, int position) {
            SubTaskItemVIew.setText(note);
            mPosition = position;
        }


        public void setListners() {


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, AddMainTaskActivity.class);
                    intent.putExtra("note_id", mtaskes.get(mPosition).getId());
                    ((Activity) mcontext).startActivityForResult(intent, MainActivity.UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });

            subcardVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, AddSubTaskActivity.class);
                    intent.putExtra("note_id", msubtaskes.get(mPosition).getId());
                    ((Activity) mcontext).startActivityForResult(intent, MainActivity.UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });




            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(mtaskes.get(mPosition));
                    }
                }
            });
        }
    }

}