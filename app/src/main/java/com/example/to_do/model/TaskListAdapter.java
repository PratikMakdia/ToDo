package com.example.to_do.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.AddMainTaskActivity;
import com.example.to_do.Database.SubTaskListAdapter;
import com.example.to_do.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> implements List<Object> {

    private List<Task> mTaskes;

    private LayoutInflater layoutInflater;
    private Context mcontext;
    private OnDeleteClickListener onDeleteClickListener;
    private RecyclerView rvSubTAsk;
    private SubTaskListAdapter subTaskListAdapter;


    public TaskListAdapter(Context context, OnDeleteClickListener listener) {
//        rvSubTAsk.setVisibility(View.VISIBLE);
        layoutInflater = LayoutInflater.from(context);
        mcontext = context;
        this.onDeleteClickListener = listener;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        if (mTaskes != null) {
            Task task = mTaskes.get(position);
            holder.setData(task.getName(), position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  Intent intent = new Intent(mcontext, AddMainTaskActivity.class);
                    intent.putExtra("note_id", mTaskes.get(position).getId());
                    mcontext.startActivity(intent);

                }
            });


            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClickListener(mTaskes.get(position));
                    }
                }
            });

        } else {
            // Covers the case of data not being ready yet.
            Toast.makeText(mcontext,R.string.no_note,Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public int getItemCount() {
        if (mTaskes != null)
            return mTaskes.size();

        return 0;
    }

    public void setNotes(List<Task> tasks) {
        mTaskes = tasks;
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
        void onDeleteClickListener(Task myTask);


    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {


        private TextView taskItemView;
        private ImageView imgDelete,imgEdit;
        private int mPosition;
        private CheckBox chkMainTask;

        private RecyclerView rvSubTAsk;

        public TaskViewHolder(View itemView) {
            super(itemView);

            chkMainTask = itemView.findViewById(R.id.chkMainTask);
            taskItemView = itemView.findViewById(R.id.txvNote);
            imgEdit=itemView.findViewById(R.id.ivRowEdit);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);
            rvSubTAsk=itemView.findViewById(R.id.rvSubRecyclerView);

        }

        private void setData(String note, int position) {
            chkMainTask.setText(note);
            //TaskItemView.setText(note);
            mPosition = position;
            chkMainTask.setOnCheckedChangeListener(this);
        }



        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                chkMainTask.setPaintFlags(chkMainTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            } else {
                chkMainTask.setPaintFlags(0);

            }
        }

    }







}