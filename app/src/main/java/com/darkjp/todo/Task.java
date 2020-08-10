package com.darkjp.todo;

public class Task {

    private String nom;
    private String author;
    private String description;

    public Task (){}

    public Task(String nom, String author) {
        this.nom = nom;
        this.author = author;
        this.description = "";
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
