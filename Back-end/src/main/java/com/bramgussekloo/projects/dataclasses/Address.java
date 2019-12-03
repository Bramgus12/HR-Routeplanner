package com.bramgussekloo.projects.dataclasses;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details from Address")
public class Address {
    @ApiModelProperty(notes = "The database generated id")
    private Integer id;

    @ApiModelProperty(notes = "Street. You should know what a street is", required = true)
    private String street;

    @ApiModelProperty(notes = "How do you not know why there is a number in an address", required = true)
    private Integer number;

    @ApiModelProperty(notes = "https://www.youtube.com/watch?v=K1b8AhIsSYQ", required = true)
    private String city;

    @ApiModelProperty(notes = "Postal code that belongs to the address", required = true)
    private String postal;

    public Address(Integer id, String street, Integer number, String city, String postal) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal = postal;
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

