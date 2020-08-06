package com.bramgussekloo.projects.utilities;

import com.bramgussekloo.projects.utilities.GetPropertyValues;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection conn = null;

    public Connection getConnection() {
        try {
            String propFileName = "Database_config.properties";
            String[] values = GetPropertyValues.getDatabasePropValues(propFileName);
            this.conn = DriverManager.getConnection(values[0], values[1], values[2]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
