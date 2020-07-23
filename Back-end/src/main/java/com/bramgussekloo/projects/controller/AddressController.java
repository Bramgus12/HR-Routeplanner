package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Address;
import com.bramgussekloo.projects.statements.AddressStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Api(value = "Address controller")
@RestController
@RequestMapping("/api/")
public class AddressController {

    // Get all the address objects in a list
    @ApiOperation(value = "Get a list of addresses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("address")
    private ResponseEntity<ArrayList<Address>> getAllAddresses() {
        try {
            return new ResponseEntity<>(AddressStatements.getAllAddresses(), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain address object
    @ApiOperation(value = "Get a certain address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully gotten the address"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("address/{id}")
    private ResponseEntity<Address> getAddress(@ApiParam(value = "the id of the address you want", required = true) @PathVariable Integer id) {
        try {
            return new ResponseEntity<>(AddressStatements.getAddress(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Create a new address object
    @ApiOperation(value = "Create a new address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a new address in the database"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("admin/address")
    private ResponseEntity<Address> createAddress(
            @ApiParam(value = "The Address that you want to add", required = true) @RequestBody Address address
    ) {
        try {
            Address result = AddressStatements.createAddress(address);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get an address by Roomcode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("address/room")
    private ResponseEntity<Address> getAddressByRoomCode(
            @ApiParam(value = "The code of the room, you want to have the address of", required = true) @RequestParam String code
    ) {
        try {
            return new ResponseEntity<>(AddressStatements.getAddressByRoomCode(code), HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get an address by building name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("address/building")
    private ResponseEntity<Address> getAddressByBuildingName(
            @ApiParam(value = "The name of a building", required = true) @RequestParam String name
    ) {
        try {
            return new ResponseEntity<>(AddressStatements.getAddressByBuildingName(name), HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete an address", response = Address.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the address"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/address/{id}")
    private ResponseEntity<Address> deleteAddress(
            @ApiParam(value = "Id for the object you want to delete", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(AddressStatements.deleteAddress(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the Address object"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/address/{id}")
    private ResponseEntity<Address> updateAddress(
            @ApiParam(value = "Id of the address that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "The object with the address that you want to update", required = true) @RequestBody Address address
    ) {
        try {
            if (id.equals(address.getId())) {
                Address result = AddressStatements.updateAddress(address);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("ID's are different");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
