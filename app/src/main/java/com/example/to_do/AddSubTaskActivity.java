package com.example.to_do;

import android.app.Application;
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
import android.text.TextUtils;
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
import com.example.to_do.Database.ViewModel;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;

public class AddSubTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public EditText edsubtitle, edsubdesc, edsubdatettime;
    Button btnsubadd,btnsubupdate;
    private SubTaskViewMOdel subTaskViewMOdel;
    private TextView tvsubimage;
    private ImageView imgsubimage;
    private ViewModel viewModel;
    private final int select_photo = 1;
    int noteId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).hide();
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
        edsubtitle = findViewById(R.id.show_sub_title);
        edsubdesc = findViewById(R.id.sub_details);
        edsubdatettime = findViewById(R.id.sub_date_time);
        btnsubadd = findViewById(R.id.btnsubadd);
        tvsubimage=findViewById(R.id.sub_tvimage);
        imgsubimage=(ImageView)findViewById(R.id.sub_imageview);
        btnsubupdate=findViewById(R.id.btnsubupdate);
     /*   Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noteId = bundle.getInt("note_id");
        }
        LiveData<Task> note = SubTaskViewMOdel.getNote(noteId);*/


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getInt("note_id");

        }

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        /* fab = findViewById(R.id.fab);*/
        LiveData<SubTask> note = viewModel.getsubNote(noteId);
        note.observe(this, new Observer<SubTask>() {
            @Override
            public void onChanged(@Nullable SubTask subTask) {
                if (subTask != null) {
                    edsubtitle.setText(subTask.getSub_name());
                    edsubdesc.setText(subTask.getSub_description());
                    edsubdatettime.setText(subTask.getSub_date_time());
                    tvsubimage.setText(subTask.getSub_img_path());
                    btnsubupdate.setVisibility(View.VISIBLE);
                    btnsubadd.setVisibility(View.GONE);
                    imgsubimage.setImageBitmap(BitmapFactory.decodeFile(subTask.getSub_img_path()));
                    Drawable d = Drawable.createFromPath(subTask.getSub_img_path());
                    imgsubimage.setImageDrawable(d);
                   /* File imgFile = new  File(tvimage.getText().toString());

                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getPath());
                        gallery_image.setImageBitmap(myBitmap);

                    }*/
                    /* imgDelete.setVisibility(View.VISIBLE);*/
                }
            }
        });


    }



    private void setOnClickListener() {
        edsubdatettime.setOnClickListener(this);
        btnsubadd.setOnClickListener(this);
        tvsubimage.setOnClickListener(this);
        btnsubupdate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sub_tvimage:
                uploadSubImage();
                break;

            case R.id.sub_date_time:
                showDateTime();
                break;

            case R.id.btnsubadd:
                addSubData();
                break;

            case R.id.btnsubupdate:
                updatesubdata();
                break;

        }

    }

    private void updatesubdata() {
        Intent result = new Intent();
        if (TextUtils.isEmpty(edsubtitle.getText())) {
            setResult(RESULT_CANCELED, result);
        } else {

            SubTask subtask;
            String updatetitle = edsubtitle.getText().toString();
            String updatedesc = edsubdesc.getText().toString();
            String updatedatetime = edsubdatettime.getText().toString();
            String updatepath=tvsubimage.getText().toString();


            subtask = new SubTask(noteId, updatetitle, updatedesc, updatedatetime,updatepath);
            subTaskViewMOdel.update(subtask);
            setResult(RESULT_OK, result);
            finish();
        }


    }

    private void uploadSubImage() {
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, select_photo);

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
            String msubpath=tvsubimage.getText().toString();



            if (TextUtils.isEmpty(mdesc) && TextUtils.isEmpty(mtime)||TextUtils.isEmpty(msubpath)) {
                subTask = new SubTask(mnote);
                subTaskViewMOdel.insert(subTask);
            }
            else if(TextUtils.isEmpty(mtime)||TextUtils.isEmpty(msubpath))
            {
                subTask = new SubTask(mnote,mdesc);
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


    private void showDateTime() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddSubTaskActivity.this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddSubTaskActivity.this, this, hour, minute, DateFormat.is24HourFormat(this));
        edsubdatettime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        edsubdatettime.setText(edsubdatettime.getText() + " -" + hourOfDay + ":" + minute);

    }


    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    try {

                        Uri imageuri = imagereturnintent.getData();// Get intent
                        // data


                        // Get real path and show over text view
                        String real_Path = getRealPathFromUri(AddSubTaskActivity.this,
                                imageuri);
                        tvsubimage.setText(real_Path);



                        Bitmap bitmap = decodeUri(AddSubTaskActivity.this, imageuri, 300);// call
                        // deocde
                        // uri
                        // method
                        // Check if bitmap is not null then set image else show
                        // toast
                        if (bitmap != null)
                            imgsubimage .setImageBitmap(bitmap);// Set image over
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
