package com.bramgussekloo.projects.DataClasses;

public class Node {
    private Integer id;
    private Integer x;
    private Integer y;
    private Integer z;
    private String type;

    public Node (Integer id, Integer x, Integer y, Integer z, String type){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public String getType() {
        return type;
    }
}
