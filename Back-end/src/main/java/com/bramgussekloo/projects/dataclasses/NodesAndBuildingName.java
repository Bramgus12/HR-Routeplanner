package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;


@ApiModel(description = "Model of a list of nodes and the locationName")
public class NodesAndBuildingName {

    @ApiModelProperty(notes = "List of nodes", required = true)
    private ArrayList<Node> nodes;

    @ApiModelProperty(notes = "The name of the location these nodes are in", required = true)
    private String locationName;

    public NodesAndBuildingName(ArrayList<Node> nodes, String locationName) {
        this.nodes = nodes;
        this.locationName = locationName;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
