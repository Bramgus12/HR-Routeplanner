package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.InternalServerException;
import com.bramgussekloo.projects.config.SecurityConfig;
import com.bramgussekloo.projects.config.DatabaseConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@ApiModel(description = "All details from User")
public class User {

    @ApiModelProperty(notes = "Auto-assigned id of the user")
    private int id;

    @ApiModelProperty(notes = "The name of the user", required = true)
    private String user_name;

    @ApiModelProperty(notes = "The password. It will be encrypted after making it", required = true)
    private String password;

    @ApiModelProperty(notes = "Authority should be either \"ROLE_USER\" or \"ROLE_ADMIN\", Admin can make users and normal users can't", required = true)
    private String authority;

    @ApiModelProperty(notes = "Should be set to true if the user wants to use the features in the api.", required = true)
    private Boolean enabled;

    public User(int id, String user_name, String password, String authority, Boolean enabled) {
        this.id = id;
        this.password = password;
        this.user_name = user_name;
        this.authority = authority;
        this.enabled = enabled;
    }

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public static ArrayList<User> getAllFromDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<User> list = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users");
        ResultSet result = preparedStatement.executeQuery();
        if (!result.next()) {
            throw new BadRequestException("No data in database");
        } else {
            do {
                list.add(getResult(result));
            } while (result.next());
            return list;
        }
    }

    public void updateInDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        this.password = SecurityConfig.HashUserPassword(this.password);
        PreparedStatement ps = conn.prepareStatement("UPDATE users SET user_name=?, password=?, enabled=?, authority=? WHERE id=?; ");
        ps.setString(1, this.user_name);
        ps.setString(2, this.password);
        ps.setBoolean(3, this.enabled);
        ps.setString(4, this.authority);
        ps.setInt(5, this.id);
        ps.executeUpdate();
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT * FROM users WHERE id=?;");
        preparedStatement1.setInt(1, this.id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (!resultSet.next()) {
            throw new InternalServerException("User doesn't exist on this id after updating");
        } else {
            setResultInObject(resultSet);
        }
    }

    public void createInDatabase() throws Exception {
        boolean userExists = false;
        ArrayList<User> users = getAllFromDatabase();
        for (User userFromList : users) {
            if (this.user_name.equals(userFromList.getUser_name())) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            if (!this.password.isEmpty() && this.password.length() > 6) {
                Connection conn = new DatabaseConnection().getConnection();
                this.password = SecurityConfig.HashUserPassword(this.password);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?)");
                preparedStatement.setString(1, this.user_name);
                preparedStatement.setString(2, this.password);
                preparedStatement.setString(3, this.authority);
                preparedStatement.setBoolean(4, this.enabled);
                preparedStatement.execute();
            } else {
                throw new BadRequestException("Password is not long enough. It has to be at least a length of 6.");
            }
        } else {
            throw new BadRequestException("User already exists.");
        }
    }

    public void deleteInDatabase() throws SQLException{
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE id=?;");
        preparedStatement.setInt(1, this.id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("User doesn't exist.");
        } else {
            PreparedStatement preparedStatement1 = conn.prepareStatement("DELETE FROM users where id=?;");
            preparedStatement1.setInt(1, id);
            preparedStatement1.execute();
            setResultInObject(resultSet);
        }

    }

    private static User getResult(ResultSet resultSet) throws SQLException {
        String user_nameResult = resultSet.getString("user_name");
        String passwordResult = resultSet.getString("password");
        String authorityResult = resultSet.getString("authority");
        Boolean enabledResult = resultSet.getBoolean("enabled");
        int id = resultSet.getInt("id");
        return new User(id, user_nameResult, passwordResult, authorityResult, enabledResult);
    }

    private void setResultInObject(ResultSet resultSet) throws SQLException {
        this.user_name = resultSet.getString("user_name");
        this.password = resultSet.getString("password");
        this.authority = resultSet.getString("authority");
        this.enabled = resultSet.getBoolean("enabled");
        this.id = resultSet.getInt("id");
    }



}
