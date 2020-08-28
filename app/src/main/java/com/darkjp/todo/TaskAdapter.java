package com.darkjp.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    String listId;
    List<Task> tasks;
    private OnTaskClickListener mOnTaskClickListener;

    private static final String TAG = "TaskAdapter";

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkbox;
        private ImageView deleteTask, editTask;
        private OnTaskClickListener onTaskClickListener;
        private TextView nom, creator, description, by;

        public TaskViewHolder(@NonNull View itemView, OnTaskClickListener onTaskClickListener) {
            super(itemView);
            by = itemView.findViewById(R.id.row_by);
            checkbox = itemView.findViewById(R.id.row_checkBox);
            creator = itemView.findViewById(R.id.row_author);
            deleteTask = itemView.findViewById(R.id.row_task_deleteTask);
            description = itemView.findViewById(R.id.row_description);
            editTask = itemView.findViewById(R.id.row_task_editTask);
            nom = itemView.findViewById(R.id.row_name);
            this.onTaskClickListener = onTaskClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTaskClickListener.onTaskClick(getAdapterPosition());
        }
    }

    public TaskAdapter(String listId, List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.listId = listId;
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
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        Task task = tasks.get(position);
        holder.nom.setText(task.getTitle());
        //get author from a parent db object
        if (task.getCreator() != null && !task.getCreator().equals("") && !task.getCreator().equals(FirebaseAuth.getInstance().getUid())) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mAuthor = database.getReference("user/" + task.getCreator());
            mAuthor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.creator.setText((String) snapshot.child("pseudo").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            holder.by.setText("");
            holder.creator.setText("");
        }
        holder.description.setText(task.getDescription());
        holder.checkbox.setChecked(task.isDone());

        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Task> tempTask = new ArrayList();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDelete = database.getReference("tasksList/" + listId);
                mDelete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.child("task").getChildren()) {
                            tempTask.add(snap.getValue(Task.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDelete.child("task").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        for (DataSnapshot snapshot : snap.getChildren()) {
                            if (snapshot.child("id").getValue().equals(tempTask.get(position).getId())) {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface OnTaskClickListener {
        void onTaskClick(int position);
    }
}
