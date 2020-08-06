package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.models.BuildingInstitute;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

// Makes it a REST-controller
@Api(value = "Building institute controller")
@RestController
@RequestMapping("/api/")
public class BuildingInstituteController {

    // Get all the buildingInstitute objects in a list
    @ApiOperation(value = "Get a list of all the buildingInstitutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("buildinginstitute")
    private ResponseEntity<ArrayList<BuildingInstitute>> getAllBuildingInstitutes() throws Exception {
        return new ResponseEntity<>(BuildingInstitute.getAllFromDatabase(), HttpStatus.OK);
    }

    // Get a certain buildingInstitute object
    @ApiOperation(value = "Get a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("buildinginstitute/{id}")
    private ResponseEntity<BuildingInstitute> getBuildingInstitute(
            @ApiParam(value = "Id of the buildingInstitute you want to get", required = true) @PathVariable Integer id
    ) throws Exception {
        BuildingInstitute bi = new BuildingInstitute();
        bi.getFromDatabase(id);
        return new ResponseEntity<>(bi, HttpStatus.OK);
    }

    // Create a new BuildingInstitute object
    @ApiOperation(value = "Create a new buildingInstitute")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/buildinginstitute")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<BuildingInstitute> createBuildingInstitute(
            @ApiParam(value = "buildingInstitute that you want to add", required = true) @RequestBody BuildingInstitute buildingInstitute
    ) throws Exception {
        buildingInstitute.createInDatabase();
        return new ResponseEntity<>(buildingInstitute, HttpStatus.CREATED);
    }

    // Delete a certain buildingInstitute object
    @ApiOperation(value = "Delete a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/buildinginstitute/{id}")
    private ResponseEntity<BuildingInstitute> DeleteBuildingInstitute(
            @ApiParam(value = "Id of the buildingInstitute that you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        BuildingInstitute bi = new BuildingInstitute(id);
        bi.deleteInDatabase();
        return new ResponseEntity<>(bi, HttpStatus.OK);
    }

    // Update a certain buildingInstitute
    @ApiOperation(value = "Update a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated the buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/buildinginstitute/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<BuildingInstitute> updateBuildingInstitute(
            @ApiParam(value = "Id if the buildingInstitute that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "BuildingInstitute that you want to update", required = true) @RequestBody BuildingInstitute buildingInstitute
    ) throws Exception {
        if (id.equals(buildingInstitute.getId())) {
            buildingInstitute.updateInDatabase();
            return new ResponseEntity<>(buildingInstitute, HttpStatus.CREATED);
        } else {
            throw new BadRequestException("Id's are different");
        }
    }
}

