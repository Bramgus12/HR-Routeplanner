package com.bramgussekloo.projects.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel(description = "All details from Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated id")
    private long id;

    @ApiModelProperty(notes = "Street. You should know what a street is", required = true)
    private String street;

    @ApiModelProperty(notes = "How do you not know why there is a number in an address", required = true)
    private int number;

    @ApiModelProperty(notes = "https://www.youtube.com/watch?v=K1b8AhIsSYQ", required = true)
    private String city;

    @ApiModelProperty(notes = "Postal code that belongs to the address", required = true)
    private String postal;

    @ApiModelProperty(notes = "The addition (dutch = \"toevoeging\") of the address")
    private String addition;

    public Address(long id, String street, int number, String city, String postal, String addition) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal = postal;
        this.addition = addition;
    }

    public Address() {
    }

    public Address(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public long getId() {
        return id;
    }

    public String getAddition() {
        return addition;
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

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

