package com.darkjp.todo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private  List<TaskList> taskLists;
    private OnTaskListClickListener mOnTaskListClickListener;

    private final String TAG = "TaskListAdapter";

    public static class TaskListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView author;
        OnTaskListClickListener onTaskListClickListener;

        public TaskListViewHolder(@NonNull View itemView,  OnTaskListClickListener onTaskListClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.row_task_list_title);
            author = itemView.findViewById(R.id.row_task_list_author);
            this.onTaskListClickListener = onTaskListClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTaskListClickListener.onTaskClick(getAdapterPosition());
        }
    }

    public TaskListAdapter(List<TaskList> taskLists, OnTaskListClickListener onTaskListClickListener){
        this.taskLists = taskLists;
        this.mOnTaskListClickListener = onTaskListClickListener;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task_list, parent, false);
        TaskListViewHolder taskListViewHolder = new TaskListViewHolder(view, mOnTaskListClickListener);
        return taskListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        Log.d(TAG, "TaskListAdapter : onBindViewHolder called");
        TaskList taskList = taskLists.get(position);
        holder.title.setText(taskList.getTitle());
        if (!taskList.getCreator().equals("") || taskList != null) {
            holder.author.setText(taskList.getCreator());
        }

    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    public interface OnTaskListClickListener {
        void onTaskClick(int position);
    }

}
