package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Database.DatabaseConnection;
import org.graalvm.compiler.code.DataSection;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressStatements {
    public static ArrayList<Address> getAllAddresses() throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Address> list = new ArrayList<>();
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
    }

    public static Address getAddress(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id);
        result.next();
        String street = result.getString("street");
        Integer number = result.getInt("number");
        String city = result.getString("city");
        String postal = result.getString("postal");
        return new Address(id, street, number, city, postal);
    }

    public static Address createAddress(Address address) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        conn.createStatement().execute("INSERT INTO address VALUES (DEFAULT , '" + street +
                "', " + number + ", '" + city + "', '" + postal + "'); ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE street='" +
                street + "' AND number=" + number + " AND postal='" + postal + "' AND city='" + city + "';");
        resultSet.next();
        return new Address(resultSet.getInt("id"), street, number, city, postal);
    }

    public static Address deleteAddress(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id + ";");
        resultSet.next();
        Address address = new Address(resultSet.getInt("id"), resultSet.getString("street"), resultSet.getInt("number"), resultSet.getString("city"), resultSet.getString("postal"));
        conn.createStatement().execute("DELETE FROM address WHERE id=" + id);
        return address;
    }
    public static Address updateAddress(Address address) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = address.getId();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        ResultSet resultSet = conn.createStatement().executeQuery("UPDATE address SET number=" + number + ", street='"
                + street + "', postal='" + postal + "', city='" + city + "' WHERE id=" + id + "; " +
                "SELECT * FROM address WHERE id=" + id + ";" );
        String streetResult = resultSet.getString("street");
        Integer numberResult = resultSet.getInt("number");
        String cityResult = resultSet.getString("city");
        String postalResult = resultSet.getString("postal");
        return new Address(id, streetResult, numberResult, cityResult, postalResult);
    }
}
