package com.bramgussekloo.projects.Database;

import java.sql.*;
class DatabaseConnection {
    private String url = "jdbc:postgresql://projects.bramgussekloo.com/Test:5432";
    private String username = "postgres";
    private String password = "*enter password here*";
    private Connection conn = null;

    DatabaseConnection() {
    }

    Connection getConnection(){
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
