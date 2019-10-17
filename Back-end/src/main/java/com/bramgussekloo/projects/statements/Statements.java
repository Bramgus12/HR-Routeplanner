package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.dataclasses.*;
import com.bramgussekloo.projects.database.DatabaseConnection;

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
}
