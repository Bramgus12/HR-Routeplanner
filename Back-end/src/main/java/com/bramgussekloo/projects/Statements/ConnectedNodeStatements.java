package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.ConnectedNode;
import com.bramgussekloo.projects.Database.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectedNodeStatements {
    public static ArrayList<ConnectedNode> getAllConnectedNodes() {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<ConnectedNode> list = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM connected_node");
            while (result.next()) {
                Integer id = result.getInt("id");
                Integer node_id_1 = result.getInt("node_id_1");
                Integer node_id_2 = result.getInt("node_id_2");
                BigDecimal distance = result.getBigDecimal("distance");
                ConnectedNode connectedNode = new ConnectedNode(id, node_id_1, node_id_2, distance);
                list.add(connectedNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ConnectedNode getConnectedNode(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            ResultSet result = conn.createStatement().executeQuery("SELECT * FROM connected_node WHERE id=" + id);
            while (result.next()){
                Integer node_id_1 = result.getInt("node_id_1");
                Integer node_id_2 = result.getInt("node_id_2");
                BigDecimal distance = result.getBigDecimal("distance");
                return new ConnectedNode(id, node_id_1, node_id_2, distance);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new ConnectedNode();
    }

    public static String createConnectedNode(ConnectedNode connectedNode) {
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = connectedNode.getId();
        Integer node_id_1 = connectedNode.getNode1();
        Integer node_id_2 = connectedNode.getNode2();
        BigDecimal distance = connectedNode.getDistance();
        try {
            conn.createStatement().execute("INSERT INTO connected_node VALUES (DEFAULT, " + node_id_1 + ", " + node_id_2 + ", " + distance + ");");
            return "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String deleteConnectedNode(Integer id){
        Connection conn = new DatabaseConnection().getConnection();
        try{
            conn.createStatement().execute("DELETE FROM connected_node WHERE id=" + id);
            return "yes";
        } catch (SQLException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String updateConnectedNode(ConnectedNode connectedNode){
        Connection conn = new DatabaseConnection().getConnection();
        Integer id = connectedNode.getId();
        Integer node_id_1 = connectedNode.getNode1();
        Integer node_id_2 = connectedNode.getNode2();
        BigDecimal distance = connectedNode.getDistance();
        String output;

        try{
            conn.createStatement().execute("UPDATE connected_node SET node_id_1=" + node_id_1 + ", node_id_2='" + node_id_2 + ", distance='" + distance + "' WHERE id=" + id );
            output = "yes";
        } catch (SQLException e) {
            e.printStackTrace();
            output = e.getMessage();
        }
        return output;
    }
}

