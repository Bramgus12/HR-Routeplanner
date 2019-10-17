package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InstituteStatements {
    public static ArrayList<Institute> getAllInstitutes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Institute> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM institute");
            while (result.next()){
                Integer id = result.getInt("id");
                String instituteName = result.getString("name");
                Institute institute = new Institute(id, instituteName);
                list.add(institute);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public static String createInstitute(Institute institute){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = institute.getId();
        String institute_name = institute.getName();
        try{
            conn.createStatement().execute("INSERT INTO institute VALUES (" + id + ", " + id + ");");
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
    public static String deleteBuildingInstitute(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            conn.createStatement().execute("DELETE FROM building_institute WHERE id=" + id);
            return "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
