package com.darkjp.todo;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
    private static final String TAG = "MyListsFragment";
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<TaskList> userTasksList;

    private RecyclerView recyclerViewListToSelect;
    private RecyclerView.LayoutManager layoutManagerListToSelect;
    private TaskListAdapter taskListAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_lists, container, false);
//        recyclerViewListToSelect = view.findViewById(R.id.fragment_dashboard_recyclerView);

        userTasksList = new ArrayList<>();
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
                        if (snapParticipant.getKey().equals(FirebaseAuth.getInstance().getUid())) {
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
        fragmentTransaction.replace(R.id.listFragment, selectedListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void sendSomeToThatRecyclerViewBiatch() {
        if (userTasksList != null) {
            // RECYCLER VIEW LIST TO SELECT
            // init
            recyclerViewListToSelect = view.findViewById(R.id.fragment_dashboard_recyclerView);
            recyclerViewListToSelect.setHasFixedSize(true);
            layoutManagerListToSelect = new LinearLayoutManager(view.getContext());
            recyclerViewListToSelect.setLayoutManager(layoutManagerListToSelect);
            taskListAdapter = new TaskListAdapter(userTasksList, this);
            recyclerViewListToSelect.setAdapter(taskListAdapter);
        } else {
            Toast.makeText(view.getContext(), "No list yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "et zou onPause()");
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        if (recyclerViewListToSelect != null ) {
            Parcelable listState = recyclerViewListToSelect.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "et zou onResume()");
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null && recyclerViewListToSelect != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerViewListToSelect.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}