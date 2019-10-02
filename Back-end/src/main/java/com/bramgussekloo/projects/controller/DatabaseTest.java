package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.Database.DatabaseConnection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/DatabaseTest")
public class DatabaseTest {

    @GetMapping("/test")
    private String testDatabase(){
        DatabaseConnection dbc = new DatabaseConnection("jbdc:postgresql://projects.bramgussekloo.com/Test:5432", "postgres", "onjuist11");
        dbc.getConnection();
        System.out.println("table created");
        return "table created";
    }
}
