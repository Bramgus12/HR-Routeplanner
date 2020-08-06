package com.bramgussekloo.projects.dataclasses;

import com.bramgussekloo.projects.Exceptions.BadRequestException;
import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.util.ArrayList;


@ApiModel(description = "Model of the node")
public class Node {

    @ApiModelProperty(notes = "Unique number of the node", required = true)
    private Integer number;

    @ApiModelProperty(notes = "The type of the node", required = true)
    private String type;

    @ApiModelProperty(notes = "The room code", required = false)
    private String code;

    @ApiModelProperty(notes = "The label that corresponds with the room code", required = false)
    private String label;

    @ApiModelProperty(notes = "The x value of the node", required = true)
    private double x;

    @ApiModelProperty(notes = "The y value of the node", required = true)
    private double y;

    @ApiModelProperty(notes = "The z value of the node", required = true)
    private double z;

    public Node(Integer id, String type, String code, String label, double x, double y, double z) {
        this.number = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.code = code;
        this.label = label;
    }

    public Node() {
    }

    public Integer getNumber() {
        return number;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * This will create a list of nodes which al have the type you define.
     *
     * @param locationName The name of the location you want the nodes of.
     * @param definedType The type of the node.
     * @return An ArrayList of the requested nodes.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.Exceptions.HandleExceptions
     */
    public static ArrayList<Node> getAllNodesByType(String locationName, String definedType) throws Exception {
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        ArrayList<Node> nodeList = new ArrayList<>();
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
            for (Node node : network.getNodes()) {
                if (node.getType().equals(definedType)) {
                    nodeList.add(node);
                }
            }
            return nodeList;
        } else {
            throw new BadRequestException(locationName + ".json does not exist.");
        }
    }
}
