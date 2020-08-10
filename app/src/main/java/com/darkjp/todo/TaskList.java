package com.darkjp.todo;

import java.util.List;

public class TaskList {
    private String title;
    private String author;
    private List<Task> tasksList;

    public TaskList() {
    }

    public TaskList(String title) {
        this.title = title;
        this.author = "";
        this.tasksList = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Task> tasksList) {
        this.tasksList = tasksList;
    }
}
