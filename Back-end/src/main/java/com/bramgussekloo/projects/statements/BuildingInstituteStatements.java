package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.dataclasses.BuildingInstitute;
import com.bramgussekloo.projects.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BuildingInstituteStatements {
    public static ArrayList<BuildingInstitute> getAllBuildingInstitutes() throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<BuildingInstitute> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute");
        while (result.next()) {
            list.add(getResult(result.getInt("id"), result));
        }
        return list;
    }

    public static BuildingInstitute getBuildingInstitute(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id);
        result.next();
        return getResult(id, result);
    }

    public static BuildingInstitute createBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer building_id = buildingInstitute.getBuildingId();
        Integer institute_id = buildingInstitute.getInstituteId();
        conn.createStatement().execute("INSERT INTO building_institute VALUES (DEFAULT , " + building_id + ", " + institute_id + ");");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE building_id=" + building_id + " AND institute_id=" + institute_id + ";");
        resultSet.next();
        return getResult(resultSet.getInt("id"), resultSet);
    }

    public static BuildingInstitute deleteBuildingInstitute(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id + ";");
        conn.createStatement().execute("DELETE FROM building_institute WHERE id=" + id);
        resultSet.next();
        return getResult(id, resultSet);
    }

    public static BuildingInstitute updateBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuildingId();
        Integer institute_id = buildingInstitute.getInstituteId();
        conn.createStatement().execute("UPDATE building_institute SET building_id=" + building_id + ", institute_id=" + institute_id + " WHERE id=" + id );
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id + ";");
        resultSet.next();
        return getResult(id, resultSet);
    }

    private static BuildingInstitute getResult(Integer id, ResultSet resultSet) throws SQLException{
        Integer building_id = resultSet.getInt("building_id");
        Integer institute_id = resultSet.getInt("institute_id");
        return new BuildingInstitute(id, building_id, institute_id);
    }

}
