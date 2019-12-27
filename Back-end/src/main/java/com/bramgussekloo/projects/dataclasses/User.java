package com.bramgussekloo.projects.dataclasses;

public class User {
    private int id;
    private String user_name;
    private String password;
    private String authority;
    private Boolean enabled;

    public User(int id, String user_name, String password, String authority, Boolean enabled) {
        this.id = id;
        this.password = password;
        this.user_name = user_name;
        this.authority = authority;
        this.enabled = enabled;

    }

    public User() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
