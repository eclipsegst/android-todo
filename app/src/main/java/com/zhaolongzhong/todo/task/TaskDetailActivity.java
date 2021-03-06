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
import com.zhaolongzhong.todo.service.Priority;
import com.zhaolongzhong.todo.service.model.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = TaskDetailActivity.class.getSimpleName();

    private static final String TASK_ID = "taskId";

    private Task task;
    private String taskId;

    @BindView(R.id.task_detail_activity_title_text_view_id) TextView titleTextView;
    @BindView(R.id.task_detail_activity_note_text_view_id) TextView noteTextView;
    @BindView(R.id.task_detail_activity_due_date_text_view_id) TextView dueDateTextView;
    @BindView(R.id.task_detail_activity_priority_spinner_id) Spinner prioritySpinner;
    @BindView(R.id.task_detail_activity_status_check_box_id) CheckBox statusCheckbox;

    public static void newInstance(Context context, String taskId) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(TASK_ID, taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_activity);
        ButterKnife.bind(this);

        setTitle(getString(R.string.task_detail_activity));

        taskId = getIntent().getStringExtra(TASK_ID);
        task = Task.getTaskById(taskId);

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

    private void invalidateViews() {;
        titleTextView.setText(task.getTitle());
        noteTextView.setText(task.getNote());
        dueDateTextView.setText(task.getDueDate());
        Priority priority = Priority.instanceFromName(task.getPriority());
        prioritySpinner.setSelection(priority.getId());
        statusCheckbox.setChecked(task.isComplete());
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

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            task.setDueDate(dueDate);;
            realm.copyToRealmOrUpdate(task);
            realm.commitTransaction();
            realm.close();
            invalidateViews();
        }
    };

    /**
     * TodoDialogFragmentCallback, update task list only when task content changed
     */
    private UpdateDialogFragment.TodoDialogFragmentCallback todoDialogFragmentCallback = new UpdateDialogFragment.TodoDialogFragmentCallback() {
        @Override
        public void onUpdateFinished() {
            updateTask();
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Priority priority = Priority.instanceFromId(position);

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            task.setPriority(priority.getName());
            realm.copyToRealmOrUpdate(task);
            realm.commitTransaction();
            realm.close();
            invalidateViews();
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
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            task.setComplete(isChecked);
            realm.copyToRealmOrUpdate(task);
            realm.commitTransaction();
            realm.close();
            invalidateViews();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void updateTask() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
        realm.close();
        invalidateViews();
    }
}
