package com.bramgussekloo.projects.dataclasses;

public class Node {
    private Integer number;
    private String type;
    private String code;
    private String label;
    private double x;
    private double y;
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
