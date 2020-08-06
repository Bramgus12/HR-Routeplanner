package com.bramgussekloo.projects.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model of a connection between two nodes")
public class ConnectedNode {

    @ApiModelProperty(notes = "The first node", required = true)
    private Integer node1;

    @ApiModelProperty(notes = "The second node", required = true)
    private Integer node2;

    @ApiModelProperty(notes = "The distance between the two nodes", required = true)
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
