package com.darkjp.todo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class ActiveListActivity extends AppCompatActivity implements Serializable {

    private TextView title;
    private Button backTo;

    private RecyclerView recyclerViewSelectedList;
    private RecyclerView.LayoutManager layoutManagerSelectedList;
    private TaskAdapter taskAdapter;

    private int listToDisplay = Integer.getInteger(getIntent().getStringExtra("activeList"));

    private static final String TAG = "ActiveList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list);
        Log.d(TAG, "onCreate:");

        if(getIntent().hasExtra("selected_list")) {
//            int taskList = getIntent().getParcelableExtra("selected_list");
            Log.d(TAG, "onCreate, Extra is ok");
        } else
            Log.d(TAG, "onCreate, Extra is not ok and it should not be");
    }
}