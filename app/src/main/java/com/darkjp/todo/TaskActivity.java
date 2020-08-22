package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
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

public class TaskActivity extends AppCompatActivity {
    private TextView creator, participants, title, description, by;
    private Button backToTaskList;
    private CheckBox isDoneCheckBox;
    private String taskIndex, taskListIndex;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "TaskActivity";
    private String selectedTaskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        by = findViewById(R.id.task_createdBy);
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
            DatabaseReference mSelectedTask = database.getReference("tasksList/" + taskListIndex).child("task/" + taskIndex);
            mSelectedTask.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("SNAP " + snapshot.toString());

                    if (snapshot.child("title").getValue() != null && !snapshot.child("title").getValue().equals(""))
                        title.setText(snapshot.child("title").getValue().toString() + " :");
                    if (snapshot.child("description").getValue() != null && !snapshot.child("description").getValue().equals(""))
                        description.setText(snapshot.child("description").getValue().toString());
                    if (snapshot.child("done").getValue().toString().equals("true"))
                        isDoneCheckBox.setChecked(true);
                    if (snapshot.child("creator").getValue() != null && !snapshot.child("creator").getValue().toString().equals("") && !snapshot.child("creator").getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                        DatabaseReference mCreator = database.getReference("user/" + snapshot.child("creator").getValue().toString()).child("pseudo");
                        mCreator.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null && !snapshot.getValue().equals(""))
                                    creator.setText(snapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        creator.setText("");
                        by.setText("");
                    }
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
                final DatabaseReference mDone = database.getReference("tasksList/" + selectedTaskId).child("task/" + taskIndex);
                mDone.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Task updateTask = new Task();
                        if (snapshot.child("creator").getValue().toString() != null)
                            updateTask.setCreator(snapshot.child("creator").getValue().toString());
                        if (snapshot.child("title").getValue().toString() != null)
                            updateTask.setTitle(snapshot.child("title").getValue().toString());
                        if (snapshot.child("description").getValue().toString() != null)
                            updateTask.setDescription(snapshot.child("description").getValue().toString());
                        if (snapshot.child("done").getValue().toString() != null)
                            updateTask.setDone(isDoneCheckBox.isChecked());
                        mDone.setValue(updateTask);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        backToTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnToTaskList = new Intent(TaskActivity.this, SelectedListActivity.class);
                returnToTaskList.putExtra("taskListIndex", taskListIndex);
                startActivity(returnToTaskList);
            }
        });
    }
}