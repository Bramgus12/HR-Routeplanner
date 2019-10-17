package com.bramgussekloo.projects.dataclasses;

public class ConnectedNode {
    private Integer node1, node2;
    private double distance;

    public ConnectedNode(Integer node1, Integer node2, double distance) {
        this.node1 = node1;
        this.node2 = node2;
        this.distance = distance;
    }

    public ConnectedNode() {
    }

    public double getDistance() {
        return distance;
    }

    public Integer getNode1() {
        return node1;
    }

    public Integer getNode2() {
        return node2;
    }
}
