package com.darkjp.todo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewTaskListAndTasksFragment extends Fragment {
    private static final String TAG = "NewTaskListAndTasksFrag";

    private EditText newListTitle, newTaskTitleInput, newTaskDescriptionInput;
    private HashMap<EditText, EditText> listOfEditText;
    private ImageButton backButton, newTaskFieldButton, saveButton;
    private LinearLayout newTaskZone, menu;
    private TaskList newTasksList = new TaskList();
    private User userUpdate = new User();
    private View view;
    
    private static int newTaskId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listOfEditText = new HashMap<>();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_task_list_and_tasks, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        newListTitle = view.findViewById(R.id.fragment_new_task_list_tasksList_title);
        backButton = view.findViewById(R.id.fragment_new_task_back_button);
        newTaskFieldButton = view.findViewById(R.id.fragment_new_add_task_field);
        saveButton = view.findViewById(R.id.fragment_new_add_task_save_button);
        newTaskZone = view.findViewById(R.id.fragment_new_task_list_new_task_zone);
        menu = view.findViewById(R.id.fragment_new_add_task_menu);

        newTasksList.setCreator(FirebaseAuth.getInstance().getUid());
        newTasksList.setId(UUID.randomUUID().toString());
        newTasksList.setTask(new ArrayList<Task>());
        userUpdate.setTaskList(new ArrayList<String>());

        newTaskFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newListTitle.getText() != null && !newListTitle.getText().toString().equals("")) {
                    newTasksList.setTitle(newListTitle.getText().toString());
                    addNewInputForNewTask();
                } else {
                    Toast.makeText(view.getContext(), "Your list need a title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
                newTaskZone.removeAllViews();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void addNewInputForNewTask() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 10);
        TextView newTaskTitle = new TextView(view.getContext());
        newTaskTitle.setLayoutParams(params);
        newTaskTitle.setText("Task title: ");
        newTaskTitle.setTypeface(Typeface.DEFAULT_BOLD);
        newTaskTitle.layout(5, 5, 5, 5);
        newTaskTitleInput = new EditText(view.getContext());
        newTaskTitleInput.setLayoutParams(params);
        newTaskTitleInput.setText("");
        newTaskTitleInput.setBackgroundColor(Color.parseColor("#FFD5D5D6"));
        TextView newTaskDescription = new TextView(view.getContext());
        newTaskDescription.setLayoutParams(params);
        newTaskDescription.setText("Task description: ");
        newTaskDescription.setTypeface(Typeface.DEFAULT_BOLD);
        newTaskDescription.layout(5, 5, 5, 5);
        newTaskDescriptionInput = new EditText(view.getContext());
        newTaskDescriptionInput.setLayoutParams(params);
        newTaskDescriptionInput.setText("");
        newTaskDescriptionInput.setBackgroundColor(Color.parseColor("#FFD5D5D6"));
        saveButton.setVisibility(View.VISIBLE);
        newTaskZone.addView(newTaskTitle);
        newTaskZone.addView(newTaskTitleInput);
        newTaskZone.addView(newTaskDescription);
        newTaskZone.addView(newTaskDescriptionInput);
        listOfEditText.put(newTaskTitleInput, newTaskDescriptionInput);
    }

    private void saveTask() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mTasklist = database.getReference("tasksList/" + newTasksList.getId());
        for (Map.Entry mapEntry : listOfEditText.entrySet()){
            Task newTask = new Task();
            newTask.setCreator(FirebaseAuth.getInstance().getUid());
            EditText eTitle = (EditText) mapEntry.getKey();
            System.out.println(eTitle);
            newTask.setTitle(eTitle.getText().toString());
            EditText eDesc = (EditText) mapEntry.getValue();
            System.out.println(eDesc);
            newTask.setDescription(eDesc.getText().toString());
            newTask.setDone(false);
            if (newTasksList.getTask() != null && !newTasksList.getTask().contains(newTask)) {
                newTasksList.getTask().add(newTask);
                System.out.println("newTask "+ newTasksList.getTask().size());
            }
        }
        mTasklist.setValue(newTasksList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(view.getContext(), "Save task with success", Toast.LENGTH_SHORT).show();
                newTaskZone.removeAllViews();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}