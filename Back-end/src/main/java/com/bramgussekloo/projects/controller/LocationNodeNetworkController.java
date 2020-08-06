package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.models.LocationNodeNetwork;
import com.bramgussekloo.projects.models.Node;
import com.bramgussekloo.projects.models.NodesAndBuildingName;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Api(value = "LocationNodeNetwork Controller")
@RestController
@RequestMapping("/api/")
public class LocationNodeNetworkController {

    @ApiOperation(value = "Get a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved LocationNodeNetwork"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    @GetMapping("locationnodenetwork/{locationName}")
    private ResponseEntity<LocationNodeNetwork> getLocationNodeNetwork(
            @ApiParam(value = "Name of the location you want to retrieve", required = true) @PathVariable String locationName
    ) throws Exception {
        LocationNodeNetwork locationNodeNetwork = new LocationNodeNetwork();
        locationNodeNetwork.getLocationNodeNetwork(locationName);
        return new ResponseEntity<>(locationNodeNetwork, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new locationNodeNetwork")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created LocationNodeNetwork"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/locationnodenetwork/{addressId}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<LocationNodeNetwork> createLocationNodeNetwork(
            @ApiParam(value = "LocationNodeNetwork you want to add", required = true) @RequestPart MultipartFile file,
            @ApiParam(value = "Address that corresponds with the locationNodeNetwork", required = true) @PathVariable Integer addressId
    ) throws Exception {
        LocationNodeNetwork locationNodeNetwork = new LocationNodeNetwork();
        locationNodeNetwork.createLocationNodeNetwork(file, addressId);
        return new ResponseEntity<>(locationNodeNetwork, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete a certain LocationNodeNetwork by locationName")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted LocationNodeNetwork"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/locationnodenetwork/{locationName}")
    private ResponseEntity<Void> deleteLocationNodeNetwork(
            @ApiParam(value = "The name of the location you want to delete", required = true) @PathVariable String locationName
    ) throws Exception {
        LocationNodeNetwork locationNodeNetwork = new LocationNodeNetwork(locationName);
        locationNodeNetwork.deleteLocationNodeNetwork();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get all nodes by type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("locationnodenetwork")
    private ResponseEntity<ArrayList<Node>> getAllNodesByType(
            @ApiParam(value = "Name of the location where you want to retrieve nodes from", required = true) @RequestParam String locationName,
            @ApiParam(value = "Type of the node where you want to have a list of", required = true) @RequestParam String type
    ) throws Exception {
        return new ResponseEntity<>(Node.getAllNodesByType(locationName, type), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a certain locationNodeNetwork")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated locationNodeNetwork"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/locationnodenetwork")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<LocationNodeNetwork> updateLocationNodeNetwork(
            @ApiParam(value = "Name of the location you want to update", required = true) @RequestParam String locationName,
            @ApiParam(value = "The ID of the address that you want to pair this locationNodeNetwork with", required = true) @RequestParam Integer addressId,
            @ApiParam(value = "The updated locationNodeNetwork", required = true) @RequestPart MultipartFile file
    ) throws Exception {
        LocationNodeNetwork locationNodeNetwork = new LocationNodeNetwork(locationName);
        locationNodeNetwork.updateLocationNodeNetwork(file, addressId);
        return new ResponseEntity<>(locationNodeNetwork, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all nodes that are a room")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of nodes with the buildingName"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    @GetMapping("locationnodenetwork/room")
    private ResponseEntity<ArrayList<NodesAndBuildingName>> getAllRooms() throws Exception {
        return new ResponseEntity<>(NodesAndBuildingName.getAllRooms(), HttpStatus.OK);
    }
}
