package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.Institute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Deprecated
public class InstituteStatements {

    @Deprecated
    public static ArrayList<Institute> getAllInstitutes() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Institute> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM institute");
        if (!result.next()) {
            throw new SQLException("No data in database");
        } else {
            do {
                list.add(getResult(result.getInt("id"), result));
            } while (result.next());
            return list;
        }
    }

    @Deprecated
    public static Institute getInstituteName(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Can't find institute with id " + id);
        } else {
            return getResult(id, resultSet);
        }
    }

    @Deprecated
    public static Institute getInstituteId(String instituteName) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
        preparedStatement.setString(1, instituteName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Can't find institute by name " + instituteName);
        } else {
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    @Deprecated
    public static Institute createInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
        preparedStatement2.setString(1, institute.getName());
        ResultSet result = preparedStatement2.executeQuery();
        if (result.next()) {
            throw new SQLException("The institute you want to create, already exists");
        } else {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO institute VALUES (DEFAULT , ?);");
            preparedStatement.setString(1, institute.getName());
            preparedStatement.execute();
            PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
            preparedStatement1.setString(1, institute.getName());
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("After creating the institute it can't be found anymore.");
            } else {
                return getResult(resultSet.getInt("id"), resultSet);
            }
        }
    }

    @Deprecated
    public static Institute deleteInstitute(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Institute with id " + id + " doesn't exist");
        } else {
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM institute WHERE id =?;");
            preparedStatement1.setInt(1, id);
            preparedStatement1.execute();
            return getResult(id, resultSet);
        }
    }

    @Deprecated
    public static Institute updateInstitute(Institute institute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = institute.getId();
        String instituteName = institute.getName();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE institute SET name =? WHERE id=?;");
        preparedStatement.setString(1, instituteName);
        preparedStatement.setInt(2, id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement1.setInt(1, id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Can't find institute after updating institute");
        } else {
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    @Deprecated
    private static Institute getResult(Integer id, ResultSet resultSet) throws SQLException {
        String instituteName = resultSet.getString("name");
        return new Institute(id, instituteName);
    }
}
