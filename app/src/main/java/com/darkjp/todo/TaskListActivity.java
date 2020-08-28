package com.darkjp.todo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {
    private EditText tasksListTitle, newTaskTitleInput, newTaskDescriptionInput;
    private Button newTask, save;
    private LinearLayout newTaskZone;
    private TaskList newTasksList = new TaskList();
    private User userUpdate = new User();

    private static final String TAG = "TaskListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        tasksListTitle = findViewById(R.id.task_list_tasksListTitle);
        newTask = findViewById(R.id.task_list_addNewTask);
        newTaskZone = findViewById(R.id.task_list_newTaskZone);


        newTasksList.setCreator(FirebaseAuth.getInstance().getUid());
        newTasksList.setId(UUID.randomUUID().toString());
        newTasksList.setTask(new ArrayList<Task>());
        userUpdate.setTaskList(new ArrayList<String>());

        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tasksListTitle.getText() != null && !tasksListTitle.getText().toString().equals("")) {
                    newTasksList.setTitle(tasksListTitle.getText().toString());
                    addNewInputForNewTask();
                } else {
                    Toast.makeText(TaskListActivity.this, "Enter a title for the tasks list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewInputForNewTask() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 10);
        TextView newTaskTitle = new TextView(TaskListActivity.this);
        newTaskTitle.setLayoutParams(params);
        newTaskTitle.setText("Task title: ");
        newTaskTitle.setTypeface(Typeface.DEFAULT_BOLD);
        newTaskTitle.layout(5, 5, 5, 5);
        newTaskTitleInput = new EditText(TaskListActivity.this);
        newTaskTitleInput.setLayoutParams(params);
        newTaskTitleInput.setText("");
        newTaskTitleInput.setBackgroundColor(Color.parseColor("#FFD5D5D6"));
        TextView newTaskDescription = new TextView(TaskListActivity.this);
        newTaskDescription.setLayoutParams(params);
        newTaskDescription.setText("Task description: ");
        newTaskDescription.setTypeface(Typeface.DEFAULT_BOLD);
        newTaskDescription.layout(5, 5, 5, 5);
        newTaskDescriptionInput = new EditText(TaskListActivity.this);
        newTaskDescriptionInput.setLayoutParams(params);
        newTaskDescriptionInput.setText("");
        newTaskDescriptionInput.setBackgroundColor(Color.parseColor("#FFD5D5D6"));
        Button addTaskButton = new Button(TaskListActivity.this);
        addTaskButton.setLayoutParams(params);
        addTaskButton.setText("ADD");
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

        newTaskZone.addView(newTaskTitle);
        newTaskZone.addView(newTaskTitleInput);
        newTaskZone.addView(newTaskDescription);
        newTaskZone.addView(newTaskDescriptionInput);
        newTaskZone.addView(addTaskButton);
    }

    private void saveTask() {
        final Task newTask = new Task();
        newTask.setCreator(FirebaseAuth.getInstance().getUid());
        newTask.setTitle(newTaskTitleInput.getText().toString());
        newTask.setDescription(newTaskDescriptionInput.getText().toString());
        newTask.setDone(false);
        if (newTasksList.getTask() != null && !newTasksList.getTask().contains(newTask)) {
            newTasksList.getTask().add(newTask);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mTasklist = database.getReference("tasksList/" + newTasksList.getId());
        mTasklist.setValue(newTasksList);
    }
}