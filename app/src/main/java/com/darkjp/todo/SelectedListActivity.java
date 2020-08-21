package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectedListActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private TextView title, pseudo, participants;
    private Button back;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String listId;

    private static final String TAG = "SelectedListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list);

        listId = "";

        title = findViewById(R.id.selectedList_txt_title);
        pseudo = findViewById(R.id.selected_list_pseudo);
        participants = findViewById(R.id.selected_list_participants);
        back = findViewById(R.id.selectedList_btn_back);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        tasks = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (getIntent().hasExtra("taskLists_number")) {
            listId = getIntent().getStringExtra("taskLists_number");


            DatabaseReference mUser = database.getReference("user_" + user.getUid());
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pseudo.setText(snapshot.child("pseudo").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference mtasksList = mUser
                    .child("tasks_list")
                    .child(listId);

            mtasksList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (tasks != null) {
                        tasks.clear();

                        title.setText(snapshot.child("title").getValue().toString());

                        for (DataSnapshot snap : snapshot.child("tasks").getChildren()) {
                            tasks.add(snap.getValue(Task.class));
//                            Log.d(TAG, "tasks size is : " + tasks.size());
                            sendSomeToThatRecyclerViewBiatch(tasks);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference mParticipants = mtasksList.child("participants");
            mParticipants.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    participants.setText("");
                    if (tasks != null) {
                        for (DataSnapshot snapId : snapshot.getChildren()) {
                            if (!snapId.child("id").getValue().toString().equals(user.getUid())) {
                                DatabaseReference mPseudo = database.getReference("user_" + snapId.child("id").getValue()).child("pseudo");
                                mPseudo.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        participants.setText(participants.getText().toString() + snapshot.getValue().toString() + ", ");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                    participants.setText("Participant(s): ");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            Toast.makeText(this, "no tasks", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnToDash = new Intent(SelectedListActivity.this, DashboardActivity.class);
                startActivity(returnToDash);
            }
        });
    }

    private void sendSomeToThatRecyclerViewBiatch(ArrayList<Task> tasks) {
        if (tasks != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            tasksRecyclerView = findViewById(R.id.selectedList_RecyclerView);
            tasksRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(SelectedListActivity.this);
            tasksRecyclerView.setLayoutManager(layoutManager);
            taskAdapter = new TaskAdapter(tasks, this);
            tasksRecyclerView.setAdapter(taskAdapter);
        } else {
            Toast.makeText(this, "No list yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskClick(int position) {
//        Toast.makeText(this, "CLICK CLICK open a new Intent. I'm sad, dave. What are you doing, dave ? Daaaave ?", Toast.LENGTH_SHORT).show();
        Intent taskToBeDone = new Intent(this, TaskActivity.class);
        taskToBeDone.putExtra("task", String.valueOf(position));
        taskToBeDone.putExtra("taskListIndex", listId);
        startActivity(taskToBeDone);
    }
}