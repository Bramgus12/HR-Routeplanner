package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.Node;
import com.bramgussekloo.projects.Database.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeStatements {
    public static ArrayList<Node> getAllNodes(){
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<Node> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM node");
            while (result.next()){
                Integer id = result.getInt("id");
                BigDecimal x = result.getBigDecimal("x");
                BigDecimal y = result.getBigDecimal("y");
                BigDecimal z = result.getBigDecimal("z");
                String type = result.getString("type");
                Integer number = result.getInt("number");
                Integer building_id = result.getInt("building_id");
                String name = result.getString("name");
                Node node = new Node(id, x, y, z, type, number, building_id, name);
                list.add(node);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static String createNode(Node node){
        Connection conn = new DatabaseConnection().getConnection();
        BigDecimal x = node.getX();
        BigDecimal y = node.getY();
        BigDecimal z = node.getZ();
        String type = node.getType();
        Integer number = node.getNumber();
        Integer building_id = node.getBuilding_id();
        String name = node.getName();

        try {
            conn.createStatement().execute("INSERT INTO node VALUES(DEFAULT," + x + ", " + y + ", " + z + ", '" + type + "', " + number + ", " + building_id + ", '" + name + "')");
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
