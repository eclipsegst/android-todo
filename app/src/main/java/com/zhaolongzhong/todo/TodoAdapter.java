package com.zhaolongzhong.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zz on 6/6/16.
 */

public class TodoAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> todos;

    public TodoAdapter(Context context, List<String> todos) {
        super(context, -1, todos);
        this.context = context;
        this.todos = todos;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.todo_cell, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.todo_cell_title_text_view_id);

        titleTextView.setText(todos.get(position));
        return rowView;
    }
}
