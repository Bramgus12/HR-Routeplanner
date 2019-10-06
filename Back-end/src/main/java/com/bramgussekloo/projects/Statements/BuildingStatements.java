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
}
