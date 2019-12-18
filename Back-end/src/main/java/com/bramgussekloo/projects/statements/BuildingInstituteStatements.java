package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.BuildingInstitute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BuildingInstituteStatements {
    public static ArrayList<BuildingInstitute> getAllBuildingInstitutes() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<BuildingInstitute> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute");
        if (!result.next()) {
            throw new SQLException("No data in database");
        } else {
            do {
                list.add(getResult(result.getInt("id"), result));
            } while (result.next());
            return list;
        }
    }

    public static BuildingInstitute getBuildingInstitute(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building_institute WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new SQLException("The buildingInstitute doesn't exist under id " + id);
        } else {
            return getResult(id, result);
        }
    }

    public static BuildingInstitute createBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer building_id = buildingInstitute.getBuildingId();
        Integer institute_id = buildingInstitute.getInstituteId();
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO building_institute VALUES (DEFAULT , ?, ?);");
        preparedStatement.setInt(1, building_id);
        preparedStatement.setInt(2, institute_id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM building_institute WHERE building_id=? AND institute_id=?;");
        preparedStatement1.setInt(1, building_id);
        preparedStatement1.setInt(2, institute_id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Can't find buildingInstitute after creating.");
        } else {
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    public static BuildingInstitute deleteBuildingInstitute(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building_institute WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM building_institute WHERE id=?");
        preparedStatement1.setInt(1, id);
        if (!resultSet.next()) {
            throw new SQLException("BuildingInstitute with id " + id + " doesn't exist");
        } else {
            preparedStatement1.execute();
            return getResult(id, resultSet);
        }
    }

    public static BuildingInstitute updateBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuildingId();
        Integer institute_id = buildingInstitute.getInstituteId();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE building_institute SET building_id=?, institute_id=? WHERE id=?");
        preparedStatement.setInt(1, building_id);
        preparedStatement.setInt(2, institute_id);
        preparedStatement.setInt(3, id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM building_institute WHERE id=?;");
        preparedStatement1.setInt(1, id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("BuildingInstitute with id " + id + " can't be found");
        } else {
            return getResult(id, resultSet);
        }
    }

    private static BuildingInstitute getResult(Integer id, ResultSet resultSet) throws SQLException {
        Integer building_id = resultSet.getInt("building_id");
        Integer institute_id = resultSet.getInt("institute_id");
        return new BuildingInstitute(id, building_id, institute_id);
    }

}
