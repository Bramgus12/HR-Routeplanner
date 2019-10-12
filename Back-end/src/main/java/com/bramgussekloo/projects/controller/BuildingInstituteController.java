package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/buildinginstitute")
public class BuildingInstituteController {

    @GetMapping
    private ResponseEntity getAllBuildingInstitutes() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getAllBuildingInstitutes());
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity getBuildingInstitute(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.getBuildingInstitute(id));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping
    private ResponseEntity createBuildingInstitute(@RequestBody BuildingInstitute buildingInstitute) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingInstituteStatements.createBuildingInstitute(buildingInstitute));
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity DeleteBuildingInstitute(@PathVariable Integer id){
        try {
            BuildingInstituteStatements.deleteBuildingInstitute(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
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

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

