package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.ProjectsApplication;
import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.bramgussekloo.projects.dataclasses.NodesAndBuildingName;
import com.bramgussekloo.projects.statements.LocationNodeNetworkStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "LocationNodeNetwork Controller")
@RestController
@RequestMapping("/api/")
public class LocationNodeNetworkController {

    @ApiOperation(value = "Get a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved LocationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    @GetMapping("locationnodenetwork/{locationName}")
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
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/locationnodenetwork/{addressId}")
    private ResponseEntity createLocationNodeNetwork(
            @ApiParam(value = "LocationNodeNetwork you want to add", required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "Address that corresponds with the locationNodeNetwork", required = true) @PathVariable Integer addressId
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.createLocationNodeNetwork(file, addressId));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Delete a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted LocationNodeNetwork", response = LocationNodeNetwork.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/locationnodenetwork/{locationName}")
    private ResponseEntity deleteLocationNodeNetwork(
            @ApiParam(value = "The name of the location you want to delete", required = true) @PathVariable String locationName
    ) {
        try {
            LocationNodeNetwork locationNodeNetwork = LocationNodeNetworkStatements.deleteLocationNodeNetwork(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(locationNodeNetwork);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get all nodes by type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = Node.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("locationnodenetwork")
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
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/locationnodenetwork")
    private ResponseEntity updateLocationNodeNetwork(
            @ApiParam(value = "Name of the location you want to update", required = true) @RequestParam String locationName,
            @ApiParam(value = "The ID of the address that you want to pair this locationNodeNetwork with", required = true) @RequestParam Integer addressId,
            @ApiParam(value = "The updated locationNodeNetwork", required = true) @RequestParam MultipartFile file
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.updateLocationNodeNetwork(locationName, file, addressId));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Get all nodes that are a room")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of nodes with the buildingName", response = NodesAndBuildingName.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    @GetMapping("locationnodenetwork/room")
    private ResponseEntity getAllRooms() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.getAllRooms());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
