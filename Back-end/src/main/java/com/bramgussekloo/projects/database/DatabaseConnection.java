package com.bramgussekloo.projects.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    String propFileName = "Database_config.properties";
    private Connection conn = null;

    public Connection getConnection() {
        try {
            getPropertiesValues prop = new getPropertiesValues();
            String[] values = prop.getPropValues(propFileName);
            this.conn = DriverManager.getConnection(values[0], values[1], values[2]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
