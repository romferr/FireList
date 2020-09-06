package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FooterFragment extends Fragment {
    ImageButton myLists, myContacts, newList, myProfile, quitApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_footer, container, false);

        myLists = view.findViewById(R.id.fragment_footer_list);
        myContacts = view.findViewById(R.id.fragment_footer_contacts);
        newList = view.findViewById(R.id.fragment_footer_add_list);
        myProfile = view.findViewById(R.id.fragment_footer_user_profile);
        quitApp = view.findViewById(R.id.fragment_footer_exit_app);

        myLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "my lists", Toast.LENGTH_SHORT).show();
                MyListsFragment myLists = new MyListsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.listFragment, myLists);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        myContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "my contacts", Toast.LENGTH_SHORT).show();

            }
        });

        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "add tasks", Toast.LENGTH_SHORT).show();
                NewTaskListAndTasksFragment newTaskListAndTasksFragment = new NewTaskListAndTasksFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.listFragment, newTaskListAndTasksFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "my profile", Toast.LENGTH_SHORT).show();

            }
        });


        quitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent loginActivity = new Intent(view.getContext(), LoginActivity.class);
                loginActivity.putExtra("email", user.getEmail());
                startActivity(loginActivity);
                getActivity().finish();
            }
        });

        return view;
    }
}