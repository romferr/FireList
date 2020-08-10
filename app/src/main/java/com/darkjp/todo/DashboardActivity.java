package com.darkjp.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private Button logOut;
    private RecyclerView recyclerViewListToSelect;
    private RecyclerView.LayoutManager layoutManagerListToSelect;
    private RecyclerView recyclerViewSelectedList;
    private RecyclerView.LayoutManager layoutManagerSelectedList;
    private TaskAdapter taskAdapter;
    private TaskListAdapter taskListAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private User user;

    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logOut = findViewById(R.id.dashboard_logout);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent login = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());


        // liste de tâche
        Task tache = new Task("Hellow World", "Jype");
        Task tache2 = new Task("Faire des bisous", "Jype");
        Task tache3 = new Task("Faire des surfs", "Jype");
        Task tache4 = new Task("Faire des nems", "Jype");
        Task tache5 = new Task("Faire à manger", "Jype");
        List<Task>tasksList = new ArrayList<>();
        tasksList.add(tache);
        tasksList.add(tache2);
        tasksList.add(tache3);
        tasksList.add(tache4);
        tasksList.add(tache5);

        TaskList tacheList = new TaskList("Hellow World Liste");
        tacheList.setTasksList(tasksList);
        tacheList.setAuthor("Linus");

        // liste de tâche
        Task tache21 = new Task("surfer", "Jype");
        Task tache22 = new Task("Faire pâtes", "Maman");
        Task tache23 = new Task("Ranger le sable", "JC");
        Task tache24 = new Task("Courir", "JC");
        List<Task>tasksList2 = new ArrayList<>();
        tasksList2.add(tache21);
        tasksList2.add(tache22);
        tasksList2.add(tache23);
        tasksList2.add(tache24);

        TaskList tacheList2 = new TaskList("Seconde Liste");
        tacheList2.setTasksList(tasksList2);
        tacheList2.setAuthor("Torvald");

        List<TaskList> AllList = new ArrayList<>();
        AllList.add(tacheList);
        AllList.add(tacheList2);

        // RECYCLER VIEW LIST TO SELECT
        // init
        recyclerViewListToSelect = findViewById(R.id.dashboard_recyclerView);
        recyclerViewListToSelect.setHasFixedSize(true);
        layoutManagerListToSelect = new LinearLayoutManager(DashboardActivity.this);
        recyclerViewListToSelect.setLayoutManager(layoutManagerListToSelect);

//        taskAdapter = new TaskAdapter(tasksList);
        taskListAdapter = new TaskListAdapter(AllList);
        recyclerViewListToSelect.setAdapter(taskListAdapter);

        //RECYCLER VIEW SELECTED LIST
        // init
        recyclerViewSelectedList = findViewById(R.id.dashboard_recyclerView_SelectedToDoList);
        layoutManagerSelectedList = new LinearLayoutManager(DashboardActivity.this);
        recyclerViewSelectedList.setLayoutManager(layoutManagerSelectedList);
        taskAdapter = new TaskAdapter(tacheList2.getTasksList());
        recyclerViewSelectedList.setAdapter(taskAdapter);
    }
}