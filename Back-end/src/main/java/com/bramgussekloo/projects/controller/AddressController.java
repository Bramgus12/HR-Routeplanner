package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.models.Address;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Api(value = "Address controller")
@RestController
@RequestMapping("/api/")
public class AddressController {

    // Get all the address objects in a list
    @ApiOperation(value = "Get a list of addresses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("address")
    private ResponseEntity<ArrayList<Address>> getAllAddresses() throws Exception {
        return new ResponseEntity<>(Address.getAllFromDatabase(), HttpStatus.OK);
    }

    // Get a certain address object
    @ApiOperation(value = "Get a certain address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully gotten the address"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("address/{id}")
    private ResponseEntity<Address> getAddress(
            @ApiParam(value = "the id of the address you want", required = true) @PathVariable Integer id
    ) throws Exception {
        Address add = new Address();
        add.getFromDatabase(id);
        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    // Create a new address object
    @ApiOperation(value = "Create a new address")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new address in the database"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("admin/address")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Address> createAddress(
            @ApiParam(value = "The Address that you want to add", required = true) @RequestBody Address address
    ) throws Exception {
        address.createInDatabase();
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get an address by Roomcode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("address/room")
    private ResponseEntity<Address> getAddressByRoomCode(
            @ApiParam(
                    value = "The code of the room you want to have the address of", required = true) @RequestParam String code
    ) throws Exception {
        Address add = new Address();
        add.getFromDatabaseByRoomCode(code);
        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    @ApiOperation(value = "Get an address by building name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved address"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("address/building")
    private ResponseEntity<Address> getAddressByBuildingName(
            @ApiParam(value = "The name of a building", required = true) @RequestParam String name
    ) throws Exception {
        Address add = new Address();
        add.getFromDatabaseByBuildingName(name);
        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete an address", response = Address.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the address"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @DeleteMapping("admin/address/{id}")
    private ResponseEntity<Address> deleteAddress(
            @ApiParam(value = "Id for the object you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        Address add = new Address(id);
        add.deleteFromDatabase();
        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated the Address object"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PutMapping("admin/address/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Address> updateAddress(
            @ApiParam(value = "Id of the address that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "The object with the address that you want to update", required = true) @RequestBody Address address
    ) throws Exception {
        if (id.equals(address.getId())) {
            address.updateInDatabase();
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            throw new BadRequestException("ID's are different");
        }
    }
}
