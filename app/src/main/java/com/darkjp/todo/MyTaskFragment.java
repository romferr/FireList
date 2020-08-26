package com.darkjp.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTaskFragment extends Fragment {
    private Bundle bundle;
    private Button backToTaskList;
    private CheckBox isDoneCheckBox;
    private FirebaseDatabase database;
    private String taskIndex, taskListIndex, fatherTasksListTitle;
    private String selectedTaskId = "";
    private TextView creator, participants, taskListTitle, title, description, by;
    private View view;

    private static final String TAG = "myTaskFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_task, container, false);

        database = FirebaseDatabase.getInstance();
        bundle = getArguments();
        //get position of the task from the master intent
        if (bundle != null) {
            taskIndex = bundle.getString("task");
            taskListIndex = bundle.getString("task_List_index");
            fatherTasksListTitle = bundle.getString("father_tasksList_title");

            taskListTitle = view.findViewById(R.id.fragment_taskslist_title);
            title = view.findViewById(R.id.fragment_task_title);
            description = view.findViewById(R.id.fragment_task_txt_description);
            isDoneCheckBox = view.findViewById(R.id.fragment_task_checkBox);
            by = view.findViewById(R.id.fragment_task_createdBy);
            creator = view.findViewById(R.id.fragment_task_creator);
            participants = view.findViewById(R.id.fragment_task_participants);

            /**
             * get the title sent by the previous view as a bundle
             */
            taskListTitle.setText(fatherTasksListTitle);

            /**
             * check the task to complete fieds
             */
            DatabaseReference mSelectedTask = database.getReference("tasksList/" + taskListIndex).child("task/" + taskIndex);
            mSelectedTask.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            Toast.makeText(view.getContext(), "NO TASK ! BAST44444RDZZZZ !", Toast.LENGTH_SHORT).show();
        }

        isDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            final DatabaseReference mDone = database.getReference("tasksList/" + taskListIndex).child("task/" + taskIndex);
            mDone.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Task updateTask = new Task();
                    if (snapshot.child("creator").getValue() != null) {
                        if (snapshot.child("creator").getValue().toString().equals(""))
                            updateTask.setCreator("no creator found");
                        updateTask.setCreator(snapshot.child("creator").getValue().toString());
                    }
                    if (snapshot.child("title").getValue() != null && !snapshot.child("title").getValue().toString().equals(""))
                        updateTask.setTitle(snapshot.child("title").getValue().toString());
                    if (snapshot.child("description").getValue() != null && !snapshot.child("description").getValue().toString().equals(""))
                        updateTask.setDescription(snapshot.child("description").getValue().toString());
                    if (snapshot.child("done").getValue() != null )
                        updateTask.setDone(isDoneCheckBox.isChecked());
                    mDone.setValue(updateTask);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}