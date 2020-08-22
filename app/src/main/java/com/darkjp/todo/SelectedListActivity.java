package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
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
    private String selectedList = "";

    private static final String TAG = "SelectedListActivity";

    public SelectedListActivity() {
    }


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

        if (getIntent().hasExtra("taskListIndex")) {
            listId = getIntent().getStringExtra("taskListIndex");


            DatabaseReference mUser = database.getReference("user/" + user.getUid());
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pseudo.setText(snapshot.child("pseudo").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final DatabaseReference mTaskList = database.getReference("tasksList/" + listId);
            mTaskList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("snapshot " + snapshot.getValue());
                    if (tasks != null) {
                        tasks.clear();
                        title.setText(snapshot.child("title").getValue().toString());

                        for (DataSnapshot snap : snapshot.child("task").getChildren()) {
                            tasks.add(snap.getValue(Task.class));
                            sendSomeToThatRecyclerViewBiatch(tasks);
                        }
                        if (snapshot.child("participant") != null) {
                            for (DataSnapshot snapParticipant : snapshot.child("participant").getChildren()) {
                                participants.setText("");
                                if (tasks != null) {
                                    if (!snapParticipant.getKey().equals(user.getUid())) {
                                        DatabaseReference mPseudo = database.getReference("user/" + snapParticipant.getKey()).child("pseudo");
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
                                participants.setText("Participant(s):\n");
                            }
                        }
                    }
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
//        Toast.makeText(this, "CLICK CLICK "+ listId, Toast.LENGTH_SHORT).show();
        Intent taskToBeDone = new Intent(this, TaskActivity.class);
        taskToBeDone.putExtra("task", String.valueOf(position));
        taskToBeDone.putExtra("taskListIndex", listId);
        startActivity(taskToBeDone);
    }
}