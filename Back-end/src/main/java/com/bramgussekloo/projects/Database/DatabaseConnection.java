package com.bramgussekloo.projects.Database;

import java.sql.*;
import java.util.ResourceBundle;

class DatabaseConnection {
    private Connection conn = null;
    private ResourceBundle reader = null;

    DatabaseConnection() {
    }

    Connection getConnection(){
        try {
            reader = ResourceBundle.getBundle("Database_config.properties");
            this.conn = DriverManager.getConnection(reader.getString("db.url"), reader.getString("db.username"), reader.getString("db.password"));
        }
        catch (SQLException e){
            System.out.println("Connection failed with error: \n" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
