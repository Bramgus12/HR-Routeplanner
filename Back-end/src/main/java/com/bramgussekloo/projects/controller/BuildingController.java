package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Database.BuildingStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/API/Building/")
public class BuildingController {

    @GetMapping("GET")
    private ArrayList<Building> getBuildings(){
        ArrayList<Building> list = BuildingStatements.getAllBuildings();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateBuilding(@RequestBody Building building){
        String output =  BuildingStatements.createBuilding(building);
        if (output.equals("yes")){
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}
