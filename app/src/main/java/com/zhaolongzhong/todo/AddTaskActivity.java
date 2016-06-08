package com.zhaolongzhong.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.model.Task;

public class AddTaskActivity extends AppCompatActivity {
    private static final String TAG = AddTaskActivity.class.getSimpleName();

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }

    private TaskDatabaseHelper taskDatabaseHelper;

    private EditText titleEditText;
    private EditText noteEditText;

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

        titleEditText = (EditText) findViewById(R.id.add_task_activity_title_edit_text_id);
        noteEditText = (EditText) findViewById(R.id.add_task_activity_note_edit_text_id);
        titleEditText.requestFocus();
    }

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
        Task task = new Task();
        task.setTitle(titleEditText.getText().toString());
        task.setNote(noteEditText.getText().toString());
        taskDatabaseHelper.addTask(task);
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
