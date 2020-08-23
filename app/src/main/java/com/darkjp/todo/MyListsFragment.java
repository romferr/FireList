package com.darkjp.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListsFragment extends Fragment implements TaskListAdapter.OnTaskListClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<TaskList> userTasksList = new ArrayList<>();
    private View view;
    private RecyclerView recyclerViewListToSelect;
    private RecyclerView.LayoutManager layoutManagerListToSelect;
    private TaskListAdapter taskListAdapter;

    private FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_lists, container, false);
        DatabaseReference mTasksList = database.getReference("tasksList");
        mTasksList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTasksList.clear();
                for (DataSnapshot snapCreator : snapshot.getChildren()) {
                    if (snapCreator.child("creator").getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {
                        userTasksList.add(snapCreator.getValue(TaskList.class));
                    }
                    for (DataSnapshot snapParticipant : snapCreator.child("participant").getChildren()) {
                        if (snapParticipant.getKey().toString().equals(FirebaseAuth.getInstance().getUid())) {
                            userTasksList.add(snapCreator.getValue(TaskList.class));
                        }
                    }
                }
                sendSomeToThatRecyclerViewBiatch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onTaskClick(int position) {
        Bundle listId = new Bundle();
        listId.putString("taskListIndex", userTasksList.get(position).getId());

        SelectedListFragment selectedListFragment = new SelectedListFragment();
        selectedListFragment.setArguments(listId);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_myLists, selectedListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void sendSomeToThatRecyclerViewBiatch() {
        if (userTasksList != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            recyclerViewListToSelect = this.getView().findViewById(R.id.fragment_dashboard_recyclerView);
            recyclerViewListToSelect.setHasFixedSize(true);
            layoutManagerListToSelect = new LinearLayoutManager(view.getContext());
            recyclerViewListToSelect.setLayoutManager(layoutManagerListToSelect);
            taskListAdapter = new TaskListAdapter(userTasksList, this);
            recyclerViewListToSelect.setAdapter(taskListAdapter);
        } else {
            Toast.makeText(view.getContext(), "No list yet", Toast.LENGTH_SHORT).show();
        }
    }
}