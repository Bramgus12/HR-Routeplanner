package com.bramgussekloo.projects.Database;

public class Address {
    private Integer id;
    private String street;
    private Integer number;
    private String city;
    private String postal;

    public Address (Integer id, String street, Integer number, String city, String postal){
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal = postal;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getPostal() {
        return postal;
    }

    public String getStreet() {
        return street;
    }
}
