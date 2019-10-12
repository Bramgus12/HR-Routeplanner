package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Building;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.BuildingStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @GetMapping
    private ResponseEntity getAllBuildings(){
        try{
            ArrayList<Building> list = BuildingStatements.getAllBuildings();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity getBuilding(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.getBuilding(id));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping
    private ResponseEntity createBuilding( @RequestBody Building building) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.createBuilding(building));
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteBuilding(@PathVariable Integer id){
         try {
             BuildingStatements.deleteBuilding(id);
             return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
         } catch (SQLException e){
             e.printStackTrace();
             throw new IllegalArgumentException(e.getMessage());
         }
    }

    @PutMapping("/{id}")
    private ResponseEntity updateAddress(@PathVariable Integer id, @RequestBody Building building) {
        try {
            if (id.equals(building.getId())) {
                return ResponseEntity.status(HttpStatus.OK).body(BuildingStatements.updateBuilding(building));
            } else {
                throw new IllegalArgumentException("Id's are different");
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }


}
