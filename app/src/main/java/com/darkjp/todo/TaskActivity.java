package com.darkjp.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {
    TextView creator, participants, title, description;
    Button backToTaskList;
    CheckBox isDoneCheckBox;
    String taskIndex, taskListIndex;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        creator = findViewById(R.id.task_creator);
        participants = findViewById(R.id.task_participants);
        title = findViewById(R.id.task_title);
        description = findViewById(R.id.task_txt_description);
        backToTaskList = findViewById(R.id.task_btn_back);
        isDoneCheckBox = findViewById(R.id.task_checkBox);


        //get position of the task from the master intent
        if (getIntent().hasExtra("task")) {
            taskIndex = getIntent().getStringExtra("task");
            taskListIndex = getIntent().getStringExtra("taskListIndex");

            System.out.println();

            //get Creator name
            DatabaseReference mUser = database.getReference("user_" + mAuth.getCurrentUser().getUid());
            DatabaseReference mTask = mUser.child("tasks_list").child(taskListIndex).child("tasks").child(taskIndex);
            mTask.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    title.setText(snapshot.child("nom").getValue().toString());
                    description.setText(snapshot.child("description").getValue().toString());

                    DatabaseReference mCreator = database.getReference("user_" +snapshot.child("creator").getValue().toString()).child("pseudo");
                    mCreator.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            creator.setText(snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            Toast.makeText(this, "NO TASK ! BAST44444RDZZZZ !", Toast.LENGTH_SHORT).show();
        }

        isDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isDoneCheckBox.isChecked()) {
                    final DatabaseReference mDone = database.getReference("user_" + mAuth.getCurrentUser().getUid())
                            .child("tasks_list")
                            .child(taskListIndex)
                            .child("tasks")
                            .child(taskIndex);
                    mDone.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Task task = (Task) snapshot.getValue();
                            System.out.println(task);
                            if (task.isDone())
                                task.setDone(!task.isDone());
                            mDone.setValue(task);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
}