package com.darkjp.todo;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private String pseudo, email, imgProfile;
    private FirebaseUser userAuth;

    public User() {
    }

    public User(String pseudo, String email) {
        this.pseudo = pseudo;
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public FirebaseUser getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(FirebaseUser userAuth) {
        this.userAuth = userAuth;
    }
}
