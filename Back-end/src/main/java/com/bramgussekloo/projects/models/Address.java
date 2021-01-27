package com.bramgussekloo.projects.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@ApiModel(description = "All details from Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ApiModelProperty(notes = "The addition (dutch = \"toevoeging\") of the address")
    private String addition;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "address")
    private List<Building> buildings;


    public Address(Integer id, String street, Integer number, String city, String postal, String addition) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postal = postal;
        this.addition = addition;
    }

    public Address() {
    }

    public Address(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getId() {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

