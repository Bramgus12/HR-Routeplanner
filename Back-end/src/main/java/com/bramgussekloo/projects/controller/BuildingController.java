package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.Error;
import com.bramgussekloo.projects.models.Building;
import com.bramgussekloo.projects.services.BuildingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// Controller for building
@Api(value = "Building controller")
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    private final BuildingService service;

    public BuildingController(BuildingService service) {
        this.service = service;
    }

    // Get all Buildings as a Json list
    @ApiOperation(value = "Get all buildings in a list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @GetMapping
    private ResponseEntity<Iterable<Building>> getAllBuildings() throws Exception {
        return new ResponseEntity<>(service.getAllBuildings(), HttpStatus.OK);
    }

    // Get a certain building by id
    @ApiOperation(value = "Get a certain building by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved building"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @GetMapping("/{id}")
    private ResponseEntity<Building> getBuilding(
            @ApiParam(value = "Id of the building you want to retrieve", required = true) @PathVariable Integer id
    ) {
        return new ResponseEntity<>(service.getBuildingById(id), HttpStatus.OK);
    }
}
