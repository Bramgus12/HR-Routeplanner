package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Address;
import com.bramgussekloo.projects.statements.AddressStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

//Makes it a REST-controller
@Api(value = "Address controller")
@RestController
@RequestMapping("/api/address")
public class AddressController {

    // Get all the address objects in a list
    @ApiOperation(value = "Get a list of addresses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = Address.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getAllAddresses() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAllAddresses());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain address object
    @ApiOperation(value = "Get a certain address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Address.class, message = "Successfully gotten the address"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{id}")
    private ResponseEntity getAddress(@ApiParam(value = "the id of the address you want", required = true) @PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAddress(id));
        } catch (Exception e) {
            throw new IllegalArgumentException("The ID that you are trying to retrieve doesn't exists!");
        }
    }

    // Create a new address object
    @ApiOperation(value = "Create a new address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a new address in the database", response = Address.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createAddress(
            @ApiParam(value = "The Address that you want to add", required = true) @RequestBody Address address
    ) {
        try {
            Address result = AddressStatements.createAddress(address);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get an address by Roomcode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address", response = Address.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/room")
    private ResponseEntity getAddressByRoomCode(
            @ApiParam(value = "The code of the room, you want to have the address of", required = true) @RequestParam String code
    ){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAddressByRoomCode(code));
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get an address by building name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address", response = Address.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/building")
    private ResponseEntity getAddressByBuildingName(
            @ApiParam(value = "The name of a building", required = true) @RequestParam String name
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.getAddressByBuildingName(name));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete an address", response = Address.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the address", response = Address.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity deleteAddress(@ApiParam(value = "Id for the object you want to delete", required = true) @PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AddressStatements.deleteAddress(id));
        } catch (Exception e) {
            throw new IllegalArgumentException("The ID that you are trying to delete doesn't exists!");
        }
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the Address object", response = Address.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{id}")
    private ResponseEntity updateAddress(
            @ApiParam(value = "Id of the address that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "The object with the address that you want to update", required = true) @RequestBody Address address
    ) {
        try {
            if (id.equals(address.getId())) {
                Address result = AddressStatements.updateAddress(address);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                throw new IllegalArgumentException("ID's are different");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("The ID that you are trying to update doesn't exist!");
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
