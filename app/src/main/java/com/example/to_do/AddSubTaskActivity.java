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

import com.example.to_do.Database.SubTask;
import com.example.to_do.Database.SubTaskViewMOdel;
import com.example.to_do.Database.ViewModel;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddSubTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText edSubTitle, edSubDesc, edSubDateTime;
    private Button btnSubAdd, btnSubUpdate;
    private SubTaskViewMOdel subTaskViewMOdel;
    private TextView tvSubImage;
    private ImageView ivSubImage;
    private int select_photo = 1;
    private int noteId;



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
        subTaskViewMOdel = new SubTaskViewMOdel((Application) getApplicationContext());
        edSubTitle = findViewById(R.id.show_sub_title);
        edSubDesc = findViewById(R.id.sub_details);
        edSubDateTime = findViewById(R.id.sub_date_time);
        btnSubAdd = findViewById(R.id.btnsubadd);
        tvSubImage =findViewById(R.id.sub_tvimage);
        ivSubImage =findViewById(R.id.sub_imageview);
        btnSubUpdate =findViewById(R.id.btnsubupdate);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getInt("note_id");

        }

        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        LiveData<SubTask> note = viewModel.getsubNote(noteId);
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

            case R.id.sub_tvimage:
                uploadSubTaskImageInRoomDatabse();
                break;

            case R.id.sub_date_time:
                showDateAndTimePicker();
                break;

            case R.id.btnsubadd:
                addSubTaskDataintoRoomDatabase();
                break;

            case R.id.btnsubupdate:
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
            String updatetitle = edSubTitle.getText().toString();
            String updatedesc = edSubDesc.getText().toString();
            String updatedatetime = edSubDateTime.getText().toString();
            String updatepath= tvSubImage.getText().toString();


            subtask = new SubTask(noteId, updatetitle, updatedesc, updatedatetime,updatepath);
            subTaskViewMOdel.update(subtask);
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
        Intent resultintent = new Intent();
        if (TextUtils.isEmpty(edSubTitle.getText()) || TextUtils.isEmpty(edSubDesc.getText()) || TextUtils.isEmpty(edSubDateTime.getText())) {
            setResult(RESULT_CANCELED, resultintent);
        } else {

            SubTask subTask;

            String mnote = edSubTitle.getText().toString();
            String mdesc = edSubDesc.getText().toString();
            String mtime = edSubDateTime.getText().toString();
            String msubpath= tvSubImage.getText().toString();



            if (TextUtils.isEmpty(mdesc) && TextUtils.isEmpty(mtime)||TextUtils.isEmpty(msubpath)) {
                subTask = new SubTask(mnote);
                subTaskViewMOdel.insert(subTask);
            }
            else if(TextUtils.isEmpty(mtime)||TextUtils.isEmpty(msubpath))
            {
                subTask = new SubTask(mnote,mdesc);
                subTaskViewMOdel.insert(subTask);
            }
            else if( TextUtils.isEmpty(msubpath))

            {
                subTask = new SubTask(mnote, mdesc,mtime);
                subTaskViewMOdel.insert(subTask);
            }
            else {
                subTask = new SubTask(mnote, mdesc, mtime,msubpath);
                subTaskViewMOdel.insert(subTask);
            }






            setResult(RESULT_OK, resultintent);
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

        Intent subintent = new Intent(AddSubTaskActivity.this, SubTaskNotificationReciever.class);
        subintent.putExtra("sub_notificationId", getUniqueNotificationId());
        subintent.putExtra("sub_todo", edSubTitle.getText().toString());
        PendingIntent subalarmIntent = PendingIntent.getBroadcast(AddSubTaskActivity.this, 0, subintent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager subalarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);


        long alarmStartTime = startTime.getTimeInMillis();
        if (subalarm != null) {
            subalarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, subalarmIntent);
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
                try {

                    Uri imageuri = imagereturnintent.getData();// Get intent
                    // data


                    // Get real path and show over text view
                    String real_Path = getRealPathFromUri(AddSubTaskActivity.this,
                            imageuri);
                    tvSubImage.setText(real_Path);


                    Bitmap bitmap = decodeUri(AddSubTaskActivity.this, imageuri, 300);// call
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
