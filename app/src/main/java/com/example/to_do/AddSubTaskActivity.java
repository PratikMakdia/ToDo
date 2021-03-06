package com.example.to_do;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
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
import android.text.TextUtils;
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

import com.example.to_do.model.SubTask;
import com.example.to_do.Database.SubTaskViewModel;
import com.example.to_do.Database.ViewModel;
import com.example.to_do.model.Task;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddSubTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText edSubTitle, edSubDesc, edSubDateTime;
    private Button btnSubAdd, btnSubUpdate;
    private SubTaskViewModel subTaskViewModel;
    private TextView tvSubImage;
    private ImageView ivSubImage;
    private int select_photo = 1;
    private int noteId,getMainTaskID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsubtask);
        initialize();
        setOnClickListener();
    }


    /**
     * for Initialize Variables
     *
     */
    private void initialize() {
        subTaskViewModel = new SubTaskViewModel((Application) getApplicationContext());
        edSubTitle = findViewById(R.id.tvShowSubTitle);
        edSubDesc = findViewById(R.id.tvSubTaskDetails);
        edSubDateTime = findViewById(R.id.tvSubTaskDateTime);
        btnSubAdd = findViewById(R.id.btnSubAdd);
        tvSubImage =findViewById(R.id.tvShowSubImagePath);
        ivSubImage =findViewById(R.id.ivSubImageView);
        btnSubUpdate =findViewById(R.id.btnSubUpdate);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getInt("noteId");

        }
        Bundle getID= getIntent().getExtras();
        if(getID!=null) {
            getMainTaskID = getID.getInt("mainTaskID");

        }


        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        LiveData<SubTask> note = viewModel.getSubNote(noteId);
        note.observe(this, new Observer<SubTask>() {
            @Override
            public void onChanged(@Nullable SubTask subTask) {
                if (subTask != null) {
                    edSubTitle.setText(subTask.getSub_name());
                    edSubDesc.setText(subTask.getSub_description());
                    edSubDateTime.setText(subTask.getSub_date_time());
                    tvSubImage.setText(subTask.getSub_img_path());
                    btnSubUpdate.setVisibility(View.VISIBLE);
                    btnSubAdd.setVisibility(View.GONE);
                    ivSubImage.setImageBitmap(BitmapFactory.decodeFile(subTask.getSub_img_path()));
                    Drawable d = Drawable.createFromPath(subTask.getSub_img_path());
                    ivSubImage.setImageDrawable(d);
                }
            }
        });

       /* Bundle getID= getIntent().getExtras();
        if(getID!=null) {
            getMainTaskID = getID.getInt("mainTaskID");

        }*/

    }



    private void setOnClickListener() {
        edSubDateTime.setOnClickListener(this);
        btnSubAdd.setOnClickListener(this);
        tvSubImage.setOnClickListener(this);
        btnSubUpdate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvShowSubImagePath:
                uploadSubTaskImageInRoomDatabse();
                break;

            case R.id.tvSubTaskDateTime:
                showDateAndTimePicker();
                break;

            case R.id.btnSubAdd:
                addSubTaskDataintoRoomDatabase();
                break;

            case R.id.btnSubUpdate:
                updateSubTaskDataIntoRoomDatabase();
                break;

        }

    }

    private void updateSubTaskDataIntoRoomDatabase() {
        Intent result = new Intent();
        if (TextUtils.isEmpty(edSubTitle.getText())) {
            setResult(RESULT_CANCELED, result);
        } else {

            SubTask subtask;
            String mUpdateTitle = edSubTitle.getText().toString();
            String mUpdateDesc = edSubDesc.getText().toString();
            String mUpdateDateTime = edSubDateTime.getText().toString();
            String mUpdatePath= tvSubImage.getText().toString();


            subtask = new SubTask(noteId, mUpdateTitle, mUpdateDesc, mUpdateDateTime,mUpdatePath);
            if (TextUtils.isEmpty(mUpdateDesc) && TextUtils.isEmpty(mUpdateDateTime) && TextUtils.isEmpty(mUpdatePath)) {
                subtask = new SubTask(noteId, mUpdateTitle,getMainTaskID);
                subTaskViewModel.update(subtask);

            } else if (TextUtils.isEmpty(mUpdateDateTime) && TextUtils.isEmpty(mUpdatePath)) {
                subtask = new SubTask(noteId, mUpdateTitle, mUpdateDesc,getMainTaskID);
                subTaskViewModel.update(subtask);
            } else {
                subtask = new SubTask(noteId, mUpdateTitle, mUpdateDesc, mUpdateDateTime, mUpdatePath,getMainTaskID);
                subTaskViewModel.update(subtask);
            }

            subTaskViewModel.update(subtask);
            setResult(RESULT_OK, result);
            finish();
        }


    }

    private void uploadSubTaskImageInRoomDatabse() {
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, select_photo);

    }


    private void addSubTaskDataintoRoomDatabase() {


        Intent subResultIntent = new Intent();
        if (TextUtils.isEmpty(edSubTitle.getText())) {
            setResult(RESULT_CANCELED, subResultIntent);
        } else {

            SubTask subTask;


            String mSubNote = edSubTitle.getText().toString();
            String mSubDesc = edSubDesc.getText().toString();
            String mSubTime = edSubDateTime.getText().toString();
            String mSubPath= tvSubImage.getText().toString();


            setResult(RESULT_OK, subResultIntent);
            if (TextUtils.isEmpty(mSubDesc) && TextUtils.isEmpty(mSubTime)&&TextUtils.isEmpty(mSubPath)) {
                subTask = new SubTask(mSubNote,getMainTaskID);
                subTaskViewModel.insert(subTask);
            }
            else if(TextUtils.isEmpty(mSubTime)&&TextUtils.isEmpty(mSubPath))
            {
                subTask = new SubTask(mSubNote,mSubDesc,getMainTaskID);
                subTaskViewModel.insert(subTask);
            }
            else if( TextUtils.isEmpty(mSubPath))

            {
                subTask = new SubTask(mSubNote, mSubDesc,mSubTime,getMainTaskID);
                subTaskViewModel.insert(subTask);
            }
            else {
                subTask = new SubTask(mSubNote, mSubDesc, mSubTime,mSubPath,getMainTaskID);
                subTaskViewModel.insert(subTask);
            }


            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);


        }
        finish();
    }


    private void showDateAndTimePicker() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddSubTaskActivity.this, this, year, month, day);
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddSubTaskActivity.this, this, hour, minute, DateFormat.is24HourFormat(this));
        edSubDateTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        edSubDateTime.setText(edSubDateTime.getText() + " -" + hourOfDay + ":" + minute);

        Intent subIntent = new Intent(AddSubTaskActivity.this, SubTaskNotificationReciever.class);
        subIntent.putExtra("sub_notificationId", getSubTaskUniqueNotificationId());
        subIntent.putExtra("sub_todo", edSubTitle.getText().toString());
        PendingIntent subAlarmIntent = PendingIntent.getBroadcast(AddSubTaskActivity.this, 0, subIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager subAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);


        long alarmStartTime = startTime.getTimeInMillis();
        if (subAlarm != null) {
            subAlarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, subAlarmIntent);
        }

    }
    public int getSubTaskUniqueNotificationId() {
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



    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnIntent) {
       super.onActivityResult(requestCode, resultCode, imageReturnIntent);
        if (requestCode == select_photo) {
            if (resultCode == RESULT_OK) {
                try {

                    Uri imageUri = imageReturnIntent.getData();// Get intent
                    // data


                    // Get real path and show over text view
                    String real_Path = getRealPathFromUri(AddSubTaskActivity.this,
                            imageUri);
                    tvSubImage.setText(real_Path);


                    Bitmap bitmap = decodeUri(AddSubTaskActivity.this, imageUri, 300);// call
                    // deocde
                    // uri
                    // method
                    // Check if bitmap is not null then set image else show
                    // toast
                    if (bitmap != null)
                        ivSubImage.setImageBitmap(bitmap);// Set image over
                        // bitmap

                    else
                        Toast.makeText(AddSubTaskActivity.this,
                                "Error while decoding image.",
                                Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                    Toast.makeText(AddSubTaskActivity.this, "File not found.",
                            Toast.LENGTH_SHORT).show();
                }
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
