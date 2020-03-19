package com.example.to_do.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subtask",
        foreignKeys = @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "taskId",
                onDelete = ForeignKey.CASCADE))
public class SubTask {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subID")
    private int id;

    @ColumnInfo(name = "subName")
    private String sub_name;

    @ColumnInfo(name = "subDetails")
    private String sub_description;

    @ColumnInfo(name = "subDateTime")
    private String sub_date_time;


    @ColumnInfo(name = "subImagePath")
    private String sub_img_path;

    @ColumnInfo(name = "taskId")
    private int mainTaskId;


    public SubTask(int id, String sub_name, String sub_description, String sub_date_time, String sub_img_path, int mainTaskId) {
        this.id = id;
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.sub_img_path = sub_img_path;
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(int id, String sub_name, String sub_description, String sub_date_time, String sub_img_path) {
        this.id = id;
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.sub_img_path = sub_img_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_description() {
        return sub_description;
    }

    public void setSub_description(String sub_description) {
        this.sub_description =
                sub_description;
    }

    public String getSub_date_time() {
        return sub_date_time;
    }

    public void setSub_date_time(String sub_date_time) {
        this.sub_date_time = sub_date_time;
    }

    public int getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(int mainTaskId) {
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(String sub_name, int mainTaskId) {
        this.sub_name = sub_name;
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_img_path() {
        return sub_img_path;
    }

    public void setSub_img_path(String sub_img_path) {
        this.sub_img_path = sub_img_path;
    }

    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
    }

    @Ignore
    public SubTask(int id, String sub_name, int mainTaskId) {
        this.id = id;
        this.sub_name = sub_name;
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(int id, String sub_name, String sub_description, int mainTaskId) {
        this.id = id;
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time, int mainTaskId) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.mainTaskId = mainTaskId;
    }

    @Ignore
    public SubTask(int id, String sub_name, String sub_description) {
        this.id = id;
        this.sub_name = sub_name;
        this.sub_description = sub_description;
    }

    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time, String sub_img_path) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.sub_img_path = sub_img_path;
    }

    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time, String sub_img_path, int mainTaskId) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.sub_img_path = sub_img_path;
        this.mainTaskId = mainTaskId;
    }
/* @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time, int maintask_id) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.maintask_id = maintask_id;
    }*/

   @Ignore
    public SubTask(String sub_name, String sub_description, int mainTaskId) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.mainTaskId = mainTaskId;
    }



    @Ignore
    public SubTask(int id, String sub_name) {
        this.id = id;
        this.sub_name = sub_name;
    }
}
