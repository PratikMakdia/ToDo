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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do.Database.SubTaskViewModel;
import com.example.to_do.Database.ViewModel;
import com.example.to_do.model.SubTask;
import com.example.to_do.model.SubTaskListAdapter;
import com.example.to_do.model.Task;
import com.example.to_do.model.TaskListAdapter;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.app.DatePickerDialog.*;
import static android.app.TimePickerDialog.*;
import static com.example.to_do.model.SubTaskListAdapter.*;

public class AddMainTaskActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener, OnTimeSetListener, OnDeleteClickListener {

    private int getId;
    private EditText edAdd, edDesc, edDateTime;
    private TextView tvImage, tvShowSubTask, tvAddSubTask;
    private Button btnAdd, btnUpdate;
    private ViewModel viewModel;
    private int noteId;
    private SubTaskViewModel subTaskViewModel;
    private ImageView ivGalleryImage, ivAddSubTaskLogo;
    private SubTaskListAdapter subTaskListAdapter;
    private RecyclerView rvShowSubTask;
    private int select_photo = 1;
    private static int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmaintask);

        initialize();
        editTextEmptyOrNot();
        setOnClickListener();

    }


    private void editTextEmptyOrNot() {
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
        edAdd = findViewById(R.id.edMainTask);
        edDesc = findViewById(R.id.edDetails);
        edDateTime = findViewById(R.id.edDateTime);
        btnAdd = findViewById(R.id.btnMainTaskAdd);
        tvImage = findViewById(R.id.tvPath);
        tvAddSubTask = findViewById(R.id.tvAddSubTasks);
        btnUpdate = findViewById(R.id.btnMainTaskUpdate);
        ivGalleryImage = findViewById(R.id.ivShowImage);
        rvShowSubTask = findViewById(R.id.rvSubRecyclerView);
        ivAddSubTaskLogo = findViewById(R.id.ivAddSubTask);


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
                    tvImage.setText(task.getImgPath());
                    tvAddSubTask.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);
                    rvShowSubTask.setVisibility(View.VISIBLE);
                    ivAddSubTaskLogo.setVisibility(View.VISIBLE);
                    ivGalleryImage.setImageBitmap(BitmapFactory.decodeFile(task.getImgPath()));
                    Drawable d = Drawable.createFromPath(task.getImgPath());
                    ivGalleryImage.setImageDrawable(d);
                }
            }
        });

        subTaskListAdapter = new SubTaskListAdapter(this, this);
        rvShowSubTask.setAdapter(subTaskListAdapter);
        rvShowSubTask.setLayoutManager(new LinearLayoutManager(this));

        subTaskViewModel = ViewModelProviders.of(this).get(SubTaskViewModel.class);

        subTaskViewModel.GetForeignKeyId().observe(this, new Observer<List<SubTask>>() {
            @Override
            public void onChanged(List<SubTask> subTasks) {

                subTaskListAdapter.setNotes(subTasks);
            }
        });

        /*subTaskViewModel.getAllSubNote().observe(this, new Observer<List<SubTask>>() {
            @Override
            public void onChanged(List<SubTask> subTasks) {

                subTaskListAdapter.setNotes(subTasks);
            }
        });*/


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
        ivAddSubTaskLogo.setOnClickListener(this);

    }


    /**
     * for Navigate to Sub Task Screen
     */
    private void navigateToSubTaskScreen() {
        String mNote = edAdd.getText().toString();
        LiveData<Task> note = viewModel.mainTaskId(mNote);
        note.observe(this, new Observer<Task>() {
            @Override
            public void onChanged(@Nullable Task task) {

                if (task != null) {
                    getId = task.getId();

                    Intent idPass = new Intent(AddMainTaskActivity.this, AddSubTaskActivity.class);
                    idPass.putExtra("mainTaskID", getId);
                    startActivity(idPass);
                    finish();

                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvPath:
                uploadImage();
                break;

            case R.id.edDateTime:
                showDateAndTimePicker();
                break;
            case R.id.tvAddSubTasks:
                AddDataIntoRoomDatabase();
                break;
            case R.id.btnMainTaskAdd:
                AddDataIntoRoom();
                break;
            case R.id.btnMainTaskUpdate:
                updateDataIntoRomDatabase();
                break;
            case R.id.ivAddSubTask:
                navigateToSubTaskScreen();
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
    private void updateDataIntoRomDatabase() {


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
            SetNotification();
            finish();
        }

    }


    /**
     * for Add Main Task data in Room Database And Navigate to Sub Task Screen
     */
    public void AddDataIntoRoomDatabase() {

        Intent resultIntent = new Intent();
        if (TextUtils.isEmpty(edAdd.getText())) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            Task task;

            String mNote = edAdd.getText().toString();
            String mDesc = edDesc.getText().toString();
            String mTime = edDateTime.getText().toString();
            String mPath = tvImage.getText().toString();

            setResult(RESULT_OK, resultIntent);
            if (TextUtils.isEmpty(mDesc) && TextUtils.isEmpty(mTime) && TextUtils.isEmpty(mPath)) {
                task = new Task(mNote, getUniqueNotificationId());
                viewModel.insert(task);
                navigateToSubTaskScreen();
//

            } else if (TextUtils.isEmpty(mTime) && TextUtils.isEmpty(mPath)) {
                task = new Task(mNote, mDesc, getUniqueNotificationId());
                viewModel.insert(task);
                navigateToSubTaskScreen();


            } else if (TextUtils.isEmpty(mPath)) {
                task = new Task(mNote, mDesc, mTime, getUniqueNotificationId());
                viewModel.insert(task);
                navigateToSubTaskScreen();

            } else {
                task = new Task(mNote, mDesc, mTime, mPath, getUniqueNotificationId());
                viewModel.insert(task);
                navigateToSubTaskScreen();

            }

        }
        SetNotification();
        finish();
    }


    public void AddDataIntoRoom() {


        if (TextUtils.isEmpty(edAdd.getText())) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_fill_main_task), Toast.LENGTH_SHORT).show();
        } else {
            Task task;

            String mNote = edAdd.getText().toString();
            String mDesc = edDesc.getText().toString();
            String mTime = edDateTime.getText().toString();
            String mPath = tvImage.getText().toString();


            if (TextUtils.isEmpty(mDesc) && TextUtils.isEmpty(mTime) && TextUtils.isEmpty(mPath)) {
                task = new Task(mNote);
                viewModel.insert(task);

            } else if (TextUtils.isEmpty(mTime) && TextUtils.isEmpty(mPath)) {
                task = new Task(mNote, mDesc);
                viewModel.insert(task);


            } else if (TextUtils.isEmpty(mPath)) {
                task = new Task(mNote, mDesc, mTime, getUniqueNotificationId());
                viewModel.insert(task);
                SetNotification();

            } else {
                task = new Task(mNote, mDesc, mTime, mPath, getUniqueNotificationId());
                viewModel.insert(task);
                SetNotification();
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

        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

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


        hourFinal = hourOfDay;
        minuteFinal = minute;
        edDateTime.setText(edDateTime.getText() + " -" + hourOfDay + ":" + minute);

    }

    public void SetNotification() {

            Intent intent = new Intent(AddMainTaskActivity.this, AlarmReceiver.class);
            intent.putExtra("notificationId", getUniqueNotificationId());
            intent.putExtra("todo", edAdd.getText().toString());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(AddMainTaskActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

            // Create time.
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hourFinal);
            startTime.set(Calendar.MINUTE, minuteFinal);
            startTime.set(Calendar.SECOND, 0);


            long alarmStartTime = startTime.getTimeInMillis();
            if (alarm != null) {
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
            }


    }

    public static int getUniqueNotificationId() {
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


    @Override
    public void onDeleteClickListener(SubTask subTask) {
        subTaskViewModel.delete(subTask);
    }
}
