package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.dataclasses.Building;
import com.bramgussekloo.projects.database.DatabaseConnection;

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
            list.add(getResult(result.getInt("id"), result));
        }
        return list;
    }

    public static Building getBuilding(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id);
        result.next();
        return getResult(id, result);
    }

    public static Building getBuildingByName(String name) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building WHERE name='" + name + "';");
        result.next();
        return getResult(result.getInt("id"), result);
    }

    public static Building createBuilding(Building building) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer addressId = building.getAddress_id();
        String name = building.getName();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building WHERE address_id=" + addressId + " AND name='" + name + "';");
        try {
            result.next();
            return getResult(result.getInt("id"), result);
        } catch (SQLException e) {
            conn.createStatement().execute("INSERT INTO building VALUES (DEFAULT, " + addressId + ", '" + name + "'); ");
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE address_id=" + addressId + " AND name='" + name + "';");
            resultSet.next();
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    public static Building deleteBuilding(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id + ";");
        conn.createStatement().execute("DELETE FROM building WHERE id=" + id);
        resultSet.next();
        return getResult(id, resultSet);
    }

    public static Building updateBuilding(Building building) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = building.getId();
        Integer address_id = building.getAddress_id();
        String name = building.getName();
        conn.createStatement().execute("UPDATE building SET address_id=" + address_id + ", name='" + name + "' WHERE id=" + id + "; ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id);
        resultSet.next();
        return getResult(id, resultSet);
    }

    private static Building getResult(Integer id, ResultSet resultSet) throws SQLException{
        Integer address_id = resultSet.getInt("address_id");
        String name = resultSet.getString("name");
        return new Building(id, address_id, name);
    }
}
