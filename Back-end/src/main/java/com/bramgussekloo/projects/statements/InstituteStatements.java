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
            list.add(getResult(result.getInt("id"), result));
        }
        return list;
    }

    public static Institute getInstituteName(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        resultSet.next();
        return getResult(id, resultSet);
    }

    public static Institute getInstituteId(String instituteName) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE name='" + instituteName + "';");
        resultSet.next();
        return getResult(resultSet.getInt("id"), resultSet);
    }

    public static Institute createInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        conn.createStatement().execute("INSERT INTO institute VALUES (DEFAULT ,'" + institute.getName() + "');");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE name='" + institute.getName() + "';");
        resultSet.next();
        return getResult(resultSet.getInt("id"), resultSet);
    }

    public static Institute deleteInstitute(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        conn.createStatement().execute("DELETE FROM institute WHERE id =" + id + ";");
        resultSet.next();
        return getResult(id, resultSet);
    }

    public static Institute updateInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = institute.getId();
        String instituteName = institute.getName();
        conn.createStatement().execute("UPDATE institute SET name ='" + instituteName + "' WHERE id=" + id + ";");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM institute WHERE id=" + id + ";");
        resultSet.next();
        return getResult(resultSet.getInt("id"), resultSet);
    }

    private static Institute getResult(Integer id, ResultSet resultSet) throws SQLException{
        String instituteName = resultSet.getString("name");
        return new Institute(id, instituteName);
    }
}
