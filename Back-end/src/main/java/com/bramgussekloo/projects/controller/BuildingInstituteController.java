package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/buildinginstitute")
public class BuildingInstituteController {

    @GetMapping
    private ResponseEntity getAllBuildingInstitutes() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getAllBuildingInstitutes());
        } catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity getBuildingInstitute(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getBuildingInstitute(id));
        } catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, e.getMessage()));
        }
    }

    @PostMapping
    private ResponseEntity createBuildingInstitute(@PathVariable Integer id, @RequestBody BuildingInstitute buildingInstitute) {
        try {
            if (id.equals(buildingInstitute.getId())) {
                return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.createBuildingInstitute(buildingInstitute));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different."));
            }
        } catch (SQLException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity DeleteBuildingInstitute(@PathVariable Integer id){
        try {
            BuildingInstituteStatements.deleteBuildingInstitute(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (SQLException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    private ResponseEntity updateBuildingInstitute(@PathVariable Integer id, @RequestBody BuildingInstitute buildingInstitute) {
        if (id.equals(buildingInstitute.getId())) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.updateBuildingInstitute(buildingInstitute));
            } catch (SQLException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, e.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different"));
        }
    }
}

