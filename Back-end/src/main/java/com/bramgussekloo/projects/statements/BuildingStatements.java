package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.Building;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Deprecated
public class BuildingStatements {
    @Deprecated
    public static ArrayList<Building> getAllBuildings() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Building> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building");
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
    public static Building getBuilding(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE id=?");
        preparedStatement.setInt(1, id);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new SQLException("Building with id " + id + " can't be found");
        } else {
            return getResult(id, result);
        }
    }

    @Deprecated
    public static Building getBuildingByName(String name) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE name=?;");
        preparedStatement.setString(1, name);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new SQLException("Building with name " + name + " can't be found");
        } else {
            return getResult(result.getInt("id"), result);
        }
    }

    @Deprecated
    public static Building createBuilding(Building building) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer addressId = building.getAddress_id();
        String name = building.getName();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE address_id=? AND name=?;");
        preparedStatement.setInt(1, addressId);
        preparedStatement.setString(2, name);
        ResultSet result = preparedStatement.executeQuery();

        if (!result.next()) {
            PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO building VALUES (DEFAULT, ?, ?); ");
            preparedStatement1.setInt(1, addressId);
            preparedStatement1.setString(2, name);
            preparedStatement1.execute();
            PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT * FROM building WHERE address_id=? AND name=?;");
            preparedStatement2.setInt(1, addressId);
            preparedStatement2.setString(2, name);
            ResultSet resultSet = preparedStatement2.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("After creating the building can't be found anymore.");
            } else {
                return getResult(resultSet.getInt("id"), resultSet);
            }
        } else {
            return getResult(result.getInt("id"), result);
        }
    }

    @Deprecated
    public static Building deleteBuilding(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Building with id " + id + " doesn't exist");
        } else {
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM building WHERE id=?;");
            preparedStatement1.setInt(1, id);
            preparedStatement1.execute();
            return getResult(id, resultSet);
        }
    }

    @Deprecated
    public static Building updateBuilding(Building building) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = building.getId();
        Integer address_id = building.getAddress_id();
        String name = building.getName();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE building SET address_id=?, name=? WHERE id=?;");
        preparedStatement.setInt(1, address_id);
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM building WHERE id=?;");
        preparedStatement1.setInt(1, id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Can't find building after updating");
        } else {
            return getResult(id, resultSet);
        }
    }

    private static Building getResult(Integer id, ResultSet resultSet) throws SQLException {
        Integer address_id = resultSet.getInt("address_id");
        String name = resultSet.getString("name");
        return new Building(id, address_id, name);
    }
}
