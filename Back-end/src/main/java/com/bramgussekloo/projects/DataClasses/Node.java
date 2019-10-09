package com.bramgussekloo.projects.DataClasses;

import java.math.BigDecimal;

public class Node {
    private Integer id;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal z;
    private String type;
    private Integer number;
    private Integer building_id;
    private String name;

    public Node (Integer id, BigDecimal x, BigDecimal y, BigDecimal z, String type, Integer number, Integer building_id, String name){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.number = number;
        this.building_id = building_id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getZ() {
        return z;
    }

    public String getType() {
        return type;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
