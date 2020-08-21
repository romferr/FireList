package com.darkjp.todo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> tasks;
    private OnTaskClickListener mOnTaskClickListener;

    private static final String TAG = "TaskAdapter";

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nom;
        private TextView author;
        private TextView description;
        private CheckBox checkbox;
        OnTaskClickListener onTaskClickListener;

        public TaskViewHolder(@NonNull View itemView, OnTaskClickListener onTaskClickListener) {
            super(itemView);
            nom = itemView.findViewById(R.id.row_name);
            author = itemView.findViewById(R.id.row_author);
            description = itemView.findViewById(R.id.row_description);
            this.onTaskClickListener = onTaskClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTaskClickListener.onTaskClick(getAdapterPosition());
        }
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.tasks = tasks;
        this.mOnTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view, mOnTaskClickListener);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, int position) {
        Log.d(TAG, "TaskAdapter : onBindViewHolder called");
        Task task = tasks.get(position);
        holder.nom.setText(task.getNom());
        //get author from a parent db object
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mAuthor = database.getReference("user_" + task.getCreator());
        mAuthor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.author.setText((String) snapshot.child("pseudo").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        holder.author.setText(task.getCreator());
        holder.description.setText(task.getDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public interface OnTaskClickListener {
        void onTaskClick(int position);
    }
}
