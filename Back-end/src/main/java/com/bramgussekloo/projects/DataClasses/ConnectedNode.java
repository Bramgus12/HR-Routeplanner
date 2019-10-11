package com.bramgussekloo.projects.DataClasses;

import java.math.BigDecimal;

public class ConnectedNode {
    private Integer id, node1, node2;
    private BigDecimal distance;

    public ConnectedNode (Integer id, Integer node1, Integer node2, BigDecimal distance){
        this.id = id;
        this.node1 = node1;
        this.node2 = node2;
        this.distance = distance;
    }
    
    public ConnectedNode(){}

    public Integer getId() {
        return id;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public Integer getNode1() {
        return node1;
    }

    public Integer getNode2() {
        return node2;
    }
}
