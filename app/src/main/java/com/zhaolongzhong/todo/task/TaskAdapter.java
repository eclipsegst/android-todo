package com.zhaolongzhong.todo.task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zhaolongzhong.todo.service.model.Task;
import com.zhaolongzhong.todo.service.model.TaskCell;

import java.util.List;

/**
 * Created by zz on 6/6/16.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    private final List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, -1, tasks);
        this.tasks = tasks;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent) {
        TaskCell taskCell = new TaskCell(parent.getContext());
        taskCell.setViewData(tasks.get(position));
        return taskCell;
    }
}
