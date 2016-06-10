package com.zhaolongzhong.todo;

import android.content.Context;

import com.zhaolongzhong.todo.main.TodoApplication;

/**
 * Created by zz on 6/9/16.
 */

public enum Priority {
    High(0, R.string.add_priority_high),
    Medium(1, R.string.add_priority_medium),
    Low(2, R.string.add_priority_low);

    private int id;
    private int nameId;

    Priority(int id, int nameId) {
        this.id = id;
        this.nameId = nameId;
    }

    public int getId() {
        return this.id;
    }

    public int getNameId() {
        return this.nameId;
    }

    public String getName() {
        Context context = TodoApplication.getApplication();
        return context.getString(nameId);
    }

    public static Priority instanceFromId(int id) {
        for (Priority priority : Priority.values()) {
            if (priority.getId() == id) {
                return priority;
            }
        }

        return null;
    }

    public static Priority instanceFromName(String name) {
        Context context = TodoApplication.getApplication();
        for (Priority priority : Priority.values()) {
            if (context.getString(priority.getNameId()).equals(name)) {
                return priority;
            }
        }

        return null;
    }
}
