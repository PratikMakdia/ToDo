package com.example.to_do;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_do.Database.SubTask;
import com.example.to_do.Database.SubTaskViewMOdel;

import java.util.Calendar;
import java.util.Objects;

public class AddSubTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private SubTaskViewMOdel subTaskViewMOdel;
    Button btnsubadd;
    public  EditText edsubtitle,edsubdesc,edsubdatettime;
    private int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_sub_task);

        initialize();
        setOnClickListener();
    }



    private void initialize() {
        subTaskViewMOdel=new SubTaskViewMOdel((Application) getApplicationContext());
        edsubtitle=findViewById(R.id.show_sub_title);
        edsubdesc=findViewById(R.id.sub_details);
        edsubdatettime=findViewById(R.id.sub_date_time);
        btnsubadd= findViewById(R.id.btnsubadd);
     /*   Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noteId = bundle.getInt("note_id");
        }
        LiveData<Task> note = SubTaskViewMOdel.getNote(noteId);*/

    }

    private void setOnClickListener() {
        edsubdatettime.setOnClickListener(this);
        btnsubadd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sub_date_time:
                showDateTime();
                break;

            case R.id.btnsubadd:
                addSubData();
                break;

        }

    }


    private void addSubData() {
            Intent resultintent = new Intent();
            if (TextUtils.isEmpty(edsubtitle.getText()) || TextUtils.isEmpty(edsubdesc.getText()) || TextUtils.isEmpty(edsubdatettime.getText())) {
                setResult(RESULT_CANCELED, resultintent);
            } else {

                SubTask subTask;

                String mnote = edsubtitle.getText().toString();
                String mdesc = edsubdesc.getText().toString();
                String mtime = edsubdatettime.getText().toString();
                setResult(RESULT_OK, resultintent);

                if(TextUtils.isEmpty(mdesc)&& TextUtils.isEmpty(mtime))
                {
                    subTask = new SubTask(mnote);
                    subTaskViewMOdel.insert(subTask);
                }
                else {
                    subTask = new SubTask(mnote, mdesc, mtime);
                    subTaskViewMOdel.insert(subTask);
                }

                subTaskViewMOdel.insert(subTask);
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);


            }
            finish();
        }




    private void showDateTime() {

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog= new DatePickerDialog(AddSubTaskActivity.this,this,year,month,day);
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar= Calendar.getInstance();
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddSubTaskActivity.this,this,hour,minute, DateFormat.is24HourFormat(this));
        edsubdatettime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        edsubdatettime.setText(edsubdatettime.getText() + " -" + hourOfDay + ":" + minute);

    }

}
