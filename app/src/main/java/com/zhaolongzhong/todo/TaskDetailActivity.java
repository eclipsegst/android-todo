package com.zhaolongzhong.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.model.Task;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = TaskDetailActivity.class.getSimpleName();

    private static final String TASK_ID = "taskId";

    private TaskDatabaseHelper taskDatabaseHelper;
    private Task task;
    private long taskId;

    private TextView titleTextView;
    private TextView noteTextView;
    private TextView dueDateTextView;
    private TextView priorityTextView;
    private TextView statusTextView;

    public static void newInstance(Context context, long taskId) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(TASK_ID, taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_activity);

        setTitle(getString(R.string.task_detail_activity));

        titleTextView = (TextView) findViewById(R.id.task_detail_activity_title_text_view_id);
        noteTextView = (TextView) findViewById(R.id.task_detail_activity_note_text_view_id);
        dueDateTextView = (TextView) findViewById(R.id.task_detail_activity_due_date_text_view_id);
        priorityTextView = (TextView) findViewById(R.id.task_detail_activity_priority_text_view_id);
        statusTextView = (TextView) findViewById(R.id.task_detail_activity_status_text_view_id);

        titleTextView.setOnClickListener(titleOnClickListener);
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);
        taskId = getIntent().getLongExtra(TASK_ID, -1);
        invalidViews();
    }

    private void invalidViews() {
        task = taskDatabaseHelper.getTaskById(taskId);
        titleTextView.setText(task.getTitle());
        noteTextView.setText(task.getNote());
        dueDateTextView.setText(task.getDueDate());
        priorityTextView.setText(task.getPriority());
        statusTextView.setText(task.isStatus()? "Done" : "Undone");

    }

    private View.OnClickListener titleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(taskId);
            updateDialogFragment.setTodoDialogFragmentCallback(todoDialogFragmentCallback);
            updateDialogFragment.show(getFragmentManager(), UpdateDialogFragment.class.getSimpleName());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void close() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
