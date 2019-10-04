package com.bramgussekloo.projects.Database;

import java.io.IOException;
import java.sql.*;

class DatabaseConnection {
    private Connection conn = null;
    String propFileName = "Database_config.properties";

    DatabaseConnection() {
    }

    Connection getConnection() {
        try {
            getPropertiesValues prop = new getPropertiesValues();
            try {
                String[] values = prop.getPropValues(propFileName);
                this.conn = DriverManager.getConnection(values[0], values[1], values[2]);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection failed with error: \n" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
