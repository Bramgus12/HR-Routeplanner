package com.bramgussekloo.projects.Database;

import com.bramgussekloo.projects.DataClasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Statements {

    public static Boolean ExecuteQuery(String SQLQuery)  {
        Connection conn = new DatabaseConnection().getConnection();
        if (conn != null) {
            try {
                conn.createStatement().execute(SQLQuery);
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static Boolean createTable(String tableName, String Columns){
        Connection conn = new DatabaseConnection().getConnection();
        try {
            conn.createStatement().execute("CREATE TABLE " + tableName + "(" + Columns + ")");
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Address> getAddressByStreetAndNumber(String street, Integer number){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Address> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM address WHERE street=" + street + " AND number=" + number);
            while (result.next()){
                Integer id = result.getInt("id");
                street = result.getString("street");
                number = result.getInt("number");
                String city = result.getString("city");
                String postal = result.getString("postal");
                Address address = new Address(id, street, number, city, postal);
                list.add(address);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Address> getAllAddresses(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Address> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM address");
            while (result.next()){
                Integer id = result.getInt("id");
                String street = result.getString("street");
                Integer number = result.getInt("number");
                String city = result.getString("city");
                String postal = result.getString("postal");
                Address address = new Address(id, street, number, city, postal);
                list.add(address);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Node> getAllNodes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Node> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM node");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer x = result.getInt("x");
                Integer y = result.getInt("y");
                Integer z = result.getInt("z");
                String type = result.getString("type");
                Node node = new Node(id, x, y, z, type);
                list.add(node);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Institute> getAllInstitutes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Institute> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM institute");
            while (result.next()){
                Integer id = result.getInt("id");
                String name = result.getString("name");
                Institute institute = new Institute(id, name);
                list.add(institute);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<EntryDest> getAllEntryDest(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<EntryDest> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM entry_dest");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer node_id = result.getInt("node_id");
                Integer building_id = result.getInt("building_id");
                Integer level = result.getInt("level");
                String name = result.getString("name");
                EntryDest entryDest = new EntryDest(id, node_id, building_id, level, name);
                list.add(entryDest);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ConnectedNode> getAllConnectedNodes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<ConnectedNode> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM connected_node");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer node_id_1 = result.getInt("node_id_1");
                Integer node_id_2 = result.getInt("node_id_2");
                Integer distance = result.getInt("distance");
                ConnectedNode connectedNode = new ConnectedNode(id, node_id_1, node_id_2, distance);
                list.add(connectedNode);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<BuildingInstitute> getAllBuildingInstitutes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<BuildingInstitute> list = new ArrayList<>();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer building_id = result.getInt("building_id");
                Integer institute_id = result.getInt("institute_id");
                BuildingInstitute buildingInstitute = new BuildingInstitute(id, building_id, institute_id);
                list.add(buildingInstitute);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Building> getAllBuildings(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Building> list = new ArrayList<>();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building_institute");
            while (result.next()){
                Integer id = result.getInt("id");
                Integer address_id = result.getInt("address_id");
                String name = result.getString("name");
                Building building = new Building(id, address_id, name);
                list.add(building);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
