package com.darkjp.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    List<TaskList> taskLists;

    public static class TaskListViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.row_task_list_title);
            author = itemView.findViewById(R.id.row_task_list_author);
        }
    }

    public TaskListAdapter(List<TaskList> taskLists){
        this.taskLists = taskLists;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task_list, parent, false);
        TaskListViewHolder taskListViewHolder = new TaskListViewHolder(view);
        return taskListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        TaskList taskList = taskLists.get(position);
        holder.title.setText(taskList.getTitle());
        if (!taskList.getAuthor().equals("") || taskList != null) {
            holder.author.setText(taskList.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

}
