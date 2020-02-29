package com.example.to_do.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.R;

import java.util.List;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.SubViewHolder> {

    private LayoutInflater sublayoutInflater;
    private Context mcontext;

    private List<SubTask> msubtaskes;


    public SubTaskListAdapter(Context context) {
        sublayoutInflater = LayoutInflater.from(context);
        mcontext = context;

    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup child, int viewType) {

        View subitemview = sublayoutInflater.inflate(R.layout.sub_list, child, false);
        SubViewHolder subTaskViewHolder=  new SubViewHolder(subitemview);
        return subTaskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        if(msubtaskes!=null)
        {
            SubTask subTaskask = msubtaskes.get(position);
            holder.setSubData(subTaskask.getSub_name(), position);


        }

    }


    @Override
    public int getItemCount() {
        if(msubtaskes!=null)
            return msubtaskes.size();
        return 0;
    }

    public void setSubNotes(List<SubTask> subTasks)
    {
        msubtaskes=subTasks;
        notifyDataSetChanged();

    }


    public class SubViewHolder extends RecyclerView.ViewHolder {

        private TextView SubTaskItemVIew;
        private ImageView imgDelete, subimgdelete;

        private int msubPosition;

        public SubViewHolder(@NonNull View itemView) {

            super(itemView);

            SubTaskItemVIew=itemView.findViewById(R.id.subtxNote);

        }

        public void setSubData(String sub_name, int position) {
            SubTaskItemVIew.setText(sub_name);
            msubPosition = position;
        }
    }
}
