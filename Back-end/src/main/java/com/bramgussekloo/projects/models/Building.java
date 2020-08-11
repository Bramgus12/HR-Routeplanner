package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.config.DatabaseConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@ApiModel(description = "The model of the building")
public class Building {

    @ApiModelProperty(notes = "Autogenerated id of the building")
    private Integer id;

    @ApiModelProperty(notes = "Id of the address", required = true)
    private Integer address_id;

    @ApiModelProperty(notes = "Name of the building", required = true)
    private String name;

    public Building(Integer id, Integer address_id, String name) {
        this.address_id = address_id;
        this.id = id;
        this.name = name;
    }

    public Building() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAddress_id(Integer address_id) {
        this.address_id = address_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets you all the building from the database.
     *
     * @return A list of all the Building resources
     * @throws Exception Will be handled by the HandleExceptions class
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public static ArrayList<Building> getAllBuildingsFromDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Building> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM building");
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
     * This will extract the ResultSet into a java object. You need to give the id of that object.
     * This can be retrieved ny doing resultSet.getInt("id");
     *
     * @param id The id of the object you want the result of.
     * @param resultSet The resultSet you want to be extracted.
     * @return A Building with the values from the resultSet.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private static Building getResult(Integer id, ResultSet resultSet) throws Exception {
        Integer address_id = resultSet.getInt("address_id");
        String name = resultSet.getString("name");
        return new Building(id, address_id, name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    /**
     * This will override all the values in the object!
     *
     * @param id The Id of the building you want to retrieve.
     * @throws Exception Will be handled by the HanldeExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getBuildingFromDatabase(Integer id) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE id=?");
        preparedStatement.setInt(1, id);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new BadRequestException("Building with id " + id + " can't be found");
        } else {
            setResultInObject(result.getInt("id"), result);
        }
    }

    /**
     * This will override all the values in the object!
     *
     * @param name The name of the building you want to have the building object of.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getFromDatabaseByName(String name) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE name=?;");
        preparedStatement.setString(1, name);
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new BadRequestException("Building with name " + name + " can't be found");
        } else {
            setResultInObject(result.getInt("id"), result);
        }
    }

    /**
     * This will create the building object in the database and will update the id value with the new id that has been given by the database.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void createInDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE address_id=? AND name=?;");
        preparedStatement.setInt(1, this.address_id);
        preparedStatement.setString(2, this.name);
        ResultSet result = preparedStatement.executeQuery();

        if (!result.next()) {
            PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO building VALUES (DEFAULT, ?, ?); ");
            preparedStatement1.setInt(1, this.address_id);
            preparedStatement1.setString(2, this.name);
            preparedStatement1.execute();
            PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT * FROM building WHERE address_id=? AND name=?;");
            preparedStatement2.setInt(1, this.address_id);
            preparedStatement2.setString(2, this.name);
            ResultSet resultSet = preparedStatement2.executeQuery();
            if (!resultSet.next()) {
                throw new BadRequestException("After creating the building can't be found anymore.");
            } else {
                this.id = resultSet.getInt("id");
            }
        } else {
            this.id = result.getInt("id");
        }
    }

    /**
     * This will delete the value in the database. This only has to have the id.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void deleteBuilding() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM building WHERE id=?;");
        preparedStatement.setInt(1, this.id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Building with id " + this.id + " doesn't exist");
        } else {
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM building WHERE id=?;");
            preparedStatement1.setInt(1, this.id);
            preparedStatement1.execute();
        }
    }

    /**
     * This will update the resource in the database with the new values.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void updateBuilding() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE building SET address_id=?, name=? WHERE id=?;");
        preparedStatement.setInt(1, this.address_id);
        preparedStatement.setString(2, this.name);
        preparedStatement.setInt(3, this.id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM building WHERE id=?;");
        preparedStatement1.setInt(1, this.id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Can't find building after updating");
        }
    }

    /**
     * @param id This is the id you want to have in the object. Can be retrieved by using resultSet.getInt("id")
     * @param resultSet The resultSet you want o have the values of in the object.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private void setResultInObject(Integer id, ResultSet resultSet) throws Exception {
        this.address_id = resultSet.getInt("address_id");
        this.name = resultSet.getString("name");
        this.id = id;
    }
}
