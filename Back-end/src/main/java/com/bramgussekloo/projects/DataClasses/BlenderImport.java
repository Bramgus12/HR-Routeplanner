package com.bramgussekloo.projects.DataClasses;

import java.util.ArrayList;

public class BlenderImport {
    private String locationName;
    private ArrayList<Node> nodeList;
    private ArrayList<ConnectedNode> connectedNodeList;

    public BlenderImport(String locationName, ArrayList<Node> nodeList, ArrayList<ConnectedNode> connectedNodeList){
        this.locationName = locationName;
        this.connectedNodeList = connectedNodeList;
        this.nodeList = nodeList;
    }

    public ArrayList<ConnectedNode> getConnectedNodeList() {
        return connectedNodeList;
    }

    public ArrayList<Node> getNodeList() {
        return  nodeList;
    }

    public String getLocationName() {
        return locationName;
    }
}
