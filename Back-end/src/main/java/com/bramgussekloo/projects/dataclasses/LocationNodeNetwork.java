package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;

@ApiModel(description = "Model of LocationNodeNetwork")
public class LocationNodeNetwork {

    @ApiModelProperty(notes = "Name of the location", required = true)
    private String locationName;

    @ApiModelProperty(notes = "All the nodes in the locationNodeNetwork", required = true)
    private ArrayList<Node> nodes;

    @ApiModelProperty(notes = "All the connections in the locationNodeNetwork", required = true)
    private ArrayList<ConnectedNode> connections;

    public LocationNodeNetwork(String locationName, ArrayList<Node> nodes, ArrayList<ConnectedNode> connections) {
        this.locationName = locationName;
        this.connections = connections;
        this.nodes = nodes;
    }

    public LocationNodeNetwork() {
    }

    public ArrayList<ConnectedNode> getConnections() {
        return connections;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public String getLocationName() {
        return locationName;
    }
}
