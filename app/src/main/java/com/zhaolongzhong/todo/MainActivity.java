package com.zhaolongzhong.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String TODO_FILENAME = "todo.txt";

    private ListView listView;
    private EditText editText;
    private Button addButton;

    private List<String> todoList;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        readItems();

        listView = (ListView) findViewById(R.id.main_activity_todo_list_view_id);
        editText = (EditText) findViewById(R.id.main_activity_edit_text_id);
        addButton = (Button) findViewById(R.id.main_activity_add_button_id);
        addButton.setOnClickListener(addOnClickListener);

        todoAdapter = new TodoAdapter(this, todoList);
        listView.setAdapter(todoAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String itemText = editText.getText().toString();
            if (itemText.isEmpty()) {
                return;
            }

            todoAdapter.add(itemText);
            editText.setText("");
            writeItems();
        }
    };

    private ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TodoDialogFragment todoDialogFragment = TodoDialogFragment.newInstance(position, todoList.get(position));
            todoDialogFragment.setTodoDialogFragmentCallback(todoDialogFragmentCallback);
            todoDialogFragment.show(getFragmentManager(), TodoDialogFragment.class.getSimpleName());
        }
    };

    private ListView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            todoList.remove(position);
            todoAdapter.notifyDataSetChanged();
            writeItems();
            return true;
        }
    };


    /**
     * TodoDialogFragmentCallback, update item in list when dialog dismiss
     */
    private TodoDialogFragment.TodoDialogFragmentCallback todoDialogFragmentCallback = new TodoDialogFragment.TodoDialogFragmentCallback() {
        @Override
        public void onDialogDismiss(int position, String title) {
            todoList.set(position, title);
            todoAdapter.notifyDataSetChanged();
            writeItems();
        }
    };

    /**
     * Read data from file
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILENAME);
        try {
            todoList = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            todoList = new ArrayList<>();
        }
    }

    /**
     * Write to-do list to file
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILENAME);
        try {
            FileUtils.writeLines(todoFile, todoList);
        } catch (IOException e) {
            Log.e(TAG, "Error in writeItems.", e);
        }
    }
}
