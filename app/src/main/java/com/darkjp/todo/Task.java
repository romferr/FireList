package com.darkjp.todo;

import java.util.Objects;

public class Task {

    private boolean done;
    private String title;
    private String creator;
    private String description;
    private String id;

    public Task() {
    }

    public Task(String title, String uuid) {
        this.title = title;
        this.creator = "";
        this.description = "";
        this.done = false;
        this.id = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return done == task.done &&
                Objects.equals(title, task.title) &&
                Objects.equals(creator, task.creator) &&
                Objects.equals(description, task.description) &&
                Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator, description, done, id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", creator='" + creator + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }


}
