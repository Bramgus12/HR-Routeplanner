package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

// Makes it a REST-controller
@RestController
@RequestMapping("/api/buildinginstitute")
public class BuildingInstituteController {

    // Get all the buildingInstitute objects in a list
    @GetMapping
    private ResponseEntity getAllBuildingInstitutes() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getAllBuildingInstitutes());
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get a certain buildingInstitute object
    @GetMapping("/{id}")
    private ResponseEntity getBuildingInstitute(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getBuildingInstitute(id));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Create a new BuildingInstitute object
    @PostMapping
    private ResponseEntity createBuildingInstitute(@RequestBody BuildingInstitute buildingInstitute) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.createBuildingInstitute(buildingInstitute));
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain buildingInstitute object
    @DeleteMapping("/{id}")
    private ResponseEntity DeleteBuildingInstitute(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.deleteBuildingInstitute(id));
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Update a certain buildingInstitute
    @PutMapping("/{id}")
    private ResponseEntity updateBuildingInstitute(@PathVariable Integer id, @RequestBody BuildingInstitute buildingInstitute) {
        if (id.equals(buildingInstitute.getId())) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.updateBuildingInstitute(buildingInstitute));
            } catch (SQLException e){
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Id's are different");
        }
    }

    // Puts the exceptions into a Spring certified object
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

