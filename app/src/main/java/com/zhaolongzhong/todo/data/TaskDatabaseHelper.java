package com.zhaolongzhong.todo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zhaolongzhong.todo.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 6/6/16.
 */

public class TaskDatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = TaskDatabaseHelper.class.getSimpleName();

    // Database info
    public static final String DATABASE_NAME = "task.db";
    public static final int DATABASE_VERSION = 3;

    // Table names
    public static final String TABLE_TASK = "task";

    // Task table columns
    public static final String KEY_TASK_ID = "id";
    public static final String KEY_TASK_TITLE = "title";
    public static final String KEY_TASK_NOTE = "note";
    public static final String KEY_TASK_DUE_DATE = "dueDate";
    public static final String KEY_TASK_PRIORITY = "priority";
    public static final String KEY_TASK_STATUS = "status";

    private static TaskDatabaseHelper taskDatabaseHelper;

    public static synchronized TaskDatabaseHelper getInstance(Context context) {
        if (taskDatabaseHelper == null) {
            taskDatabaseHelper = new TaskDatabaseHelper(context.getApplicationContext());
        }

        return taskDatabaseHelper;
    }

    private TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK +
                "(" +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_TASK_TITLE + " TEXT," +
                    KEY_TASK_NOTE + " TEXT," +
                    KEY_TASK_DUE_DATE + " TEXT," +
                    KEY_TASK_PRIORITY + " TEXT," +
                    KEY_TASK_STATUS + " INTEGER" +

                ")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    /**
     * Upgrade database when the database needs to be upgraded.
     * This method will only be called if a database already exists on disk with the same DATABASE_NAME,
     * but the current DATABASE_VERSION is different from the previous version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
            onCreate(db);
        }
    }

    /**
     * Insert a new task into database
     */
    public long addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        long rowId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_TITLE, task.getTitle());
            values.put(KEY_TASK_NOTE, task.getNote());
            values.put(KEY_TASK_DUE_DATE, task.getDueDate());
            values.put(KEY_TASK_PRIORITY, task.getPriority());
            values.put(KEY_TASK_STATUS, 0);

            rowId = db.insert(TABLE_TASK, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add task to database.", e);
        } finally {
            db.close();
        }

        return rowId;
    }

    /**
     * Get a task by id
     */
    public Task getTaskById(long taskId) {
        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s where id = %s" , TABLE_TASK, taskId);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                Task newTask = new Task();
                newTask.setId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));
                newTask.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE)));
                newTask.setNote(cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTE)));
                newTask.setDueDate(cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE_DATE)));
                newTask.setPriority(cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY)));
                newTask.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TASK_STATUS)) != 0);
                return newTask;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get task from database.", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    /**
     * Return a list of task
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s" , TABLE_TASK);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task();
                    newTask.setId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));
                    newTask.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE)));
                    newTask.setNote(cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTE)));
                    newTask.setDueDate(cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE_DATE)));
                    newTask.setPriority(cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY)));
                    newTask.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TASK_STATUS)) != 0);
                    tasks.add(newTask);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get tasks from database.", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return tasks;
    }

    /**
     * Update a task
     */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.getTitle());
        values.put(KEY_TASK_NOTE, task.getNote());

        return db.update(TABLE_TASK, values, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    /**
     * Delete a task by id
     */
    public int deleteTaskById(long taskId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(TABLE_TASK, KEY_TASK_ID + " = ?",
                    new String[] { String.valueOf(taskId) });
        } catch (Exception e) {
            Log.e(TAG, "Error while trying delete a task.", e);
        } finally {
            db.close();
        }

        return -1;
    }
}
