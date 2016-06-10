package com.zhaolongzhong.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.model.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private static final String TAG = AddTaskActivity.class.getSimpleName();

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }

    private TaskDatabaseHelper taskDatabaseHelper;

    private EditText titleEditText;
    private EditText noteEditText;
    private TextView dueDateTextView;
    private Spinner prioritySpinner;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.create_task));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        setSupportActionBar(toolbar);
        // Place this line after setSupportActionBar, order matters
        toolbar.setNavigationOnClickListener(navigationOnClickListener);

        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this);
        task = new Task();

        titleEditText = (EditText) findViewById(R.id.add_task_activity_title_edit_text_id);
        noteEditText = (EditText) findViewById(R.id.add_task_activity_note_edit_text_id);
        dueDateTextView = (TextView) findViewById(R.id.add_task_activity_due_date_edit_text_id);
        prioritySpinner = (Spinner) findViewById(R.id.add_task_activity_priority_spinner_id);

        titleEditText.requestFocus();
        dueDateTextView.setOnClickListener(dueDateOnClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(onItemSelectedListener);
    }

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
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Show alert to remind user unsaved changed.
     */
    private View.OnClickListener navigationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(AddTaskActivity.this)
                    .setTitle(R.string.unsaved_changes)
                    .setMessage(getString(R.string.unsaved_changes_message))
                    .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            close();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTask() {
        task.setTitle(titleEditText.getText().toString());
        task.setNote(noteEditText.getText().toString());
        task.setPriority(prioritySpinner.getSelectedItem().toString());
        task.setStatus(false);
        taskDatabaseHelper.addTask(task);
        Log.d(TAG, "saveTask: " + task.getPriority());
        close();
    }

    private void close() {
        finish();
        overridePendingTransition(R.anim.stay,R.anim.bottom_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay,R.anim.bottom_out);
    }
}
