package com.bramgussekloo.projects.models;


import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.utilities.GetPropertyValues;
import com.bramgussekloo.projects.utilities.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@ApiModel(description = "All details from Address")
public class Address {
    @ApiModelProperty(notes = "The database generated id")
    private Integer id;

    @ApiModelProperty(notes = "Street. You should know what a street is", required = true)
    private String street;

    @ApiModelProperty(notes = "How do you not know why there is a number in an address", required = true)
    private Integer number;

    @ApiModelProperty(notes = "https://www.youtube.com/watch?v=K1b8AhIsSYQ", required = true)
    private String city;

    @ApiModelProperty(notes = "Postal code that belongs to the address", required = true)
    private String postal;

    @ApiModelProperty(notes = "The addition (dutch = \"toevoeging\") of the address")
    private String addition;

    public Address(Integer id, String street, Integer number, String city, String postal, String addition) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal = postal;
        this.addition = addition;
    }

    public Address() {
    }

    public Address(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public boolean ObjectIsEmpty() {
        return this.city == null && this.street == null && this.addition == null && this.number == null && this.postal == null && this.id == null;
    }

    /**
     * @return An arraylist with all the addresses that are in the database.
     * @throws Exception Will be handled by the HandleException class
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public static ArrayList<Address> getAllFromDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Address> list = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address");
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new BadRequestException("No data in database");
        } else {
            do {
                list.add(getResult(result.getInt("id"), result));
            } while (result.next());
            return list;
        }
    }

    /**
     * This method will delete the Address resource which is in the object. Does not delete this object.
     *
     * @throws Exception Will be handled by the HandleException class
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void deleteFromDatabase() throws Exception {
        if (ObjectIsEmpty()) {
            throw new BadRequestException("Can't delete an empty object");
        }
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=?;");
        preparedStatement.setInt(1, this.id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Address doesn't exist");
        } else {
            setResultSetInObject(this.id, resultSet);
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM address where id=?;");
            preparedStatement1.setInt(1, this.id);
            preparedStatement1.execute();
        }
    }

    /**
     * !Overrides the data that is already in the object!
     *
     * @param id The id of the address you want to get from the database
     * @throws Exception Will be handled by the HandleException class
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getFromDatabase(Integer id) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM address WHERE id=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Address with id " + id + " does not exist");
        } else {
            setResultSetInObject(id, resultSet);
        }
    }

    /**
     * !Overrides the data that is already in the object!
     *
     * @param code The code you want the address from.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getFromDatabaseByRoomCode(String code) throws Exception {
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
                throw new BadRequestException("No address found at this roomCode");
            } else {
                setResultSetInObject(resultSet.getInt("id"), resultSet);
            }
        }
    }

    /**
     * Get an address by building name. This will update the values in the object!
     *
     * @param buildingName The buildingName you want to get the address of.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getFromDatabaseByBuildingName(String buildingName) throws Exception {
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
                throw new BadRequestException("Address does not exist at this buildingName");
            } else {
                setResultSetInObject(resultSet.getInt("id"), resultSet);
            }
        } else {
            throw new BadRequestException("Building does not exist");
        }
    }


    /**
     * Creates an Address in the database by the values that are in this object. Updates id to the new value.
     *
     * @throws Exception This will be handled by the HandleException class
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void createInDatabase() throws Exception {
        ArrayList<Address> list = getAllFromDatabase();
        for (Address address :
                list) {
            if (
                    // TODO: Gives nullPointerException if a value is null.
                    this.street.equals(address.getStreet()) && this.postal.equals(address.getPostal()) && this.number.equals(address.getNumber())
                            && this.addition.equals(address.getAddition()) && this.city.equals(address.getCity())
            ) {
                throw new BadRequestException("Address already exists and has id: " + address.getId());
            }
        }
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO address VALUES (DEFAULT , ?, ?, ?, ?, ?); ");
        preparedStatement1.setString(1, this.street);
        preparedStatement1.setInt(2, this.number);
        preparedStatement1.setString(3, this.city);
        preparedStatement1.setString(4, this.postal);
        preparedStatement1.setString(5, this.addition);
        preparedStatement1.execute();
        PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT id FROM address WHERE street=? AND number=? AND postal=? AND city=? AND addition=?;");
        preparedStatement2.setString(1, street);
        preparedStatement2.setInt(2, number);
        preparedStatement2.setString(3, postal);
        preparedStatement2.setString(4, city);
        preparedStatement2.setString(5, addition);
        ResultSet resultSet = preparedStatement2.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Getting the address after it has been posted failed.");
        } else {
            this.id = resultSet.getInt("id");
        }
    }

    /**
     * Updates the resource in the database from this address object.
     *
     * @throws Exception Will be handled by the HandleException class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void updateInDatabase() throws Exception {
        if (this.id == null) {
            throw new BadRequestException("Id field is empty");
        }
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE address SET number=?, street=?, postal=?, city=?, addition=? WHERE id=?; ");
        ps.setInt(1, this.number);
        ps.setString(2, this.street);
        ps.setString(3, this.postal);
        ps.setString(4, this.city);
        ps.setString(5, this.addition);
        ps.setInt(6, this.id);
        ps.execute();
    }

    /**
     * This method will get all the data out of a resultSet. Id has to be given separately.
     * Can be done by already extracting the id from the resultSet by doing resultSet.getInt("id") .
     *
     * @param id        The id of the object that you want to retrieve from the resultSet.
     * @param resultSet The resultSet you want to retrieve the data from.
     * @return An Address object which contains everything from the resultSet.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private static Address getResult(Integer id, ResultSet resultSet) throws Exception {
        String streetResult = resultSet.getString("street");
        Integer numberResult = resultSet.getInt("number");
        String cityResult = resultSet.getString("city");
        String postalResult = resultSet.getString("postal");
        String additionResult = resultSet.getString("addition");
        return new Address(id, streetResult, numberResult, cityResult, postalResult, additionResult);
    }

    /**
     * Sets the data from the resultSet in the object.
     *
     * @param id        The Id you want to insert in the object.
     * @param resultSet The resultSet with the data that has to go in the Object
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private void setResultSetInObject(int id, ResultSet resultSet) throws Exception {
        this.street = resultSet.getString("street");
        this.addition = resultSet.getString("addition");
        this.city = resultSet.getString("city");
        this.number = resultSet.getInt("number");
        this.postal = resultSet.getString("postal");
        this.id = id;
    }
}

