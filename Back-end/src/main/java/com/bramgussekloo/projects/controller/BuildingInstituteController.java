package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.Error;
import com.bramgussekloo.projects.models.Building;
import com.bramgussekloo.projects.models.BuildingInstitute;
import com.bramgussekloo.projects.models.Institute;
import com.bramgussekloo.projects.services.BuildingInstituteService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

// Makes it a REST-controller
@Api(value = "Building institute controller")
@RestController
@RequestMapping("/api/")
public class BuildingInstituteController {

    BuildingInstituteService buildingInstituteService;

    public BuildingInstituteController(BuildingInstituteService buildingInstituteService) {
        this.buildingInstituteService = buildingInstituteService;
    }

    // Create a new BuildingInstitute object
    @ApiOperation(value = "Link building to institute")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully created new buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @PostMapping("admin/buildinginstitute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Void> createBuildingInstitute(
            @ApiParam(value = "buildingInstitute that you want to add", required = true) @RequestBody Integer buildingId, Integer instituteId
    ) throws Exception {
        buildingInstituteService.linkBuildingInstitute(buildingId, instituteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete a certain buildingInstitute object
    @ApiOperation(value = "Delete a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @DeleteMapping("admin/buildinginstitute/{buildingId}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<Set<Institute>> DeleteBuildingFromInstitutes(
            @ApiParam(value = "Id of the buildingInstitute that you want to delete", required = true) @PathVariable Integer buildingId
    ) {
        return new ResponseEntity<>(buildingInstituteService.deleteBuildingFromInstitutes(buildingId), HttpStatus.OK);
    }

    // Delete a certain buildingInstitute object
    @ApiOperation(value = "Delete a certain buildingInstitute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the buildingInstitute"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @DeleteMapping("admin/buildinginstitute/{instituteId}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<Set<Building>> DeleteInstituteFromBuildings(
            @ApiParam(value = "Id of the buildingInstitute that you want to delete", required = true) @PathVariable Integer instituteId
    ) {
        return new ResponseEntity<>(buildingInstituteService.deleteInstituteFromBuildings(instituteId), HttpStatus.OK);
    }
}

