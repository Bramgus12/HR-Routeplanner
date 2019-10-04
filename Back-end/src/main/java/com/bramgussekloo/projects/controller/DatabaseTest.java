package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.Database.Statements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/DatabaseTest")
public class DatabaseTest {

    @GetMapping("/test")
    private Address testDatabase(){
        ArrayList<Address> list = Statements.getAllAddresses();
        Address address = list.get(0);
        return address;
    }

}
