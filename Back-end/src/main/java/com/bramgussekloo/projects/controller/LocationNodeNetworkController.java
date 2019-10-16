package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.LocationNodeNetwork;
import com.bramgussekloo.projects.Statements.LocationNodeNetworkStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/locationnodenetwork")
public class LocationNodeNetworkController {

    @GetMapping("/{locationName}")
    private ResponseEntity getLocationNodeNetwork(@PathVariable String locationName){
        try {
            LocationNodeNetwork locationNodeNetwork = LocationNodeNetworkStatements.getLocationNodeNetwork(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(locationNodeNetwork);
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping
    private ResponseEntity createLocationNodeNetwork(@RequestBody LocationNodeNetwork locationNodeNetwork){
        try {
            LocationNodeNetworkStatements.createLocationNodeNetwork(locationNodeNetwork);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @DeleteMapping("/{locationName}")
    private ResponseEntity deleteLocationNodeNetwork(@PathVariable String locationName){
        try {
            LocationNodeNetwork locationNodeNetwork = LocationNodeNetworkStatements.deleteLocationNodeNetwork(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(locationNodeNetwork);
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PutMapping("/{locationName}")
    private ResponseEntity updateLocationNodeNetwork(@PathVariable String locationName, @RequestBody LocationNodeNetwork locationNodeNetwork){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(LocationNodeNetworkStatements.updateLocationNodeNetwork(locationName, locationNodeNetwork));

        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
