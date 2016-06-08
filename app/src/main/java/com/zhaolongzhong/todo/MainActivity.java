package com.zhaolongzhong.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editText;

    private TaskDatabaseHelper taskDatabaseHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);

        taskList = taskDatabaseHelper.getAllTasks();

        ListView listView = (ListView) findViewById(R.id.main_activity_task_list_view_id);
        editText = (EditText) findViewById(R.id.main_activity_edit_text_id);
        Button addButton = (Button) findViewById(R.id.main_activity_add_button_id);
        addButton.setOnClickListener(addOnClickListener);

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
        editText.setText("");
    }

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemText = editText.getText().toString();
            if (itemText.isEmpty()) {
                return;
            }

            Task task = new Task();
            task.setTitle(itemText);

            taskDatabaseHelper.addTask(task);
            invalidViews();
        }
    };

    private ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(taskList.get(position).getId());
            updateDialogFragment.setTodoDialogFragmentCallback(todoDialogFragmentCallback);
            updateDialogFragment.show(getFragmentManager(), UpdateDialogFragment.class.getSimpleName());
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

    /**
     * TodoDialogFragmentCallback, update task list only when task content changed
     */
    private UpdateDialogFragment.TodoDialogFragmentCallback todoDialogFragmentCallback = new UpdateDialogFragment.TodoDialogFragmentCallback() {
        @Override
        public void onUpdateFinished() {
            invalidViews();
        }
    };
}
