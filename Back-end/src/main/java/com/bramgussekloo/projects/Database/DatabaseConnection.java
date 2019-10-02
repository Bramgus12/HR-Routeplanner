package com.bramgussekloo.projects.Database;

import java.sql.*;

public class DatabaseConnection {
    private String url;
    private String username;
    private String password;
    private Connection conn;

    public DatabaseConnection(String url, String Username, String password) {
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
}
