package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.AddressStatements;
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
    private ResponseEntity createAddress(@RequestBody Address address){
        String output =  AddressStatements.createAddress(address);
        if (output.equals("yes")){
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

    @DeleteMapping("DEL")
    private ResponseEntity deleteAddress(@RequestParam Integer id){
        String output = AddressStatements.deleteAddress(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

}
