package com.darkjp.todo;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SelectedListFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private ArrayList<Task> tasks = new ArrayList<>();
    private Bundle bundle;
    private RecyclerView tasksRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String listId;
    private TaskAdapter taskAdapter;
    private TextView title;
    private View view;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "SelectedListFragment";
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_selected_list, container, false);
        bundle = getArguments();
        listId = bundle.getString("taskListIndex");


        title = view.findViewById(R.id.fragment_selectedList_txt_title);

        DatabaseReference mTaskList = database.getReference("tasksList/" + listId);
        mTaskList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (tasks != null) {
                    tasks.clear();
                    title.setText(snapshot.child("title").getValue().toString());

                    for (DataSnapshot snap : snapshot.child("task").getChildren()) {
                        tasks.add(snap.getValue(Task.class));
                        sendSomeToThatRecyclerViewBiatch(tasks);
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
        Bundle taskToBeDone = new Bundle();
        taskToBeDone.putString("task", String.valueOf(position));
        taskToBeDone.putString("task_List_index", String.valueOf(listId));
        taskToBeDone.putString("father_tasksList_title", title.getText().toString());

        MyTaskFragment myTaskFragment = new MyTaskFragment();
        myTaskFragment.setArguments(taskToBeDone);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listFragment, myTaskFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "et zou onPause()");
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = tasksRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "et zou onResume()");
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null && tasksRecyclerView != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            tasksRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}