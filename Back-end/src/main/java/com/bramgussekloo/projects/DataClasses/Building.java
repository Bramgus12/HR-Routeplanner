package com.bramgussekloo.projects.DataClasses;

public class Building {
    private Integer id, addressId;
    private String name;

    public Building(Integer id, Integer addressId, String name){
        this.addressId = addressId;
        this.id = id;
        this.name = name;
    }
    

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAddressId() {
        return addressId;
    }
}
