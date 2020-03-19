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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.to_do.AddSubTaskActivity;
import com.example.to_do.R;

import java.util.List;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.SubViewHolder> {


    private List<SubTask> mSubTaskes;
    private Context mSubContext;
    private LayoutInflater subLayoutInflater;
    private OnDeleteClickListener onDeleteClickListener;



    public SubTaskListAdapter(Context context,OnDeleteClickListener subListener) {
        subLayoutInflater = LayoutInflater.from(context);
        mSubContext = subLayoutInflater.getContext();
        this.onDeleteClickListener = subListener;

    }



    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View subItemView = subLayoutInflater.inflate(R.layout.sub_list, parent, false);
        return new SubViewHolder(subItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder subViewHolder, final int subPosition) {
        if (mSubTaskes != null) {
            SubTask subTask = mSubTaskes.get(subPosition);
            subViewHolder.setData(subTask.getSub_name(), subPosition);



            subViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mSubContext, AddSubTaskActivity.class);
                    intent.putExtra("noteId", mSubTaskes.get(subPosition).getId());
                    mSubContext.startActivity(intent);
                }
            });

            subViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClickListener(mSubTaskes.get(subPosition));
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if (mSubTaskes != null)
            return mSubTaskes.size();

        return 0;
    }

    public void setNotes(List<SubTask> subTasks) {
        mSubTaskes = subTasks;
        notifyDataSetChanged();
    }
    public interface OnDeleteClickListener {
        void onDeleteClickListener(SubTask submyTask);


    }

    public static class SubViewHolder extends ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private CheckBox chkSubTask;
        private int mSubPosition;
        private ImageView imgDelete, imgEdit;


        private SubViewHolder(@NonNull View itemView) {
            super(itemView);

            chkSubTask = itemView.findViewById(R.id.chkSubTask);
            imgDelete = itemView.findViewById(R.id.ivSubRowDelete);
            imgEdit = itemView.findViewById(R.id.ivSubRowEdit);

        }

        private void setData(String note, int position) {

            chkSubTask.setText(note);
            //TaskItemView.setText(note);
            mSubPosition = position;
            chkSubTask.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                chkSubTask.setPaintFlags(chkSubTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            } else {
                chkSubTask.setPaintFlags(0);

            }
        }
    }
}

