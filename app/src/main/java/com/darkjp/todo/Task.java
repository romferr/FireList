package com.darkjp.todo;

import java.util.Objects;

public class Task {

    private String title;
    private String creator;
    private String description;
    private boolean done;

    public Task (){}

    public Task(String title) {
        this.title = title;
        this.creator = "";
        this.description = "";
        this.done = false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return done == task.done &&
                Objects.equals(title, task.title) &&
                Objects.equals(creator, task.creator) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator, description, done);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nom='" + title + '\'' +
                ", creator='" + creator + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }


}
