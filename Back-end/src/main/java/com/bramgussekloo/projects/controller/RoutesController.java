package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.statements.LocationNodeNetworkStatements;
import com.bramgussekloo.projects.routeengine.RouteEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/routes")
public class RoutesController {

    @GetMapping
    private ResponseEntity getLocationNodeNetwork(@RequestParam Integer from, @RequestParam Integer to, @RequestParam String locationName) {
        try {
            RouteEngine routeEngine = new RouteEngine();
            LocationNodeNetwork network = LocationNodeNetworkStatements.getLocationNodeNetwork(locationName);
            routeEngine.init(network);
            return ResponseEntity.status(HttpStatus.OK).body(routeEngine.generateRoute(from, to));
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Puts the exceptions into a Spring certified object
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
