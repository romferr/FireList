package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

    private TextView title;
    private Fragment pseudo, participants;
    private Button back;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String listId;

    FragmentTransaction fragmentTransaction, fragmentTransactionFooter;

    private static final String TAG = "SelectedListActivity";

    public SelectedListActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list);

        //fragment Header
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        final HeaderFragment headerFragment = new HeaderFragment();
        fragmentTransaction.replace(R.id.fragment_header, headerFragment);
        fragmentTransaction.commit();
        //fragment Footer
        fragmentTransactionFooter = getSupportFragmentManager().beginTransaction();
        FooterFragment footerFragment = new FooterFragment();
        fragmentTransactionFooter.replace(R.id.fragment_footer, footerFragment);
        fragmentTransactionFooter.commit();

        listId = "";

        title = findViewById(R.id.selectedList_txt_title);
        pseudo = getSupportFragmentManager().findFragmentById(R.id.fragment_header);
        participants = getSupportFragmentManager().findFragmentById(R.id.fragment_header);

//        back = findViewById(R.id.selectedList_btn_back);

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
                    headerFragment.changePseudo(snapshot.child("pseudo").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final DatabaseReference mTaskList = database.getReference("tasksList/" + listId);
            mTaskList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (tasks != null) {
                        tasks.clear();
                        title.setText(snapshot.child("title").getValue().toString());

                        for (DataSnapshot snap : snapshot.child("task").getChildren()) {
                            tasks.add(snap.getValue(Task.class));
                            sendSomeToThatRecyclerViewBiatch(listId, tasks);
                        }
                        if (snapshot.child("participant") != null) {
                            for (DataSnapshot snapParticipant : snapshot.child("participant").getChildren()) {
                                headerFragment.changePseudo("");
                                if (tasks != null) {
                                    if (!snapParticipant.getKey().equals(user.getUid())) {
                                        DatabaseReference mPseudo = database.getReference("user/" + snapParticipant.getKey()).child("pseudo");
                                        mPseudo.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String participants = headerFragment.getParticipants();
                                                headerFragment.addParticipant(participants + snapshot.getValue().toString() + ", ");
//                                                participants.setText(participants.getText().toString() + snapshot.getValue().toString() + ", ");
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                headerFragment.addParticipant("Participant(s):\n");
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
    }

    private void sendSomeToThatRecyclerViewBiatch(String listId, ArrayList<Task> tasks) {
        if (tasks != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            tasksRecyclerView = findViewById(R.id.selectedList_RecyclerView);
            tasksRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(SelectedListActivity.this);
            tasksRecyclerView.setLayoutManager(layoutManager);
            taskAdapter = new TaskAdapter(listId, tasks, this);
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