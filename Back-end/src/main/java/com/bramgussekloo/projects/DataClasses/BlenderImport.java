package com.bramgussekloo.projects.DataClasses;

import java.util.ArrayList;

public class BlenderImport {
    private String locationName;
    private ArrayList<Node> nodes;
    private ArrayList<ConnectedNode> connections;

    public BlenderImport(String locationName, ArrayList<Node> nodes, ArrayList<ConnectedNode> connections){
        this.locationName = locationName;
        this.connections = connections;
        this.nodes = nodes;
    }

    public BlenderImport(){}

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
