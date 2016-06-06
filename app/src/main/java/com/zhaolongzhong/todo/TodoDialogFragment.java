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

/**
 * Created by zz on 6/6/16.
 */

public class TodoDialogFragment extends DialogFragment {
    private static final String TAG = TodoDialogFragment.class.getSimpleName();

    private static String POSITION = "position";
    private static String TITLE = "title";

    private int position;
    private String title;

    private EditText titleEditText;
    private Button cancelButton;
    private Button okButton;

    private TodoDialogFragmentCallback todoDialogFragmentCallback;

    public static TodoDialogFragment newInstance(int position, String title) {
        TodoDialogFragment todoDialogFragment = new TodoDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putString(TITLE, title);
        todoDialogFragment.setArguments(bundle);

        return todoDialogFragment;
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
        titleEditText = (EditText) view.findViewById(R.id.todo_dialog_title_edit_text_id);
        cancelButton = (Button) view.findViewById(R.id.todo_dialog_cancel_button_id);
        okButton = (Button) view.findViewById(R.id.todo_dialog_ok_button_id);

        cancelButton.setOnClickListener(cancelOnClickListener);
        okButton.setOnClickListener(okOnClickListener);

        Bundle bundle = getArguments();
        position = bundle.getInt(POSITION);
        title = bundle.getString(TITLE);
        titleEditText.setText(title);

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
            if (todoDialogFragmentCallback != null) {
                todoDialogFragmentCallback.onDialogDismiss(position, titleEditText.getText().toString());
            }
        }
    };

    public void setTodoDialogFragmentCallback(TodoDialogFragmentCallback todoDialogFragmentCallback) {
        this.todoDialogFragmentCallback = todoDialogFragmentCallback;
    }

    public interface TodoDialogFragmentCallback {
        void onDialogDismiss(int position, String title);
    }
}
