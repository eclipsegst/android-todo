package com.zhaolongzhong.todo.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhaolongzhong.todo.R;
import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.service.model.Task;
import com.zhaolongzhong.todo.task.AddTaskActivity;
import com.zhaolongzhong.todo.task.TaskAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String STATE_SPINNER = "spinnerState";
    private TaskDatabaseHelper taskDatabaseHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    @BindView(R.id.main_activity_toolbar_spinner_id) Spinner spinner;
    @BindView(R.id.main_activity_no_task_text_view_id) TextView noTaskTextView;
    @BindView(R.id.main_activity_toolbar_id) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.main_activity_task_list_view_id) ListView listView;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);
        taskList = taskDatabaseHelper.getAllTasks();

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_status_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.getBackground().setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        spinner.setSelection(sharedPref.getInt(STATE_SPINNER, 0));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskActivity.newInstance(MainActivity.this);
                overridePendingTransition(R.anim.bottom_in, R.anim.stay);
            }
        });

        noTaskTextView.setVisibility(taskList.size() == 0 ? View.VISIBLE : View.GONE);

        taskAdapter = new TaskAdapter(this, taskList);
        listView.setAdapter(taskAdapter);
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }

    private void invalidateViews() {
        //TODO: Is this the most efficient way?
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                taskList = taskDatabaseHelper.getAllTasks();
                break;
            case 1:
                taskList = taskDatabaseHelper.getAllTasksByStatus(false);
                break;
            case 2:
                taskList = taskDatabaseHelper.getAllTasksByStatus(true);
                break;
        }

        taskAdapter.clear();
        taskAdapter.addAll(taskList);
        noTaskTextView.setVisibility(taskList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private ListView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            taskDatabaseHelper.deleteTaskById(taskList.get(position).getId());
            invalidateViews();

            return true;
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(STATE_SPINNER, position);
            editor.apply();

            invalidateViews();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        invalidateViews();
    }
}
