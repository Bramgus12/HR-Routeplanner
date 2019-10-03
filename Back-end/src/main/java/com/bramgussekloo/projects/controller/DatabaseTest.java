package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.Database.Address;
import com.bramgussekloo.projects.Database.Statements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/DatabaseTest")
public class DatabaseTest {

    @GetMapping("/test")
    private String testDatabase(){
        ArrayList<Address> list = Statements.getAddressList();
        Address address = list.get(0);
        return ("city: " + address.getCity()+ "\nPostal code: " + address.getPostal() + "\nStreet: " + address.getStreet() + "\nNumber: " + address.getNumber() + "\nId: " + address.getId());
    }
}
