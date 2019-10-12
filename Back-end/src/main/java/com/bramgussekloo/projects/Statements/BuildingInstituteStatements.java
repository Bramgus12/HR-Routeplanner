package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.Database.DatabaseConnection;

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
            Integer id = result.getInt("id");
            Integer building_id = result.getInt("building_id");
            Integer institute_id = result.getInt("institute_id");
            BuildingInstitute buildingInstitute = new BuildingInstitute(id, building_id, institute_id);
            list.add(buildingInstitute);
        }
        return list;
    }

    public static BuildingInstitute getBuildingInstitute(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id);
        result.next();
        Integer building_id = result.getInt("building_id");
        Integer institute_id = result.getInt("institute_id");
        return new BuildingInstitute(id, building_id, institute_id);
    }

    public static BuildingInstitute createBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuilding_id();
        Integer institute_id = buildingInstitute.getInstituteId();
        conn.createStatement().execute("INSERT INTO building_institute VALUES (DEFAULT , " + building_id + ", " + institute_id + ");");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE building_id=" + building_id + " AND institute_id=" + institute_id + ";");
        return new BuildingInstitute(resultSet.getInt("id"), resultSet.getInt("building_id"), resultSet.getInt("institute_id"));
    }

    public static Boolean deleteBuildingInstitute(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        conn.createStatement().execute("DELETE FROM building_institute WHERE id=" + id);
        return true;
    }

    public static BuildingInstitute updateBuildingInstitute(BuildingInstitute buildingInstitute) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = buildingInstitute.getId();
        Integer building_id = buildingInstitute.getBuilding_id();
        Integer institute_id = buildingInstitute.getInstituteId();
        conn.createStatement().execute("UPDATE building_institute SET building_id=" + building_id + ", institute_id=" + institute_id + " WHERE id=" + id );
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building_institute WHERE id=" + id + ";");
        return new BuildingInstitute(resultSet.getInt("id"), resultSet.getInt("building_id"), resultSet.getInt("institute_id"));
    }
}
