package com.example.to_do.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "Date")
    private String date;
    @ColumnInfo(name = "img_path")
    private String img_path;

    @ColumnInfo(name ="word_order")
    private int order;

    @Ignore
    public Task(int id, String name, String description, String date, String img_path) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.img_path = img_path;
    }



    @Ignore
    public Task(int order) {
        this.order = order;
    }

    public Task(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    @Ignore
    public Task(String name, String description, String date, int order) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.order = order;
    }

    @Ignore
    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Ignore
    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Ignore
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Ignore
    public Task(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;


    }
    @Ignore
    public Task(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Ignore
    public Task(String name, String description, String date, String img_path) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.img_path = img_path;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
