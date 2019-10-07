package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/API/BuildingInstitute/")
public class BuildingInstituteController {

    @GetMapping("GET")
    private ArrayList<BuildingInstitute> getBuildingsInstitute() {
        ArrayList<BuildingInstitute> list = BuildingInstituteStatements.getAllABuildingInstitutes();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateBuildingInstitute(@RequestBody BuildingInstitute buildingInstitute) {
        String output = BuildingInstituteStatements.createBuildingInstitute(buildingInstitute);
        if (output.equals("yes")) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
    @DeleteMapping("DEL")
    private ResponseEntity DeleteBuildingInstitute(@RequestParam Integer id){
        String output = BuildingInstituteStatements.deleteBuildingInstitute(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}

