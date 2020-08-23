package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class FooterFragment extends Fragment {
    ImageButton myLists, myContacts, newList, myProfile, quitApp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_footer, container, false);

        myLists = view.findViewById(R.id.fragment_footer_list);
        myContacts = view.findViewById(R.id.fragment_footer_contacts);
        newList = view.findViewById(R.id.fragment_footer_add_list);
        myProfile = view.findViewById(R.id.fragment_footer_user_profile);
        quitApp = view.findViewById(R.id.fragment_footer_exit_app);

        myLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyListsFragment myLists = new MyListsFragment();

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_myLists, myLists);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}