package com.example.to_do;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Database.SubTask;
import com.example.to_do.Database.Task;
import com.example.to_do.Database.TaskListAdapter;
import com.example.to_do.Database.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TaskListAdapter.OnDeleteClickListener, PopupMenu.OnMenuItemClickListener {


    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;
    private static final int NEW_ACTIVITY_REQUEST_CODE = 1;
    FirebaseAuth firebaseAuth;
    private ImageView popupmenu;

    TextView txnote, txmytasks;
    RecyclerView recyclerView;
    Button btnupdate;

    // private String TAG = this.getClass().getSimpleName();
    private ViewModel viewModel;
    private TaskListAdapter taskListAdapter;
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
        btnupdate=findViewById(R.id.btnmainupdate);
        popupmenu=findViewById(R.id.popup_order);
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


            getallnoteObserver();


    }

    /**
     * for show task when Mainactivity is launch
     */
    private void getallnoteObserver() {
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {


                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                taskListAdapter.setNotes(tasks);
                //taskListAdapter.setSubNotes(subTasks);


            }
        });

        viewModel.getAllSubNotes().observe(this, new Observer<List<SubTask>>() {
            @Override
            public void onChanged(List<SubTask> subTasks) {
                taskListAdapter.setSubNotes(subTasks);
            }
        });
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |ItemTouchHelper.START|ItemTouchHelper.END, 0) {


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

            String abc= String.valueOf(startposition);
            Toast.makeText(getApplicationContext(),abc,Toast.LENGTH_SHORT).show();
            String xyz= String.valueOf(endposition);
            Toast.makeText(getApplicationContext(),xyz,Toast.LENGTH_SHORT).show();



            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };




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
        popupmenu.setOnClickListener(this);




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

            case R.id.popup_order:
                showPopupMenu();
                break;



        }
    }

    /**
     * for Show Popup Menu
     */
    private void showPopupMenu() {

        PopupMenu popup = new PopupMenu(MainActivity.this, popupmenu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_sort__menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);

        popup.show(); //showing popup menu
    }



    /**
     * for Navigate to Main Task Screen
     */
    private void navigateToAddMainTask() {


        Intent i = new Intent(MainActivity.this, AddMainTaskActivity.class);
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
        exitDialog();

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

    private void exitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Confirm Exit ???");
        alertDialog.setMessage("Are you sure you want to Exit ?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();


            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bydate:
                viewModel.getmAllNotesByDate().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {

                        taskListAdapter.setNotes(tasks);

                    }
                });

                break;
            case R.id.myorder:

                viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {



                        taskListAdapter.setNotes(tasks);


                    }
                });
                break;


        }
        return false;
    }


}




