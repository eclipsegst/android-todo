package com.zhaolongzhong.todo;

import android.test.AndroidTestCase;

import com.zhaolongzhong.todo.service.model.Task;

/**
 * Created by zz on 6/6/16.
 */

public class TestUtilities extends AndroidTestCase{
    private static final String TAG = TestUtilities.class.getSimpleName();

    public static Task getTestTask() {
        Task task = new Task();
        task.setTitle("task test Task title");
        task.setNote("task test Task note");

        return task;
    }

}
