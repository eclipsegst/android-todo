package com.zhaolongzhong.todo.service.model;

import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by zz on 6/6/16.
 */

@RealmClass
public class Task implements RealmModel {
    private static final String TAG = Task.class.getSimpleName();

    public static final String ID_KEY = "id";
    public static final String DUE_DATE_KEY = "dueDate";
    public static final String STATUS_KEY = "isComplete";

    @PrimaryKey
    private String id;
    private String title;
    private String note;
    private String dueDate;
    private String priority;
    private boolean isComplete = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        this.isComplete = complete;
    }

    public static RealmResults<Task> getAllTasks() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Task.class).findAllSorted(Task.DUE_DATE_KEY);
    }

    public static RealmResults<Task> getAllTasks(boolean isComplete) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Task.class)
                .equalTo(Task.STATUS_KEY, isComplete)
                .findAllSorted(Task.DUE_DATE_KEY);
    }

    public static @Nullable Task getTaskById(String taskId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Task.class).equalTo(Task.ID_KEY, taskId).findFirst();
    }
}
