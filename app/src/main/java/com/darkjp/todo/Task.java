package com.darkjp.todo;

import java.util.Objects;

public class Task {

    private String nom;
    private String creator;
    private String description;
    private Boolean done;

    public Task (){}

    public Task(String nom, String creator) {
        this.nom = nom;
        this.creator = creator;
        this.description = "";
        this.done = false;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
                Objects.equals(nom, task.nom) &&
                Objects.equals(creator, task.creator) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, creator, description, done);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nom='" + nom + '\'' +
                ", creator='" + creator + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }


}
