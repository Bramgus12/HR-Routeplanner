package com.bramgussekloo.projects.dataclasses;

public class Building {
    private Integer id, address_id;
    private String name;

    public Building(Integer id, Integer address_id, String name){
        this.address_id = address_id;
        this.id = id;
        this.name = name;
    }

    public Building(){}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAddress_id() {
        return address_id;
    }
}
