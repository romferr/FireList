package com.darkjp.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FragmentTransaction fragmentTransactionHeader, fragmentTransactioMyLists, fragmentTransactionFooter;

    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fragmentTransactionHeader = getSupportFragmentManager().beginTransaction();
        final HeaderFragment headerFragment = new HeaderFragment();
        fragmentTransactionHeader.replace(R.id.fragment_header, headerFragment);
        fragmentTransactionHeader.commit();

        fragmentTransactioMyLists = getSupportFragmentManager().beginTransaction();
        MyListsFragment myListsFragment = new MyListsFragment();
        fragmentTransactioMyLists.replace(R.id.fragment_myLists, myListsFragment);
        fragmentTransactioMyLists.commit();

        fragmentTransactionFooter = getSupportFragmentManager().beginTransaction();
        FooterFragment footerFragment = new FooterFragment();
        fragmentTransactionFooter.replace(R.id.fragment_footer, footerFragment);
        fragmentTransactionFooter.commit();

        //get infos from database (ex: user info)
        // 1) PSEUDO
        DatabaseReference mPseudo = database.getReference("user/" + mAuth.getUid()).child("pseudo");
        mPseudo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String valuePseudo = snapshot.getValue(String.class);
                headerFragment.changePseudo(valuePseudo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value for the user Pseudo", error.toException());
            }
        });
    }
}