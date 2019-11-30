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

    public static Address getAddressByRoomNumber(String code) throws SQLException, IOException {
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
                    if (node.getType().equals("Room") && node.getCode().toLowerCase().replace(".", "").equals(code.toLowerCase())){
                        locationName = network.getLocationName();
                        break;
                    }
                }
            }
        }
        if (locationName.isEmpty()){
            throw new IOException("Room cannot be found in the locationNodeNetworks");
        } else {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=(SELECT address_id FROM building WHERE name='" + locationName + "');");
            resultSet.next();
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    public static Address createAddress(Address address) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        conn.createStatement().execute("INSERT INTO address VALUES (DEFAULT , '" + street +
                "', " + number + ", '" + city + "', '" + postal + "', " + latitude + ", " + longitude + "); ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE street='" +
                street + "' AND number=" + number + " AND postal='" + postal + "' AND city='" + city + "' AND latitude=" + latitude + " AND longitude= " + longitude + ";");
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
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        conn.createStatement().execute("UPDATE address SET number=" + number + ", street='"
                + street + "', postal='" + postal + "', city='" + city + "', latitude="+ latitude + ", longitude="+ longitude +" WHERE id=" + id + "; ");
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM address WHERE id=" + id + ";");
        resultSet.next();
        return getResult(id, resultSet);
    }
    
    private static Address getResult(Integer id, ResultSet resultSet) throws SQLException{
        String streetResult = resultSet.getString("street");
        Integer numberResult = resultSet.getInt("number");
        String cityResult = resultSet.getString("city");
        String postalResult = resultSet.getString("postal");
        double latitudeResult = resultSet.getDouble("latitude");
        double longitudeResult = resultSet.getDouble("longitude");
        return new Address(id, streetResult, numberResult, cityResult, postalResult, latitudeResult, longitudeResult);
    }
}
