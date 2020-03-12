package com.example.to_do;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Database.SubTaskViewMOdel;
import com.example.to_do.Database.Task;
import com.example.to_do.Database.TaskListAdapter;
import com.example.to_do.Database.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TaskListAdapter.OnDeleteClickListener, PopupMenu.OnMenuItemClickListener {



    FirebaseAuth firebaseAuth;
    private ImageView popupmenu;

    private TextView txnote;
    private RecyclerView recyclerView;
    private  Button btnUpdate;

    private SubTaskViewMOdel subTaskViewMOdel;

    private ViewModel viewModel;
    private TaskListAdapter taskListAdapter;
    private ImageView imglogout;
    private FloatingActionButton fab;
    private CardView cardView;

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

    public void initVariables() {

        firebaseAuth = FirebaseAuth.getInstance();

        TextView tvMytasks = findViewById(R.id.tvMyTasks);
        imglogout = findViewById(R.id.iglogout);
        fab = findViewById(R.id.floataddmain);
        btnUpdate =findViewById(R.id.btnmainupdate);
        popupmenu=findViewById(R.id.popup_order);
        recyclerView = findViewById(R.id.recyclerview);

        cardView = findViewById(R.id.cardview);
        taskListAdapter = new TaskListAdapter(this, this);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
        subTaskViewMOdel=ViewModelProviders.of(this).get(SubTaskViewMOdel.class);

        viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {

            @Override
            public void onChanged(List<Task> tasks) {

                taskListAdapter.setNotes(tasks);

            }
        });

    }


   final  ItemTouchHelper.Callback callback= new ItemTouchHelper.Callback() {
        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return 0;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

   final  ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |ItemTouchHelper.START|ItemTouchHelper.END, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int startposition = viewHolder.getAdapterPosition();
            int endposition = target.getAdapterPosition();

            if (startposition < endposition) {
                for (int i = startposition; i < endposition; i++) {
                    Collections.swap(taskListAdapter, i, i + 1);
                }
            }

            else {
                for (int i = startposition; i > endposition; i--) {
                    Collections.swap(taskListAdapter, i, i - 1);

                }
            }
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(startposition, endposition);

           /* Task task;
            task= new Task(endposition);
            viewModel.insert(task);*/
            String abc= String.valueOf(startposition);
            Toast.makeText(getApplicationContext(),abc,Toast.LENGTH_SHORT).show();
            String xyz= String.valueOf(endposition);
            Toast.makeText(getApplicationContext(),xyz,Toast.LENGTH_SHORT).show();

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);

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
        popupmenu.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iglogout:
                logoutAlertDialog();
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

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent i = new Intent(MainActivity.this, AddMainTaskActivity.class);
            startActivity(i);
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int STORAGE_PERMISSION_CODE = 1;
        if(requestCode== STORAGE_PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else

            {

                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onDeleteClickListener(Task myTask) {
        viewModel.delete(myTask);

    }


    /**
     * from user back pressed
     */
    public void onBackPressed() {
        exitAlertDialog();

    }


    /**
     * For Logout Alert Dialog
     */
    private void logoutAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(R.string.confirm_logout);
        alertDialog.setMessage(R.string.logout_message);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                finish();
                navigateToLoginScreen();

            }
        });

        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    /**
     * For Exit alert Dialog Box
     */
    private void exitAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(R.string.confirm_exit);
        alertDialog.setMessage(R.string.are_you_sure_want_to_exit_messeage);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();


            }
        });

        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                        taskListAdapter.setNotes(tasks);
                    }
                });
                break;
            case R.id.myorder:

                viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(final List<Task> tasks) {

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                        taskListAdapter.setNotes(tasks);

                    }
                });
                break;
        }
        return false;
    }





}




