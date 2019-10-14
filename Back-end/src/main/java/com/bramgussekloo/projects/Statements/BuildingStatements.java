package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BuildingStatements {
    public static ArrayList<Building> getAllBuildings() throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Building> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building");
        while (result.next()){
            Integer id = result.getInt("id");
            Integer address_id = result.getInt("address_id");
            String name = result.getString("name");
            Building building = new Building(id, address_id, name);
            list.add(building);
        }
        return list;
    }

    public static Building getBuilding(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id);
        result.next();
        Integer address_id = result.getInt("address_id");
        String name = result.getString("name");
        return new Building(id, address_id, name);
    }

    public static Building createBuilding(Building building) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer addressId = building.getAddress_id();
        String name = building.getName();
        conn.createStatement().execute("INSERT INTO building VALUES (DEFAULT, " + addressId + ", '" + name + "'); ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE address_id=" + addressId + " AND name='" + name + "';");
        resultSet.next();
        return new Building(resultSet.getInt("id"), resultSet.getInt("address_id"), resultSet.getString("name"));
    }

    public static Building deleteBuilding(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id + ";");
        conn.createStatement().execute("DELETE FROM building WHERE id=" + id);
        resultSet.next();
        return new Building(resultSet.getInt("id"), resultSet.getInt("address_id"), resultSet.getString("name"));
    }

    public static Building updateBuilding(Building building) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = building.getId();
        Integer address_id = building.getAddress_id();
        String name = building.getName();
        conn.createStatement().executeQuery("UPDATE building SET address_id=" + address_id + ", name='" + name + "' WHERE id=" + id + "; ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id);
        resultSet.next();
        return new Building(resultSet.getInt("id"), resultSet.getInt("address_id"), resultSet.getString("name"));
    }
}
