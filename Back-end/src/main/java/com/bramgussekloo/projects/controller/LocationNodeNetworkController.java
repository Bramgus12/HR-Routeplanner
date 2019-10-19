package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.bramgussekloo.projects.statements.LocationNodeNetworkStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "LocationNodeNetwork Controller")
@RestController
@RequestMapping("/api/locationnodenetwork")
public class LocationNodeNetworkController {

    @ApiOperation(value = "Get a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved LocationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{locationName}")
    private ResponseEntity getLocationNodeNetwork(
            @ApiParam(value = "Name of the location you want to retrieve", required = true) @PathVariable String locationName
    ) {
        try {
            LocationNodeNetwork locationNodeNetwork = LocationNodeNetworkStatements.getLocationNodeNetwork(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(locationNodeNetwork);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Create a new locationNodeNetwork")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created LocationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createLocationNodeNetwork(
            @ApiParam(value = "LocationNodeNetwork you want to add", required = true) @RequestBody LocationNodeNetwork locationNodeNetwork
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.createLocationNodeNetwork(locationNodeNetwork));
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Delete a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted LocationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{locationName}")
    private ResponseEntity deleteLocationNodeNetwork(
            @ApiParam(value = "The name of the location you want to delete", required = true) @PathVariable String locationName
    ) {
        try {
            LocationNodeNetwork locationNodeNetwork = LocationNodeNetworkStatements.deleteLocationNodeNetwork(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(locationNodeNetwork);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get all nodes by type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = Node.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getAllNodesByType(
            @ApiParam(value = "Name of the location where you want to retrieve nodes from", required = true) @RequestParam String locationName,
            @ApiParam(value = "Type of the node where you want to have a list of", required = true) @RequestParam String type
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.getAllNodesByType(locationName, type));
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Update a certain locationNodeNetwork")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated locationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{locationName}")
    private ResponseEntity updateLocationNodeNetwork(
            @ApiParam(value = "Name of the location you want to update", required = true) @PathVariable String locationName,
            @ApiParam(value = "The updated locationNodeNetwork") @RequestBody LocationNodeNetwork locationNodeNetwork
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.updateLocationNodeNetwork(locationName, locationNodeNetwork));
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
