package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Building;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


// Controller for building
@Api(value = "Building controller")
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    // Get all Buildings as a Json list
    @ApiOperation(value = "Get all buildings in a list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity<ArrayList<Building>> getAllBuildings() throws Exception {
        ArrayList<Building> list = Building.getAllBuildingsFromDatabase();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Get a certain building by id
    @ApiOperation(value = "Get a certain building by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved building"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{id}")
    private ResponseEntity<Building> getBuilding(
            @ApiParam(value = "Id of the building you want to retrieve", required = true) @PathVariable Integer id
    ) throws Exception {
        Building building = new Building();
        building.getBuildingFromDatabase(id);
        return new ResponseEntity<>(building, HttpStatus.OK);
    }
}
