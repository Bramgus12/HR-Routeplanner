package com.bramgussekloo.projects.Database;

import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {
    private Connection conn = null;
    String propFileName = "Database_config.properties";

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
