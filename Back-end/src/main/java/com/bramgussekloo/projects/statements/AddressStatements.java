package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.Address;
import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressStatements {
    public static ArrayList<Address> getAllAddresses() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Address> list = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address");
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new SQLException("No data in database");
        } else {
            do {
                list.add(getResult(result.getInt("id"), result));
            } while (result.next());
            return list;
        }
    }

    public static Address getAddress(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Address with id " + id + " does not exist");
        } else {
            return getResult(id, resultSet);
        }
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
                for (Node node : network.getNodes()) {
                    if (node.getType().equals("Room") && node.getCode().toLowerCase().equals(code.toLowerCase())) {
                        locationName = network.getLocationName();
                        break;
                    }
                }
            }
        }
        if (locationName.isEmpty()) {
            throw new IOException("Room cannot be found in the locationNodeNetworks");
        } else {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=(SELECT address_id FROM building WHERE name=?);");
            preparedStatement.setString(1, locationName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("No address found at this roomCode");
            } else {
                return getResult(resultSet.getInt("id"), resultSet);
            }
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
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=(SELECT building.address_id FROM building WHERE name=?);");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("Address does not exist at this buildingName");
            } else {
                return getResult(resultSet.getInt("id"), resultSet);
            }
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
        String addition = address.getAddition();
        PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO address VALUES (DEFAULT , ?, ?, ?, ?, ?); ");
        preparedStatement1.setString(1, street);
        preparedStatement1.setInt(2, number);
        preparedStatement1.setString(3, city);
        preparedStatement1.setString(4, postal);
        preparedStatement1.setString(5, addition);
        preparedStatement1.execute();
        PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT * FROM address WHERE street=? AND number=? AND postal=? AND city=? AND addition=?;");
        preparedStatement2.setString(1, street);
        preparedStatement2.setInt(2, number);
        preparedStatement2.setString(3, postal);
        preparedStatement2.setString(4, city);
        preparedStatement2.setString(5, addition);
        ResultSet resultSet = preparedStatement2.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Getting the address after it has been posted failed.");
        } else {
            return getResult(resultSet.getInt("id"), resultSet);
        }
    }

    public static Address deleteAddress(Integer id) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Address doesn't exist.");
        } else {
            Address address = getResult(id, resultSet);
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM address where id=?;");
            preparedStatement1.setInt(1, id);
            preparedStatement1.execute();
            return address;
        }
    }

    public static Address updateAddress(Address address) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = address.getId();
        Integer number = address.getNumber();
        String street = address.getStreet();
        String postal = address.getPostal();
        String city = address.getCity();
        String addition = address.getAddition();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE address SET number=?, street=?, postal=?, city=?, addition=? WHERE id=?; ");
        preparedStatement.setInt(1, number);
        preparedStatement.setString(2, street);
        preparedStatement.setString(3, postal);
        preparedStatement.setString(4, city);
        preparedStatement.setString(5, addition);
        preparedStatement.setInt(6, id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM address WHERE id=?;");
        preparedStatement1.setInt(1, id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Address doesn't exist on this id after updating");
        } else {
            return getResult(id, resultSet);
        }
    }

    private static Address getResult(Integer id, ResultSet resultSet) throws SQLException {
        String streetResult = resultSet.getString("street");
        Integer numberResult = resultSet.getInt("number");
        String cityResult = resultSet.getString("city");
        String postalResult = resultSet.getString("postal");
        String additionResult = resultSet.getString("addition");
        return new Address(id, streetResult, numberResult, cityResult, postalResult, additionResult);
    }
}
