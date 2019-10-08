package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BuildingStatements {
    public static ArrayList<Building> getAllBuildings(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Building> list = new ArrayList<>();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer address_id = result.getInt("address_id");
                String name = result.getString("name");
                Building building = new Building(id, address_id, name);
                list.add(building);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public static Building getBuilding(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building WHERE id=" + id);
            while (result.next()){
                Integer address_id = result.getInt("address_id");
                String name = result.getString("name");
                return new Building(id, address_id, name);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new Building();
    }
    public static String createBuilding(Building building){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = building.getId();
        Integer address_id = building.getAddress_id();
        String name = building.getName();
        try{
            conn.createStatement().execute("INSERT INTO building VALUES (" + id + ", " + address_id + ", '" + name + "');");
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String deleteBuilding(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            conn.createStatement().execute("DELETE FROM building WHERE id=" + id);
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String updateBuilding(Building building){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = building.getId();
        Integer address_id = building.getAddress_id();
        String name = building.getName();
        String output;

        try{
            conn.createStatement().execute("UPDATE building SET address_id=" + address_id + ", name='" + name + "' WHERE id=" + id );
            output = "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            output = e.getMessage();
        }
        return output;
    }
}
