package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.dataclasses.Address;
import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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
            list.add(getResult(result.getInt("id"), result));
        }
        return list;
    }

    public static Address getAddress(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id);
        result.next();
        return getResult(id, result);
    }

    public static Address getAddressByRoomCode(String code) throws SQLException, IOException {
        Connection conn = new DatabaseConnection().getConnection();
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        String locationName = "";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                for (Node node : network.getNodes()){
                    if (node.getType().equals("Room") && node.getCode().toLowerCase().equals(code.toLowerCase())){
                        locationName = network.getLocationName();
                        break;
                    }
                }
            }
        }
        System.out.println(code);
        if (locationName.isEmpty()){
            throw new IOException("Room cannot be found in the locationNodeNetworks");
        } else {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=(SELECT address_id FROM building WHERE name='" + locationName + "');");
            resultSet.next();
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    public static Address getAddressByBuildingName(String buildingName) throws SQLException, IOException {
        Connection conn = new DatabaseConnection().getConnection();
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        String name = "";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                if (network.getLocationName().equals(buildingName)) {
                    name = buildingName;
                    break;
                }
            }
        }
        if (!name.isEmpty()) {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=(SELECT building.address_id FROM building WHERE name='" + name + "')");
            resultSet.next();
            return getResult(resultSet.getInt("id"), resultSet);
        } else {
            throw new IOException("Building does not exist");
        }
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
        return getResult(resultSet.getInt("id"), resultSet);
    }

    public static Address deleteAddress(Integer id) throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id + ";");
        resultSet.next();
        Address address = getResult(id, resultSet);
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
        conn.createStatement().execute("UPDATE address SET number=" + number + ", street='"
                + street + "', postal='" + postal + "', city='" + city + "' WHERE id=" + id + "; ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id + ";");
        resultSet.next();
        return getResult(id, resultSet);
    }
    
    private static Address getResult(Integer id, ResultSet resultSet) throws SQLException{
        String streetResult = resultSet.getString("street");
        Integer numberResult = resultSet.getInt("number");
        String cityResult = resultSet.getString("city");
        String postalResult = resultSet.getString("postal");
        return new Address(id, streetResult, numberResult, cityResult, postalResult);
    }
}
