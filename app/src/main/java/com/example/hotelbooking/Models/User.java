package com.example.hotelbooking.Models;

public class User {
    String name;
    String id;
    String urlIMG;
    String email;

    public User() {

    }

    public User(String name, String id, String urlIMG, String email) {
        this.name = name;
        this.id = id;
        this.urlIMG = urlIMG;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlIMG() {
        return urlIMG;
    }

    public void setUrlIMG(String urlIMG) {
        this.urlIMG = urlIMG;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

