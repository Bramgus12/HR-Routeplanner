package com.bramgussekloo.projects.dataclasses;

public class User {
    private int id;
    private String user_name;
    private String auth_key;

    public User(int id, String user_name, String auth_key) {
        this.id = id;
        this.auth_key = auth_key;
        this.user_name = user_name;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
