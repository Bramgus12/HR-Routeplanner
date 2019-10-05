package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Database.AddressStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/API/Address/")
public class AddressController {

    @GetMapping("GET")
    private ArrayList<Address> getAddresses(){
        ArrayList<Address> list = AddressStatements.getAllAddresses();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateAddress(@RequestBody Address address){
        String output =  AddressStatements.createAddress(address);
        if (output == "yes"){
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

}
