package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.ProjectsApplication;
import com.bramgussekloo.projects.dataclasses.BuildingInstitute;
import com.bramgussekloo.projects.statements.BuildingInstituteStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// Makes it a REST-controller
@Api(value = "Building institute controller")
@RestController
@RequestMapping("/api/")
public class BuildingInstituteController {

    // Get all the buildingInstitute objects in a list
    @ApiOperation(value = "Get a list of all the buildingInstitutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = BuildingInstitute.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("buildinginstitute")
    private ResponseEntity<ArrayList<BuildingInstitute>> getAllBuildingInstitutes() {
        try {
            return new ResponseEntity<>(BuildingInstituteStatements.getAllBuildingInstitutes(), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain buildingInstitute object
    @ApiOperation(value = "Get a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved buildingInstitute", response = BuildingInstitute.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("buildinginstitute/{id}")
    private ResponseEntity<BuildingInstitute> getBuildingInstitute(
            @ApiParam(value = "Id of the buildingInstitute you want to get", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(BuildingInstituteStatements.getBuildingInstitute(id), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Create a new BuildingInstitute object
    @ApiOperation(value = "Create a new buildingInstitute")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created new buildingInstitute", response = BuildingInstitute.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/buildinginstitute")
    private ResponseEntity<BuildingInstitute> createBuildingInstitute(
            @ApiParam(value = "buildingInstitute that you want to add", required = true) @RequestBody BuildingInstitute buildingInstitute
    ) {
        try {
            return new ResponseEntity<>(BuildingInstituteStatements.createBuildingInstitute(buildingInstitute), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain buildingInstitute object
    @ApiOperation(value = "Delete a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the buildingInstitute", response = BuildingInstitute.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/buildinginstitute/{id}")
    private ResponseEntity<BuildingInstitute> DeleteBuildingInstitute(
            @ApiParam(value = "Id of the buildingInstitute that you want to delete", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(BuildingInstituteStatements.deleteBuildingInstitute(id), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Update a certain buildingInstitute
    @ApiOperation(value = "Update a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the buildingInstitute", response = BuildingInstitute.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/buildinginstitute/{id}")
    private ResponseEntity<BuildingInstitute> updateBuildingInstitute(
            @ApiParam(value = "Id if the buildingInstitute that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "BuildingInstitute that you want to update", required = true) @RequestBody BuildingInstitute buildingInstitute
    ) {
        try {
            if (id.equals(buildingInstitute.getId())) {
                return new ResponseEntity<>(BuildingInstituteStatements.updateBuildingInstitute(buildingInstitute), HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("Id's are different");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Puts the exceptions into a Spring certified object
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

