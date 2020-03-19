package com.example.to_do;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Database.SubTaskViewModel;
import com.example.to_do.Database.ViewModel;
import com.example.to_do.model.SubTaskListAdapter;
import com.example.to_do.model.Task;
import com.example.to_do.model.TaskListAdapter;
import com.example.to_do.model.TaskListAdapter.OnDeleteClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnDeleteClickListener, PopupMenu.OnMenuItemClickListener {



   private FirebaseAuth firebaseAuth;
    private ImageView ivMenuOrder;
    private RecyclerView rvShowMainTask;
    private ViewModel viewModel;
    private SubTaskViewModel subTaskViewModel;
    private TaskListAdapter taskListAdapter;
    private ImageView ivLogout;
    private FloatingActionButton fbtnAddMainTask;
    private AlarmReceiver mAlarmReceiver;
    private  RecyclerView rvShowSubTask;
    private SubTaskListAdapter subTaskListAdapter;
    private  int STORAGE_PERMISSION_CODE=1;
    private CardView cvSubTaskShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        ivLogout = findViewById(R.id.ivLogout);
        fbtnAddMainTask = findViewById(R.id.fabAddMainTask);

        ivMenuOrder =findViewById(R.id.ivMenuSortOrder);
        rvShowMainTask = findViewById(R.id.rvShowTasks);
//
//        cvSubTaskShow=findViewById(R.id.cvSubTask);

//        rvShowSubTask=findViewById(R.id.rvSubRecyclerView);
       // rvShowSubTask = findViewById(R.id.rvSubTasksShow);

        taskListAdapter = new TaskListAdapter(this, this);
        rvShowMainTask.setAdapter(taskListAdapter);
        rvShowMainTask.setLayoutManager(new LinearLayoutManager(this));

      /*  subTaskListAdapter = new SubTaskListAdapter(this);
        rvShowSubTask.setAdapter(subTaskListAdapter);
        rvShowSubTask.setLayoutManager(new LinearLayoutManager(this));*/




        rvShowMainTask.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    fbtnAddMainTask.show();
                    // Recycle view scrolling up...

                } else if (dy > 0) {
                    // Recycle view scrolling down...x
                    fbtnAddMainTask.show();
                }
            }
        });


            getAllNoteObserver();


    }

    /**
     * for LiveData Observer  when MainActivity is launch
     */
    private void getAllNoteObserver() {
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);


        viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {

            @Override
            public void onChanged(List<Task> tasks) {

                taskListAdapter.setNotes(tasks);
            }
        });
       /* subTaskViewModel=ViewModelProviders.of(this).get(SubTaskViewModel.class);
        subTaskViewModel.getAllSubNote().observe(this, new Observer<List<SubTask>>() {
            @Override
            public void onChanged(List<SubTask> subTasks) {

                subTaskListAdapter.setNotes(subTasks);
            }
        });
*/
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
           /* String abc= String.valueOf(startposition);
            Toast.makeText(getApplicationContext(),abc,Toast.LENGTH_SHORT).show();
            String xyz= String.valueOf(endposition);
            Toast.makeText(getApplicationContext(),xyz,Toast.LENGTH_SHORT).show();*/

            return true;
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

            navigateToLoginScreen();

        }
    }

    /**
     * for Navigate to login Screen
     */
    private void navigateToLoginScreen() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    /**
     * for  login on click of button
     */
    private void setOnClickListener() {
        ivLogout.setOnClickListener(this);
        fbtnAddMainTask.setOnClickListener(this);
        ivMenuOrder.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLogout:
                logoutAlertDialog();
                break;

            case R.id.fabAddMainTask:
                navigateToAddMainTask();
                break;

            case R.id.ivMenuSortOrder:
                showPopupMenu();
                break;



        }
    }

    /**
     * for Show Popup Menu
     */
    private void showPopupMenu() {

        PopupMenu popup = new PopupMenu(MainActivity.this, ivMenuOrder);
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
        else
        {
            requestStorgaePermission();
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

    private void requestStorgaePermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {

        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }


    @Override
    public void onDeleteClickListener(Task myTask) {

/*
        Bundle bundle = getIntent().getExtras();
        int  noteId = 0;
        if (bundle != null) {
            noteId = bundle.getInt("note_id");


        }*/
      /*  Intent mainIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        int uniqueNotificationId = mainIntent.getIntExtra("GenerateNotificationId",0 );
        //  int uniqueNotificationId=AddMainTaskActivity.getUniqueNotificationId();
        AlarmReceiver.cancelNotification(getApplicationContext(),uniqueNotificationId);*/
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
        alertDialog.setMessage(R.string.are_you_sure_want_to_exit_message);
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


                viewModel.getAllNotesByDate().observe(this, new Observer<List<Task>>() {

                    @Override
                    public void onChanged(List<Task> tasks) {
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                        itemTouchHelper.attachToRecyclerView(rvShowMainTask);
                        //cvSubTaskShow.setVisibility(View.GONE);
                        taskListAdapter.setNotes(tasks);
                    }
                });
                break;
            case R.id.myorder:

                viewModel.getAllNotes().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(final List<Task> tasks) {

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(rvShowMainTask);
                        //cvSubTaskShow.setVisibility(View.VISIBLE);
                        taskListAdapter.setNotes(tasks);

                    }
                });
                break;
        }
        return false;
    }





}




