package com.example.to_do.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Add_Main_Task;
import com.example.to_do.MainActivity;
import com.example.to_do.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> implements List<Object> {

    private List<Task> mtaskes;
    private LayoutInflater layoutInflater;
    private Context mcontext;
    private OnDeleteClickListener onDeleteClickListener;


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
    }

    public void setIndexInDatabase() {
        TaskDao taskDao = TaskDatabase.getDatabase(mcontext).taskDao();
        for (Task task : mtaskes) {
            task.setOrder(mtaskes.indexOf(task));
            taskDao.insert(task);
        }


    }

    @Override
    public int getItemCount() {
        if (mtaskes != null)
            return mtaskes.size();
        return 0;
    }

    public void setNotes(List<Task> tasks) {
        mtaskes = tasks;
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


        private TextView TaskItemView;
        private ImageView imgDelete, imgEdit;
        private int mPosition;
        private Button btnupdate, btnadd;

        public TaskViewHolder(View itemView) {
            super(itemView);

            TaskItemView = itemView.findViewById(R.id.txvNote);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);
            imgEdit = itemView.findViewById(R.id.ivRowEdit);
            btnadd = itemView.findViewById(R.id.btnmainadd);
            btnupdate = itemView.findViewById(R.id.btnmainupdate);

        }

        public void setData(String note, int position) {
            TaskItemView.setText(note);

            mPosition = position;
        }

        public void setListners() {
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(mcontext, Add_Main_Task.class);
                    intent.putExtra("note_id", mtaskes.get(mPosition).getId());
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