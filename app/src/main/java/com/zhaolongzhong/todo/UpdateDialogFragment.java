package com.zhaolongzhong.todo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zhaolongzhong.todo.data.TaskDatabaseHelper;
import com.zhaolongzhong.todo.model.Task;

/**
 * Created by zz on 6/6/16.
 */

public class UpdateDialogFragment extends DialogFragment {
    private static final String TAG = UpdateDialogFragment.class.getSimpleName();

    private static String TASK_ID = "taskId";

    private EditText titleEditText;

    private TodoDialogFragmentCallback todoDialogFragmentCallback;
    private TaskDatabaseHelper taskDatabaseHelper;
    private Task task;

    public static UpdateDialogFragment newInstance(long taskId) {
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(TASK_ID, taskId);
        updateDialogFragment.setArguments(bundle);

        return updateDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todo_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(getActivity());

        titleEditText = (EditText) view.findViewById(R.id.todo_dialog_title_edit_text_id);
        Button cancelButton = (Button) view.findViewById(R.id.todo_dialog_cancel_button_id);
        Button okButton = (Button) view.findViewById(R.id.todo_dialog_ok_button_id);

        cancelButton.setOnClickListener(cancelOnClickListener);
        okButton.setOnClickListener(okOnClickListener);

        long taskId = getArguments().getLong(TASK_ID);
        task = taskDatabaseHelper.getTaskById(taskId);
        titleEditText.setText(task.getTitle());

        titleEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };

    private View.OnClickListener okOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
            task.setTitle(titleEditText.getText().toString());
            taskDatabaseHelper.updateTask(task);
            todoDialogFragmentCallback.onUpdateFinished();
        }
    };

    public void setTodoDialogFragmentCallback(TodoDialogFragmentCallback todoDialogFragmentCallback) {
        this.todoDialogFragmentCallback = todoDialogFragmentCallback;
    }

    public interface TodoDialogFragmentCallback {
        void onUpdateFinished();
    }
}
