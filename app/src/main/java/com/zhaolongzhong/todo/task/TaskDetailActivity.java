package com.zhaolongzhong.todo.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhaolongzhong.todo.R;
import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.service.model.Task;
import com.zhaolongzhong.todo.service.Priority;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = TaskDetailActivity.class.getSimpleName();

    private static final String TASK_ID = "taskId";

    private TaskDatabaseHelper taskDatabaseHelper;
    private Task task;
    private long taskId;

    private TextView titleTextView;
    private TextView noteTextView;
    private TextView dueDateTextView;
    private Spinner prioritySpinner;
    private CheckBox statusCheckbox;

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

        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);
        taskId = getIntent().getLongExtra(TASK_ID, -1);

        titleTextView = (TextView) findViewById(R.id.task_detail_activity_title_text_view_id);
        noteTextView = (TextView) findViewById(R.id.task_detail_activity_note_text_view_id);
        dueDateTextView = (TextView) findViewById(R.id.task_detail_activity_due_date_text_view_id);
        statusCheckbox = (CheckBox) findViewById(R.id.task_detail_activity_status_check_box_id);
        prioritySpinner = (Spinner) findViewById(R.id.task_detail_activity_priority_spinner_id);

        titleTextView.setOnClickListener(textViewOnClickListener);
        noteTextView.setOnClickListener(textViewOnClickListener);
        dueDateTextView.setOnClickListener(dueDateOnClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(onItemSelectedListener);

        invalidateViews();
    }

    private void invalidateViews() {
        task = taskDatabaseHelper.getTaskById(taskId);
        titleTextView.setText(task.getTitle());
        noteTextView.setText(task.getNote());
        dueDateTextView.setText(task.getDueDate());
        Priority priority = Priority.instanceFromName(task.getPriority());
        prioritySpinner.setSelection(priority.getId());
        statusCheckbox.setChecked(task.isStatus());
        statusCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(taskId,
                    textView.equals(titleTextView) ? UpdateDialogFragment.UPDATE_TITLE : UpdateDialogFragment.UPDATE_NOTE);
            updateDialogFragment.setTodoDialogFragmentCallback(todoDialogFragmentCallback);
            updateDialogFragment.show(getFragmentManager(), UpdateDialogFragment.class.getSimpleName());
        }
    };

    private View.OnClickListener dueDateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setDatePickerCallback(datePickerCallback);
            datePickerFragment.show(getFragmentManager(), DatePickerFragment.class.getSimpleName());
        }
    };

    private DatePickerFragment.DatePickerCallback datePickerCallback = new DatePickerFragment.DatePickerCallback() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            DateFormat format = new SimpleDateFormat(getString(R.string.due_date_format), Locale.ENGLISH);
            String dueDate = format.format(calendar.getTime());
            dueDateTextView.setText(dueDate);
            task.setDueDate(dueDate);
            taskDatabaseHelper.updateTask(task);
            invalidateViews();
        }
    };

    /**
     * TodoDialogFragmentCallback, update task list only when task content changed
     */
    private UpdateDialogFragment.TodoDialogFragmentCallback todoDialogFragmentCallback = new UpdateDialogFragment.TodoDialogFragmentCallback() {
        @Override
        public void onUpdateFinished() {
            invalidateViews();
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Priority priority = Priority.instanceFromId(position);
            task.setPriority(priority.getName());
            taskDatabaseHelper.updateTask(task);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            task.setStatus(isChecked);
            taskDatabaseHelper.updateTask(task);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
