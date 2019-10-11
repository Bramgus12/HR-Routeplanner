package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @GetMapping
    private ArrayList<Building> getAllBuildings(){
        ArrayList<Building> list = BuildingStatements.getAllBuildings();
        return list;
    }

    @GetMapping("/{id}")
    private Building getBuilding(@PathVariable Integer id){
        return BuildingStatements.getBuilding(id);
    }

    @PostMapping("/{id}")
    private ResponseEntity createBuilding(@PathVariable Integer id, @RequestBody Building building) {
        if (id.equals(building.getId())) {
            String output = BuildingStatements.createBuilding(building);
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
    private ResponseEntity deleteBuilding(@PathVariable Integer id){
        String output = BuildingStatements.deleteBuilding(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
    @PutMapping("/{id}")
    private ResponseEntity updateAddress(@PathVariable Integer id, @RequestBody Building building) {
        if (id.equals(building.getId())) {
            String output = BuildingStatements.updateBuilding(building);
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
