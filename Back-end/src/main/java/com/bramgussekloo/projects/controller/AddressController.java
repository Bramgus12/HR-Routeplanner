package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.AddressStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @GetMapping
    private ArrayList<Address> getAllAddresses(){
        ArrayList<Address> list = AddressStatements.getAllAddresses();
        return list;
    }


    @GetMapping("{id}")
    private Address getAddress(@PathVariable Integer id){
        return AddressStatements.getAddress(id);
    }

    @PostMapping("/{id}")
    private ResponseEntity createAddress(@PathVariable Integer id, @RequestBody Address address) {
        if (id.equals(address.getId())) {
            String output = AddressStatements.createAddress(address);
            if (output.equals("yes")) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different"));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteAddress(@PathVariable Integer id){
        String output = AddressStatements.deleteAddress(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        if (id.equals(address.getId())) {
            String output = AddressStatements.updateAddress(address);
            if (output.equals("yes")) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different"));
        }
    }
}
