package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.bramgussekloo.projects.routeengine.RouteEngine;
import com.bramgussekloo.projects.statements.LocationNodeNetworkStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@Api(value = "RouteEngine Controller")
@RestController
@RequestMapping("/api/routes")
public class RoutesController {

    @ApiOperation(value = "Get the route between two nodes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created route"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity<ArrayList<Node>> getLocationNodeNetwork(
            @ApiParam(value = "The value of the node you want to start at", required = true) @RequestParam Integer from,
            @ApiParam(value = "The value of the node you want to end at", required = true) @RequestParam Integer to,
            @ApiParam(value = "The name of the location you want to be routed in", required = true) @RequestParam String locationName) {
        try {
            RouteEngine routeEngine = new RouteEngine();
            LocationNodeNetwork network = LocationNodeNetworkStatements.getLocationNodeNetwork(locationName);
            routeEngine.init(network);
            return new ResponseEntity<>(routeEngine.generateRoute(from, to), HttpStatus.OK);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
