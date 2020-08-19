package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements TaskListAdapter.OnTaskListClickListener {
    private TextView pseudo;
    private Button logOut;
    private RecyclerView recyclerViewListToSelect;
    private RecyclerView.LayoutManager layoutManagerListToSelect;
    private TaskListAdapter taskListAdapter;
    private FirebaseAuth mAuth;
    private ArrayList<TaskList> userTasksList;

    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userTasksList = new ArrayList<>();
        pseudo = findViewById(R.id.dashboard_userPseudo);
        logOut = findViewById(R.id.dashboard_logout);

        //get infos from database (ex: user info)
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mPseudo = database.getReference("user_" + mAuth.getUid()).child("pseudo");

        //PSEUDO
        mPseudo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String valuePseudo = snapshot.getValue(String.class);
                pseudo.setText(valuePseudo);
                Log.d(TAG, "onDataChange: " + valuePseudo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value for the user Pseudo", error.toException());
            }
        });

        // TASKS LIST for the recycler view
        DatabaseReference mTasksList = database.getReference("user_" + mAuth.getUid()).child("tasks_list");
        mTasksList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTasksList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    userTasksList.add(snap.getValue(TaskList.class));
                }
                sendSomeToThatRecyclerViewBiatch();
                Log.d(TAG, "userTasksList final size : " + String.valueOf(userTasksList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to access tasks list", error.toException());
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent login = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
//
//        sendSomeToThatRecyclerViewBiatch();
    }

    @Override
    public void onTaskClick(int position) {
        Log.d(TAG, "DashboardActivity has seen a click on item " + position);

        Intent taskIntent = new Intent(this, SelectedListActivity.class);
        taskIntent.putExtra("taskLists_number", String.valueOf(position));
//        taskIntent.putExtra("selected_list", (Parcelable) userTasksList.get(position));
        startActivity(taskIntent);


    }

    private void sendSomeToThatRecyclerViewBiatch() {
        if (userTasksList != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            recyclerViewListToSelect = findViewById(R.id.dashboard_recyclerView);
            recyclerViewListToSelect.setHasFixedSize(true);
            layoutManagerListToSelect = new LinearLayoutManager(DashboardActivity.this);
            recyclerViewListToSelect.setLayoutManager(layoutManagerListToSelect);
            taskListAdapter = new TaskListAdapter(userTasksList, this);
            recyclerViewListToSelect.setAdapter(taskListAdapter);
        } else {
            Toast.makeText(this, "No list yet", Toast.LENGTH_SHORT).show();
        }
    }
}