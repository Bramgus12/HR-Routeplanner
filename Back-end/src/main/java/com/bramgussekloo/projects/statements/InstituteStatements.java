package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InstituteStatements {
    public static ArrayList<Institute> getAllInstitutes() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Institute> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM institute");
        while (result.next()) {
            Integer id = result.getInt("id");
            String instituteName = result.getString("name");
            Institute institute = new Institute(id, instituteName);
            list.add(institute);
        }
        return list;
    }

    public static Institute getInstituteName(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        resultSet.next();
        return new Institute(resultSet.getInt("id"), resultSet.getString("name"));
    }

    public static Institute getInstituteId(String instituteName) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE name='" + instituteName + "';");
        resultSet.next();
        return new Institute(resultSet.getInt("id"), resultSet.getString("name"));
    }

    public static Institute createInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        conn.createStatement().execute("INSERT INTO institute VALUES (DEFAULT ,'" + institute.getName() + "');");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE name='" + institute.getName() + "';");
        resultSet.next();
        return new Institute(resultSet.getInt("id"), resultSet.getString("name"));
    }

    public static Institute deleteInstitute(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        conn.createStatement().execute("DELETE FROM institute WHERE id =" + id + ";");
        resultSet.next();
        return new Institute(resultSet.getInt("id"), resultSet.getString("name"));
    }

    public static Institute updateInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = institute.getId();
        String instituteName = institute.getName();
        conn.createStatement().execute("UPDATE institute SET name ='" + instituteName + "' WHERE id=" + id + ";");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        resultSet.next();
        return new Institute(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
