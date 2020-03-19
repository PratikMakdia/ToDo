package com.example.to_do;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.model.SubTaskListAdapter;

public class subTaskActivity extends AppCompatActivity {
    private RecyclerView rvShowSubTask;
    private SubTaskListAdapter subTaskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task_show);
        rvShowSubTask = findViewById(R.id.rvSubRecyclerView);
        /*subTaskListAdapter = new SubTaskListAdapter(this,this);
        rvShowSubTask.setAdapter(subTaskListAdapter);
        rvShowSubTask.setLayoutManager(new LinearLayoutManager(this));


        SubTaskViewModel subTaskViewModel = ViewModelProviders.of(this).get(SubTaskViewModel.class);
        subTaskViewModel.getAllSubNote().observe(this, new Observer<List<SubTask>>() {
            @Override
            public void onChanged(List<SubTask> subTasks) {

                subTaskListAdapter.setNotes(subTasks);
            }
        });


*/
    }
}
