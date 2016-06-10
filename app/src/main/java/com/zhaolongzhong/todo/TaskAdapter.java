package com.zhaolongzhong.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zhaolongzhong.todo.model.Task;

import java.util.List;

/**
 * Created by zz on 6/6/16.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, -1, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_cell, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.task_cell_title_text_view_id);
        TextView dueDateTextView = (TextView) rowView.findViewById(R.id.task_cell_due_date_text_view_id);
        TextView priorityTextView = (TextView) rowView.findViewById(R.id.task_cell_priority_text_view_id);

        titleTextView.setText(tasks.get(position).getTitle());
        dueDateTextView.setText(tasks.get(position).getDueDate());
        priorityTextView.setText(tasks.get(position).getPriority());

        return rowView;
    }
}
