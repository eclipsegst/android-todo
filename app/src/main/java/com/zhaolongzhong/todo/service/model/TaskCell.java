package com.zhaolongzhong.todo.service.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaolongzhong.todo.main.MainActivity;
import com.zhaolongzhong.todo.R;
import com.zhaolongzhong.todo.task.TaskDetailActivity;
import com.zhaolongzhong.todo.data.TaskDatabaseHelper;

/**
 * Created by zz on 6/12/16.
 */

public class TaskCell extends RelativeLayout {
    private static final String TAG = TaskCell.class.getSimpleName();

    private Task task;

    private TextView titleTextView;
    private TextView dueDateTextView;
    private TextView priorityTextView;
    private CheckBox statusCheckbox;
    private ImageView navigationImage;

    public TaskCell(Context context) {
        super(context);
        initialize();
    }

    public TaskCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TaskCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.task_cell, this);

        titleTextView = (TextView) findViewById(R.id.task_cell_title_text_view_id);
        dueDateTextView = (TextView) findViewById(R.id.task_cell_due_date_text_view_id);
        priorityTextView = (TextView) findViewById(R.id.task_cell_priority_text_view_id);
        statusCheckbox = (CheckBox) findViewById(R.id.task_cell_status_check_box_id);
        navigationImage = (ImageView) findViewById(R.id.task_cell_navigation_image_view_id);

        titleTextView.setOnClickListener(onClickListener);
        navigationImage.setOnClickListener(onClickListener);
        statusCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void setViewData(Task task) {
        this.task = task;

        titleTextView.setText(task.getTitle());
        dueDateTextView.setText(task.getDueDate());
        priorityTextView.setText(task.getPriority());
        statusCheckbox.setChecked(task.isStatus());
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            task.setStatus(isChecked);
            TaskDatabaseHelper taskDatabaseHelper = TaskDatabaseHelper.getInstance(getContext());
            taskDatabaseHelper.updateTask(task);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TaskDetailActivity.newInstance(getContext(), task.getId());
            ((MainActivity)getContext()).overridePendingTransition(R.anim.right_in, R.anim.left_out);;
        }
    };
}
