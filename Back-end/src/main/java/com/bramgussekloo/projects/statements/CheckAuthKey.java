package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckAuthKey {
    public static boolean checkKey(String authKey) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<User> list = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users");
        ResultSet result = preparedStatement.executeQuery();
        boolean returnValue = false;
        if (!result.next()) {
            throw new SQLException("No data in database");
        } else {
            do {
                list.add(getResult(result.getInt("id"), result));
            } while (result.next());
            for (User user : list) {
                if (authKey.equals(user.getAuth_key())) {
                    returnValue = true;
                }
            }
            return returnValue;
        }
    }

    private static User getResult(Integer id, ResultSet resultSet) throws SQLException {
        String userNameResult = resultSet.getString("user_name");
        String authKeyResult = resultSet.getString("auth_key");
        return new User(id, userNameResult, authKeyResult);
    }

}
