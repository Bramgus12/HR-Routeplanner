package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.utilities.GetPropertyValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
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

    /**
     * Will give you a list of all the nodes which are in all the json files.
     *
     * @return A list of all the rooms in all the json files.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public static ArrayList<NodesAndBuildingName> getAllRooms() throws Exception {
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        ArrayList<NodesAndBuildingName> nodeArrayList = new ArrayList<>();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                ArrayList<Node> nodes = new ArrayList<>();
                for (Node node : network.getNodes()) {
                    if (node.getType().equals("Room")) {
                        String label = node.getLabel().toLowerCase();
                        if (!label.isEmpty()) {
                            if (!label.contains("trap") && !label.contains("lift")
                                    && !label.contains("toilet") && !label.contains("gang") && !label.contains("hal")
                                    && !label.contains("berging") && !label.contains("portaal")) {
                                nodes.add(node);
                            }
                        } else {
                            nodes.add(node);
                        }
                    }
                }
                NodesAndBuildingName rnib = new NodesAndBuildingName(nodes, network.getLocationName());
                nodeArrayList.add(rnib);
            }
        }
        return nodeArrayList;
    }
}
