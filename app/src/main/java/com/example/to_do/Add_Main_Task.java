package com.example.to_do;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.to_do.Database.Task;
import com.example.to_do.Database.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class Add_Main_Task extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private EditText edadd, eddesc, eddatetime;

   private FloatingActionButton fab;
    private Button btnadd;
    private ViewModel viewModel;
    Notification note;
    NotificationManager mgr;
    private TextView addsub;
    static final String channel_id="1001";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add__main__task);

        initialize();


        edittextEmptyornot();



        setOnClickListener();
    }

    private void edittextEmptyornot() {
       if(edadd.getText().toString().equals(""))
        {
            addsub.setEnabled(false);
        }
        edadd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(edadd.getText().toString().equals("")){
                    addsub.setEnabled(false);
                } else {
                    addsub.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(edadd.getText().toString().equals("")){
                    fab.setEnabled(false);
                } else {
                    fab.setEnabled(true);
                }

            }
        });

    }

    /**
     * for Initialize Variable
     */
    private void initialize() {
        edadd = findViewById(R.id.show_title);
        eddesc = findViewById(R.id.details);
        eddatetime =findViewById(R.id.date_time);
        btnadd =  findViewById(R.id.btnmainadd);
        addsub=findViewById(R.id.txaddsubtask);
        viewModel= ViewModelProviders.of(this).get(ViewModel.class);
       /* fab = findViewById(R.id.fab);*/

    }




    /**
     * for  click of EditText and Button
     */
    private void setOnClickListener() {

        eddatetime.setOnClickListener(this);


        btnadd.setOnClickListener(this);

        addsub.setOnClickListener(this);

    }


    /**
     * for Navigate to Sub Task Screen
     */
    private void navigateToSubTaskScreen() {

        Intent i = new Intent(getApplicationContext(), AddSubTaskActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.date_time:
                showDateTime();
                break;
            case R.id.txaddsubtask:
                navigateToSubTaskScreen();
                break;
            case R.id.btnmainadd:
                Add_Data();
               /* createnote();*/
                break;
        }
    }


    private void Add_Data() {
        Intent resultintent = new Intent();
        if (TextUtils.isEmpty(edadd.getText())) {
            setResult(RESULT_CANCELED, resultintent);
        } else {
            Task task;

            String note = edadd.getText().toString();
            String desc = eddesc.getText().toString();
            String time = eddatetime.getText().toString();
            setResult(RESULT_OK, resultintent);
            if(TextUtils.isEmpty(desc)||TextUtils.isEmpty(time))
            {
                task = new Task(note);
                viewModel.insert(task);
                int mid=task.getId();
                String id=String.valueOf(mid);
                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();

            }
            else {
                task = new Task(note, desc, time);
                viewModel.insert(task);
                int mid=task.getId();
                String id=String.valueOf(mid);
                Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
            }

        }
        finish();
    }


    private void showDateTime() {

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog= new DatePickerDialog(Add_Main_Task.this,this,year,month,day);
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar= Calendar.getInstance();
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(Add_Main_Task.this,this,hour,minute, DateFormat.is24HourFormat(this));
        eddatetime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        eddatetime.setText(eddatetime.getText() + " -" + hourOfDay + ":" + minute);

    }



   /* public void createnote() {
        mgr=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i2= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:5556"));
        PendingIntent pi2= PendingIntent.getActivity(this,0,i2,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("Open ToDO APp");
        builder.setSmallIcon(android.R.drawable.ic_menu_call);
        builder.setContentText("");
        builder.addAction(R.drawable.ic_done_black_24dp,"Complete Task",pi2);
        builder.setContentIntent(pi2);
        builder.setAutoCancel(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new
                    NotificationChannel(channel_id,"Hello",NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channel_id);
            mgr.createNotificationChannel(channel);
        }
        mgr.notify(1,builder.build());
    }
*/
}
