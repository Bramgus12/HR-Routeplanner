package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Building;
import com.bramgussekloo.projects.statements.BuildingStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


// Controller for building
@Api(value = "Building controller")
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    // Get all Buildings as a Json list
    @ApiOperation(value = "Get all buildings in a list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = Building.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getAllBuildings() {
        try {
            ArrayList<Building> list = BuildingStatements.getAllBuildings();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain building by id
    @ApiOperation(value = "Get a certain building by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved building", response = Building.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{id}")
    private ResponseEntity getBuilding(@ApiParam(value = "Id of the building you want to retrieve", required = true) @PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.getBuilding(id));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // create a building and give the created building back with an id.
    @ApiOperation(value = "Create a new building")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a new building", response = Building.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createBuilding(@ApiParam(value = "Building that you want to add.", required = true) @RequestBody Building building) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.createBuilding(building));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //Delete an item by id and send the item back.
    @ApiOperation(value = "Delete a building by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted building", response = Building.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity deleteBuilding(@ApiParam(value = "Id of the building that you want to delete") @PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.deleteBuilding(id));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //Update a building and send the updated building back
    @ApiOperation(value = "Update a building")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the building", response = Building.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{id}")
    private ResponseEntity updateBuilding(
            @ApiParam(value = "Id of the building that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "Tha building that you want to update", required = true) @RequestBody Building building
    ) {
        try {
            if (id.equals(building.getId())) {
                return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.updateBuilding(building));
            } else {
                throw new IllegalArgumentException("Id's are different");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Handles exceptions and puts extra information on it.
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
