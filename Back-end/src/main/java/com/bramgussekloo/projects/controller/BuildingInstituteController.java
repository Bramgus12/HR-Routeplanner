package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/buildinginstitute")
public class BuildingInstituteController {

    @GetMapping
    private ArrayList<BuildingInstitute> getBuildingInstitutes() {
        ArrayList<BuildingInstitute> list = BuildingInstituteStatements.getAllBuildingInstitutes();
        return list;
    }

    @PostMapping("/{id}")
    private ResponseEntity createBuildingInstitute(@PathVariable Integer id, @RequestBody BuildingInstitute buildingInstitute) {
        if (id.equals(buildingInstitute.getId())) {
            String output = BuildingInstituteStatements.createBuildingInstitute(buildingInstitute);
            if (output.equals("yes")) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different"));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity DeleteBuildingInstitute(@PathVariable Integer id){
        String output = BuildingInstituteStatements.deleteBuildingInstitute(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
    @PutMapping("/{id}")
    private ResponseEntity updateBuildingInstitute(@PathVariable Integer id, @RequestBody BuildingInstitute buildingInstitute) {
        if (id.equals(buildingInstitute.getId())) {
            String output = BuildingInstituteStatements.updateBuildingInstitute(buildingInstitute);
            if (output.equals("yes")) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, "ID's are different"));
        }
    }
}

