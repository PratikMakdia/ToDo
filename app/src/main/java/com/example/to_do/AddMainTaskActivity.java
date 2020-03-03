package com.example.to_do;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.to_do.Database.SubTask;
import com.example.to_do.Database.SubTaskViewMOdel;
import com.example.to_do.Database.Task;
import com.example.to_do.Database.TaskListAdapter;
import com.example.to_do.Database.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;

public class AddMainTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{



    private EditText edadd, eddesc, eddatetime;
    private TextView tvimage,showsubtask;
    private FloatingActionButton fab;
    private Button btnadd, btnupdate;
    private ViewModel viewModel;
    private TextView addsub;
    private ImageView imgDelete;
    private SubTaskViewMOdel subTaskViewMOdel;
    private TaskListAdapter taskListAdapter;
    private int noteId,subnoteId;

    private static ImageView gallery_image;
    private final int select_photo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_addmaintask);

        initialize();
        edittextEmptyornot();
        setOnClickListener();

    }


    private void edittextEmptyornot() {
        if (edadd.getText().toString().equals("")) {
            addsub.setEnabled(false);
        }
        edadd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edadd.getText().toString().equals("")) {
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

              /*  if(edadd.getText().toString().equals("")){
                    fab.setEnabled(false);
                } else {
                    fab.setEnabled(true);
                }*/

            }
        });

    }

    /**
     * for Initialize Variable
     */
    private void initialize() {
        edadd = findViewById(R.id.show_title);
        eddesc = findViewById(R.id.details);
        eddatetime = findViewById(R.id.date_time);
        btnadd = findViewById(R.id.btnmainadd);
        tvimage=findViewById(R.id.image);
        addsub = findViewById(R.id.txaddsubtask);
        btnupdate = findViewById(R.id.btnmainupdate);
        imgDelete = findViewById(R.id.ivRowDelete);
        gallery_image = (ImageView) findViewById(R.id.gallery_imageview);
        showsubtask=findViewById(R.id.showsubtask);

        /*taskListAdapter = new TaskListAdapter(this ,this);*/
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getInt("note_id");
            subnoteId=bundle.getInt("sub_note_id");

        }

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        subTaskViewMOdel=ViewModelProviders.of(this).get(SubTaskViewMOdel.class);
        /* fab = findViewById(R.id.fab);*/
        LiveData<Task> note = viewModel.getNote(noteId);
       /* LiveData<SubTask> subnote=subTaskViewMOdel.getsubNote(subnoteId);

        subnote.observe(this, new Observer<SubTask>() {
            @Override
            public void onChanged(SubTask subTask) {
                    showsubtask.setText(subTask.getSub_name());
            }
        });*/
        note.observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable Task task) {
                SubTask subTask;
                if (task != null) {
                    edadd.setText(task.getName());
                    eddesc.setText(task.getDescription());
                    eddatetime.setText(task.getDate());
                    tvimage.setText(task.getImg_path());
                    addsub.setVisibility(View.GONE);
                    btnupdate.setVisibility(View.VISIBLE);
                    btnadd.setVisibility(View.GONE);

                    showsubtask.setVisibility(View.VISIBLE);

                    gallery_image.setImageBitmap(BitmapFactory.decodeFile(task.getImg_path()));
                    Drawable d = Drawable.createFromPath(task.getImg_path());
                    gallery_image.setImageDrawable(d);




                }
            }
        });


    }


    /**
     * for  click of EditText and Button
     */
    private void setOnClickListener() {

        eddatetime.setOnClickListener(this);
        btnadd.setOnClickListener(this);
        addsub.setOnClickListener(this);
        btnupdate.setOnClickListener(this);
        /*imgDelete.setOnClickListener(this);*/
        tvimage.setOnClickListener(this);

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

            case R.id.image:
                uploadImage();
                break;

            case R.id.date_time:
                showDateTime();
                break;
            case R.id.txaddsubtask:
                Add_Data();
                navigateToSubTaskScreen();
                break;
            case R.id.btnmainadd:
                Add_Data();
                break;
            /*case R.id.ivRowDelete:
                break;*/
            case R.id.btnmainupdate:
                updatedata();
                break;

        }
    }

    private void uploadImage() {

        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, select_photo);




    }

    private void updatedata() {


        Intent result = new Intent();
        if (TextUtils.isEmpty(edadd.getText())) {
            setResult(RESULT_CANCELED, result);
        } else {

            Task task;
            String updatetitle = edadd.getText().toString();
            String updatedesc = eddesc.getText().toString();
            String updatedatetime = eddatetime.getText().toString();
            String updatepath=tvimage.getText().toString();



            if (TextUtils.isEmpty(updatedesc) || TextUtils.isEmpty(updatedatetime)||TextUtils.isEmpty(updatepath))
            {
                task = new Task(noteId,updatetitle);
                viewModel.updateName(task);

            }
            else if(TextUtils.isEmpty(updatedatetime)||TextUtils.isEmpty(updatepath))
            {
                task = new Task(noteId,updatetitle,updatedesc);
                viewModel.updateName(task);
            }

            else {
                task = new Task(noteId, updatetitle, updatedesc, updatedatetime,updatepath);
                viewModel.updateName(task);
            }


            setResult(RESULT_OK, result);
            finish();
        }

    }


    /**
     * for Add Main Task data in Room Database
     */
    public void Add_Data() {


        Intent resultintent = new Intent();
        if (TextUtils.isEmpty(edadd.getText())) {
            setResult(RESULT_CANCELED, resultintent);
        } else {
            Task task;

            String note = edadd.getText().toString();
            String desc = eddesc.getText().toString();
            String time = eddatetime.getText().toString();
            String path= tvimage.getText().toString();

            setResult(RESULT_OK, resultintent);
            if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(time)||TextUtils.isEmpty(path))
            {
                task = new Task(note);
                viewModel.insert(task);

            }
            else if(TextUtils.isEmpty(time)||TextUtils.isEmpty(path))
            {
                task = new Task(note,desc);
                viewModel.insert(task);
            }

            else {
                task = new Task(note, desc, time,path);
                viewModel.insert(task);

            }

        }
        finish();
    }


    private void showDateTime() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddMainTaskActivity.this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddMainTaskActivity.this, this, hour, minute, DateFormat.is24HourFormat(this));
        eddatetime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        eddatetime.setText(eddatetime.getText() + " -" + hourOfDay + ":" + minute);

    }




    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    Uri imageuri = imagereturnintent.getData();// Get intent
                    String real_Path = getRealPathFromUri(AddMainTaskActivity.this,
                            imageuri);
                    tvimage.setText(real_Path);
                    tvimage.setVisibility(View.VISIBLE);
                    Bitmap bitmap = null;// call
                    try {
                        bitmap = decodeUri(AddMainTaskActivity.this, imageuri, 300);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null)
                        gallery_image.setImageBitmap(bitmap);// Set image over
                        // bitmap

                    else
                        Toast.makeText(AddMainTaskActivity.this,
                                "Error while decoding image.",
                                Toast.LENGTH_SHORT).show();
                }
        }
    }

    // Method that deocde uri into bitmap. This method is necessary to deocde
    // large size images to load over imageview
    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }
    // Get Original image path
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
