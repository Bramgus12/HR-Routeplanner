package com.bramgussekloo.projects.controller;

import java.sql.*;

public class DatabaseController {
    private String url;
    private String username;
    private String password;
    private Connection conn;

    public DatabaseController(String url, String Username, String password) {
        this.url = url;
        this.username = Username;
        this.password = password;
        this.conn = null;
    }

    public Connection getConnection(){
        try {
            this.conn = DriverManager.getConnection(this.url, this.username, this.password);
        }
        catch (SQLException e){
            System.out.println("Connection failed with error: \n" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public void createStatement(String SQLQuery){
        try {
            conn.createStatement().execute(SQLQuery);
            System.out.println("Query successfully executed");
        }
        catch (SQLException e){
            System.out.println("Query failed with error: \n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
