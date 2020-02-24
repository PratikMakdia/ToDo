package com.example.to_do.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subtask")/*,
        foreignKeys = @ForeignKey(entity = Task.class,
                parentColumns = "id",
                childColumns = "maintaskid",
                onDelete = ForeignKey.CASCADE))*/
public class SubTask {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sub_id")
    public int id;

    @ColumnInfo(name = "sub_name")
    private String sub_name;

    @ColumnInfo(name = "sub_Description")
    private String sub_description;

    @ColumnInfo(name = "sub_Date_Time")
    private String sub_date_time;

    @ColumnInfo(name = "maintaskid")
    private int maintask_id;



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

    public int getMaintask_id() {
        return maintask_id;
    }

    public void setMaintask_id(int maintask_id) {
        this.maintask_id = maintask_id;
    }

    public SubTask(String sub_name) {
        this.sub_name = sub_name;
    }

    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
    }
    @Ignore
    public SubTask(String sub_name, String sub_description, String sub_date_time, int maintask_id) {
        this.sub_name = sub_name;
        this.sub_description = sub_description;
        this.sub_date_time = sub_date_time;
        this.maintask_id = maintask_id;
    }
}
