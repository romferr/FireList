package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SelectedListFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private View view;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_selected_list, container, false);
        Bundle bundle = getArguments();
        String listId = bundle.getString("taskListIndex");

        final HeaderFragment headerFragment = (HeaderFragment) getActivity().getSupportFragmentManager().findFragmentByTag("fragment_header");
        System.out.println(headerFragment.getParticipants());

        final TextView title = view.findViewById(R.id.fragment_selectedList_txt_title);

        DatabaseReference mTaskList = database.getReference("tasksList/" + listId);
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
//                            headerFragment.changePseudo("");
                            if (tasks != null) {
                                if (!snapParticipant.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
                        }
                        headerFragment.addParticipant("Participant(s):\n");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void sendSomeToThatRecyclerViewBiatch(ArrayList<Task> tasks) {
        if (tasks != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            tasksRecyclerView = view.findViewById(R.id.fragment_selected_list_recyclerView);
            tasksRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(view.getContext());
            tasksRecyclerView.setLayoutManager(layoutManager);
            taskAdapter = new TaskAdapter(tasks, this);
            tasksRecyclerView.setAdapter(taskAdapter);
        } else {
            Toast.makeText(view.getContext(), "No list yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskClick(int position) {
        Toast.makeText(view.getContext(), "Cot cot cot", Toast.LENGTH_SHORT).show();
        Intent taskToBeDone = new Intent(view.getContext(), TaskActivity.class);
        taskToBeDone.putExtra("task", String.valueOf(position));
//        taskToBeDone.putExtra("taskListIndex", listId);
        startActivity(taskToBeDone);
    }
}