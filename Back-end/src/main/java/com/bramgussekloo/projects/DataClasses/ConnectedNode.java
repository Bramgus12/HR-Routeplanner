package com.bramgussekloo.projects.DataClasses;

public class ConnectedNode {
    private Integer id, node_id_1, node_id_2, distance;

    public ConnectedNode (Integer id, Integer node_id_1, Integer node_id_2, Integer distance){
        this.id = id;
        this.node_id_1 = node_id_1;
        this.node_id_2 = node_id_2;
        this.distance = distance;
    }
    
    public ConnectedNode(){}

    public Integer getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getNode_id_1() {
        return node_id_1;
    }

    public Integer getNode_id_2() {
        return node_id_2;
    }
}
