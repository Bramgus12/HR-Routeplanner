package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.AddressStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.PastOrPresent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


//Makes it a REST-controller
@RestController
@RequestMapping("/api/address")
public class AddressController {

    // Get all the address objects in a list
    @GetMapping
    private ResponseEntity getAllAddresses(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAllAddresses());
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain address object
    @GetMapping("/{id}")
    private ResponseEntity getAddress(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAddress(id));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Create a new address object
    @PostMapping
    private ResponseEntity createAddress(@RequestBody Address address) {
        try{
            Address result = AddressStatements.createAddress(address);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain address object.
    @DeleteMapping("/{id}")
    private ResponseEntity deleteAddress(@PathVariable Integer id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.deleteAddress(id));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Update a certain object
    @PutMapping("/{id}")
    private ResponseEntity updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        try{
            if (id.equals(address.getId())) {
                Address result = AddressStatements.updateAddress(address);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                throw new IllegalArgumentException("ID's are different");
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
