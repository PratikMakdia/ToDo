package com.example.to_do;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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

import com.example.to_do.Database.Task;
import com.example.to_do.Database.ViewModel;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddMainTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText edAdd, edDesc, edDateTime;
    private TextView tvImage, tvShowSubTask, tvAddSubTask;
    private Button btnAdd, btnUpdate;
    private ViewModel viewModel;
    private int noteId;
    private ImageView ivGalleryImage;
    private int select_photo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmaintask);

        initialize();
        edittextEmptyOrNot();
        setOnClickListener();

    }


    private void edittextEmptyOrNot() {
        if (edAdd.getText().toString().equals("")) {
            tvAddSubTask.setEnabled(false);
        }
        edAdd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edAdd.getText().toString().equals("")) {
                    tvAddSubTask.setEnabled(false);
                } else {
                    tvAddSubTask.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * for Initialize Variable
     */
    private void initialize() {
        edAdd = findViewById(R.id.show_title);
        edDesc = findViewById(R.id.details);
        edDateTime = findViewById(R.id.date_time);
        btnAdd = findViewById(R.id.btnmainadd);
        tvImage = findViewById(R.id.image);
        tvAddSubTask = findViewById(R.id.txaddsubtask);
        btnUpdate = findViewById(R.id.btnmainupdate);
        ivGalleryImage = findViewById(R.id.gallery_imageview);
        tvShowSubTask = findViewById(R.id.showsubtask);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getInt("note_id");


        }
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        LiveData<Task> note = viewModel.getNote(noteId);
        note.observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable Task task) {

                if (task != null) {
                    edAdd.setText(task.getName());
                    edDesc.setText(task.getDescription());
                    edDateTime.setText(task.getDate());
                    tvImage.setText(task.getImg_path());
                    tvAddSubTask.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);
                    tvShowSubTask.setVisibility(View.VISIBLE);
                    ivGalleryImage.setImageBitmap(BitmapFactory.decodeFile(task.getImg_path()));
                    Drawable d = Drawable.createFromPath(task.getImg_path());
                    ivGalleryImage.setImageDrawable(d);


                }
            }
        });


    }


    /**
     * for  click of EditText and Button
     */
    private void setOnClickListener() {

        edDateTime.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        tvAddSubTask.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        tvImage.setOnClickListener(this);

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
                showDateAndTimePicker();
                break;
            case R.id.txaddsubtask:
                AddDataIntoRoomDatabase();
                navigateToSubTaskScreen();
                break;
            case R.id.btnmainadd:
                AddDataIntoRoomDatabase();
                break;
            case R.id.btnmainupdate:
                updateDataIntoRomDatabse();
                break;

        }
    }

    /**
     * For Image Upload
     */
    private void uploadImage() {

        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, select_photo);


    }

    /**
     * for update data in  room database
     */
    private void updateDataIntoRomDatabse() {


        Intent result = new Intent();
        if (TextUtils.isEmpty(edAdd.getText())) {
            setResult(RESULT_CANCELED, result);
        } else {

            Task task;
            String mUpdateTitle = edAdd.getText().toString();
            String mUpdateDesc = edDesc.getText().toString();
            String mUpdateDateTime = edDateTime.getText().toString();
            String mUpdatePath = tvImage.getText().toString();


            if (TextUtils.isEmpty(mUpdateDesc) || TextUtils.isEmpty(mUpdateDateTime) || TextUtils.isEmpty(mUpdatePath)) {
                task = new Task(noteId, mUpdateTitle);
                viewModel.updateName(task);

            } else if (TextUtils.isEmpty(mUpdateDateTime) || TextUtils.isEmpty(mUpdatePath)) {
                task = new Task(noteId, mUpdateTitle, mUpdateDesc);
                viewModel.updateName(task);
            } else {
                task = new Task(noteId, mUpdateTitle, mUpdateDesc, mUpdateDateTime, mUpdatePath);
                viewModel.updateName(task);
            }


            setResult(RESULT_OK, result);
            finish();
        }

    }


    /**
     * for Add Main Task data in Room Database
     */
    public void AddDataIntoRoomDatabase() {


        Intent resultintent = new Intent();
        if (TextUtils.isEmpty(edAdd.getText())) {
            setResult(RESULT_CANCELED, resultintent);
        } else {
            Task task;

            String note = edAdd.getText().toString();
            String desc = edDesc.getText().toString();
            String time = edDateTime.getText().toString();
            String path = tvImage.getText().toString();

            setResult(RESULT_OK, resultintent);
            if (TextUtils.isEmpty(desc) || TextUtils.isEmpty(time) || TextUtils.isEmpty(path)) {
                task = new Task(note);
                viewModel.insert(task);

            } else if (TextUtils.isEmpty(time) || TextUtils.isEmpty(path)) {
                task = new Task(note, desc);
                viewModel.insert(task);
            } else if( TextUtils.isEmpty(path))

            {
                task = new Task(note, desc,time);
                viewModel.insert(task);
            }
            else {
                task = new Task(note, desc, time, path);
                viewModel.insert(task);
            }

        }
        finish();
    }


    private void showDateAndTimePicker() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddMainTaskActivity.this, this, year, month, day);
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddMainTaskActivity.this, this, hour, minute, DateFormat.is24HourFormat(this));
        edDateTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        edDateTime.setText(edDateTime.getText() + " -" + hourOfDay + ":" + minute);



        Intent intent = new Intent(AddMainTaskActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", getUniqueNotificationId());
        intent.putExtra("todo", edAdd.getText().toString());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(AddMainTaskActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);


        long alarmStartTime = startTime.getTimeInMillis();
        if (alarm != null) {
            alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
        }


    }

    public int getUniqueNotificationId() {
        int notificationId;
        try {
            notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            Random random = new Random();
            notificationId = random.nextInt(9999 - 1000) + 10;
        }
        return notificationId;
    }


    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        if (requestcode == select_photo) {
            if (resultcode == RESULT_OK) {
                Uri imageuri = imagereturnintent.getData();// Get intent
                String real_Path = getRealPathFromUri(AddMainTaskActivity.this,
                        imageuri);


                tvImage.setText(real_Path);
                tvImage.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), (CharSequence) imageuri,Toast.LENGTH_SHORT).show();
                Bitmap bitmap = null;// call
                try {
                    bitmap = decodeUri(AddMainTaskActivity.this, imageuri, 300);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                if (bitmap != null)
                    ivGalleryImage.setImageBitmap(bitmap);// Set image over
                    // bitmap

                else
                    Toast.makeText(AddMainTaskActivity.this,
                            "Error while decoding image.",
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method that deocde uri into bitmap. This method is necessary to deocdex`
    // large size images to load over imageview
    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (width_tmp / 2 >= requiredSize && height_tmp / 2 >= requiredSize) {
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
