package com.example.to_do;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Database.Task;
import com.example.to_do.Database.TaskListAdapter;
import com.example.to_do.Database.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TaskListAdapter.OnDeleteClickListener {


    private static final int NEW_ACTIVITY_REQUEST_CODE = 1;


    FirebaseAuth firebaseAuth;

    TextView txnote, txmytasks;
    RecyclerView recyclerView;


   // private String TAG = this.getClass().getSimpleName();
    private ViewModel viewModel;
    private TaskListAdapter taskListAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int startposition = viewHolder.getAdapterPosition();
            int endposition = target.getAdapterPosition();

            if (startposition < endposition) {
                for (int i = startposition; i < endposition; i++) {
                    Collections.swap(taskListAdapter, i, i + 1);
                }

            } else {
                for (int i = startposition; i > endposition; i--) {
                    Collections.swap(taskListAdapter, i, i - 1);
                }
            }
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(startposition, endposition);


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    private ImageView imglogout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        initVariables();
        userornot();

        setOnClickListener();
    }

    /**
     * for initialize variable
     */
    @SuppressLint("SetTextI18n")
    public void initVariables() {

        firebaseAuth = FirebaseAuth.getInstance();

        txnote = findViewById(R.id.txvNote);
        txmytasks = findViewById(R.id.my_tasks);

        imglogout = findViewById(R.id.iglogout);
        fab = findViewById(R.id.floataddmain);

        recyclerView = findViewById(R.id.recyclerview);
        taskListAdapter = new TaskListAdapter(this, this);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    fab.show();
                    // Recycle view scrolling up...

                } else if (dy > 0) {
                    // Recycle view scrolling down...x
                    fab.show();
                }
            }
        });
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {

                taskListAdapter.setNotes(tasks);
            }
        });
    }

    /**
     * for check firebase user Login  or not
     */
    private void userornot() {
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            navigateToLoginScreen();

        }
    }

    /**
     * for Navigate to login Screen
     */
    private void navigateToLoginScreen() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * for  login on click of button
     */
    private void setOnClickListener() {
        imglogout.setOnClickListener(this);
        fab.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iglogout:
                logalertdialog();
                break;

            case R.id.floataddmain:
                navigateToAddMainTask();
                break;


        }
    }

    /**
     * for Navigate to Main Task Screen
     */
    private void navigateToAddMainTask() {
        Intent i = new Intent(MainActivity.this, Add_Main_Task.class);
        startActivityForResult(i, NEW_ACTIVITY_REQUEST_CODE)
        ;
    }


    @Override
    public void OnDeleteClickListener(Task myTask) {
        viewModel.delete(myTask);

    }

    /**
     * from user back pressed
     */
    public void onBackPressed() {

        finish();
    }


    /**
     * For Logout Alert Dialog
     */
    private void logalertdialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Confirm Logout...");
        alertDialog.setMessage("Are you sure you want Logout?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                finish();
                navigateToLoginScreen();

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}




