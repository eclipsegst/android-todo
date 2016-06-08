package com.zhaolongzhong.todo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TaskDatabaseHelper taskDatabaseHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private TextView noTaskTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        noTaskTextView = (TextView) findViewById(R.id.main_activity_no_task_text_view_id);

        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskActivity.newInstance(MainActivity.this);
                overridePendingTransition(R.anim.bottom_in, R.anim.stay);
            }
        });

        taskList = taskDatabaseHelper.getAllTasks();
        noTaskTextView.setVisibility(taskList.size() == 0 ? View.VISIBLE : View.GONE);
        ListView listView = (ListView) findViewById(R.id.main_activity_task_list_view_id);

        taskAdapter = new TaskAdapter(this, taskList);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }

    private void invalidViews() {
        //TODO: Is this the most efficient way?
        taskList = taskDatabaseHelper.getAllTasks();
        taskAdapter.clear();
        taskAdapter.addAll(taskList);
        noTaskTextView.setVisibility(taskList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TaskDetailActivity.newInstance(MainActivity.this, taskList.get(position).getId());
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    };

    private ListView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            taskDatabaseHelper.deleteTaskById(taskList.get(position).getId());
            invalidViews();

            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        invalidViews();
    }
}
