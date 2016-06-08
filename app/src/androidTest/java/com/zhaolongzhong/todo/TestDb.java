package com.zhaolongzhong.todo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zz on 6/6/16.
 */

public class TestDb extends AndroidTestCase {
    private static final String TAG = TestDb.class.getSimpleName();

    private TaskDatabaseHelper taskDatabaseHelper;

    /**
     * This function gets called before each test.
     */
    public void setUp() {
        deleteDatabase();
    }

    private void deleteDatabase() {
        mContext.deleteDatabase(TaskDatabaseHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        Set<String> tableSet = new HashSet<>();
        tableSet.add(TaskDatabaseHelper.TABLE_TASK);
        mContext.deleteDatabase(TaskDatabaseHelper.DATABASE_NAME);
        SQLiteDatabase db = TaskDatabaseHelper.getInstance(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen()); // Check db is open

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: no table created.", cursor.moveToFirst());

        do {
            tableSet.remove(cursor.getString(0));
        } while (cursor.moveToNext());

        assertTrue("Error: not all tables are created.", tableSet.isEmpty());

        // Check if we can query table info
        cursor = db.rawQuery("PRAGMA table_info(" + TaskDatabaseHelper.TABLE_TASK + ")", null);
        assertTrue("Error: we were unable to query database.", cursor.moveToFirst());

        // Check if we have correct columns
        Set<String> columnNameSet = new HashSet<>();
        columnNameSet.add(TaskDatabaseHelper.KEY_TASK_ID);
        columnNameSet.add(TaskDatabaseHelper.KEY_TASK_TITLE);
        columnNameSet.add(TaskDatabaseHelper.KEY_TASK_NOTE);

        do {
            String columnNameFromTable = cursor.getString(cursor.getColumnIndex("name"));
            columnNameSet.remove(columnNameFromTable);
        } while (cursor.moveToNext());

        assertTrue("Error: table task does not contain all the required columns", columnNameSet.isEmpty());

        db.close();
    }

    public void testTaskTable() {
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(mContext);

        // Test insert task
        long rowId = insertTask();
        assertTrue(rowId != -1);

        // Test getTaskById
        Task task = getTaskById(rowId);
        assertTrue(task.getTitle().equals(TestUtilities.getTestTask().getTitle()));

        // Test getAllTask
        insertTask();
        List<Task> tasks = getAllTask();
        assertEquals(2, tasks.size());

        // Test updateTask
        String newTitle = "newTitle";
        task.setTitle(newTitle);
        int result = updateTask(task);
        assertEquals(1, result);

        Task updateTask = getTaskById(rowId);
        assertEquals(newTitle, updateTask.getTitle());

        // Test deleteTask
        result = deleteTaskById(rowId);
        assertEquals(1, result);
        task = getTaskById(rowId);
        assertEquals(null, task);
    }

    private long insertTask() {
        long rowId = taskDatabaseHelper.addTask(TestUtilities.getTestTask());
        return rowId;
    }

    private Task getTaskById(long taskId) {
        return taskDatabaseHelper.getTaskById(taskId);
    }

    private List<Task> getAllTask() {
        return taskDatabaseHelper.getAllTasks();
    }

    private int updateTask(Task task) {
        return taskDatabaseHelper.updateTask(task);
    }

    private int deleteTaskById(long taskId) {
        return taskDatabaseHelper.deleteTaskById(taskId);
    }
}