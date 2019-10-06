package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/API/BuildingInstitute/")
public class BuildingInstituteController {

    @GetMapping("GET")
    private ArrayList<Building> getBuildingsInstitute() {
        ArrayList<Building> list = BuildingStatements.getAllBuildings();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateBuildingInstitute(@RequestBody Building building) {
        String output = BuildingStatements.createBuilding(building);
        if (output.equals("yes")) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}

