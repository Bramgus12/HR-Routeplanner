package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
}
