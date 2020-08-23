package com.darkjp.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HeaderFragment extends Fragment {

    private static final String TAG = "HeaderFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        return view;
    }

    public void changePseudo(String pseudo){
        TextView t = (TextView) this.getView().findViewById(R.id.fragment_userPseudo);
        t.setText(pseudo);
    }

    public void addParticipant(String participant) {
        TextView participants = (TextView) this.getView().findViewById(R.id.fragment_participants);
        participants.setText(participant);
    }

    public String getParticipants(){
        TextView participants = (TextView) this.getView().findViewById(R.id.fragment_participants);
        return participants.getText().toString();
    }
}