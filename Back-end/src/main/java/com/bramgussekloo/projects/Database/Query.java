package com.bramgussekloo.projects.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class Query {
    private String dbUrl = "jdbc:postgresql://projects.bramgussekloo.com/Test:5432";
    private String dbPassword = "*insert password here*";
    private String dbUsername = "postgres";
    private Connection conn = new DatabaseConnection(dbUrl, dbUsername, dbPassword).getConnection();

    public Boolean ExecuteQuery(String SQLQuery) throws SQLException {
        try {
            conn.createStatement().execute(SQLQuery);
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public Boolean createTable(String tableName, String Columns){
        try {
            conn.createStatement().execute("CREATE TABLE " + tableName + "(" + Columns + ")");
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void getAddress(){

    }
}
