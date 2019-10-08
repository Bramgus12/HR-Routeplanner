package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Database.DatabaseConnection;
import org.graalvm.compiler.code.DataSection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressStatements {
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
            e.printStackTrace();
        }
        return list;
    }

    public static Address getAddress(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id);
            while (result.next()) {
                String street = result.getString("street");
                Integer number = result.getInt("number");
                String city = result.getString("city");
                String postal = result.getString("postal");
                return new Address(id, street, number, city, postal);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new Address();
    }

    public static String createAddress(Address address){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = address.getId();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        try{
            conn.createStatement().execute("INSERT INTO address VALUES (" + id + ", '" + street + "', " + number + ", '" + city + "', '" + postal + "');");
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String deleteAddress(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            conn.createStatement().execute("DELETE FROM address WHERE id=" + id);
            return "yes";
        } catch(SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    public static String updateAddress(Address address){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = address.getId();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        String output = "";

        try{
            conn.createStatement().execute("UPDATE address SET number=" + number + ", street='" + street + "', postal='" + postal + "', city='" + city + "' WHERE id=" + id );
            output = "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            output = e.getMessage();
        }
        return output;
    }
}
