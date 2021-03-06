package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.InternalServerException;
import com.bramgussekloo.projects.config.DatabaseConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@ApiModel(description = "The model of the institute")
public class Institute {

    @ApiModelProperty(notes = "Autogenerated id by the database")
    private Integer id;

    @ApiModelProperty(notes = "Name of the institute")
    private String name;

    public Institute(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Institute() {
    }

    public Institute(int id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


    /**
     * @return A list with all the institute resources.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public static ArrayList<Institute> getAllInstitutes() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Institute> list = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM institute");
        if (!result.next()) {
            throw new BadRequestException("No data in database");
        } else {
            do {
                list.add(getResult(result));
            } while (result.next());
            return list;
        }
    }

    /**
     * This will override the data already present in the object.
     *
     * @param id The Id of the Institute resource
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getNameFromDatabase(Integer id) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Can't find institute with id " + id);
        } else {
            ResultSetInObject(resultSet);
        }
    }

    /**
     * This will override the data already present in the object.
     *
     * @param instituteName The Name of the Institute resource.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getIdFromDatabase(String instituteName) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
        preparedStatement.setString(1, instituteName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Can't find institute by name " + instituteName);
        } else {
            ResultSetInObject(resultSet);
        }
    }


    /**
     * Creates the object in the database. Id will be autogenerated by the database and will then be updated in the object.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void createInDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
        preparedStatement2.setString(1, this.name);
        ResultSet result = preparedStatement2.executeQuery();
        if (result.next()) {
            throw new BadRequestException("The institute you want to create, already exists");
        } else {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO institute VALUES (DEFAULT , ?);");
            preparedStatement.setString(1, this.name);
            preparedStatement.execute();
            PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM institute WHERE name=?;");
            preparedStatement1.setString(1, this.name);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (!resultSet.next()) {
                throw new BadRequestException("After creating the institute it can't be found anymore.");
            } else {
                this.id = resultSet.getInt("id");
            }
        }
    }

    /**
     * Will delete the resource in the database associated with this object.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void deleteInstitute() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement.setInt(1, this.id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new BadRequestException("Institute with id " + this.id + " doesn't exist");
        } else {
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM institute WHERE id =?;");
            preparedStatement1.setInt(1, this.id);
            preparedStatement1.execute();
        }
    }


    /**
     * Will update the name of the institute in the database.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void updateInstitute() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE institute SET name =? WHERE id=?;");
        preparedStatement.setString(1, this.name);
        preparedStatement.setInt(2, this.id);
        preparedStatement.execute();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM institute WHERE id=?;");
        preparedStatement1.setInt(1, this.id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new InternalServerException("Can't find institute after updating institute");
        }
    }

    /**
     * This will set the values of the resultSet in the object.
     *
     * @param resultSet The resultSet with the values that has to go in the object.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private void ResultSetInObject(ResultSet resultSet) throws Exception {
        this.name = resultSet.getString("name");
        this.id = resultSet.getInt("id");
    }

    /**
     * @param resultSet The resultSet you want as an object.
     * @return A institute object
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private static Institute getResult(ResultSet resultSet) throws Exception {
        String instituteName = resultSet.getString("name");
        int id = resultSet.getInt("id");
        return new Institute(id, instituteName);
    }
}
