package com.bramgussekloo.projects.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Statements {

    public static Boolean ExecuteQuery(String SQLQuery)  {
        Connection conn = new DatabaseConnection().getConnection();
        try {
            conn.createStatement().execute(SQLQuery);
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
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
    public static void getAddressByStreetAndNumber(String street, String number){
        Connection conn = new DatabaseConnection().getConnection();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM 'address' WHERE street=" + street + "AND number=" + number);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Address> getAddressList(){
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
            return list;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return list;
        }
    }
}
