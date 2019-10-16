package com.bramgussekloo.projects.DataClasses;

import java.math.BigDecimal;

public class Node {
    private Integer number;
    private String type;
    private String code;
    private String label;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal z;

    public Node (Integer id, String type, String code, String label, BigDecimal x, BigDecimal y, BigDecimal z){
        this.number = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.code = code;
        this.label = label;
    }

    public Node(){}

    public Integer getNumber() {
        return number;
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

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
