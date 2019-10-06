package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.ConnectedNode;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.ConnectedNodeStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/API/ConnectedNode/")
public class ConnectedNodeController {
    @GetMapping("GET")
    private ArrayList<ConnectedNode> getBuildingsInstitute() {
        ArrayList<ConnectedNode> list = ConnectedNodeStatements.getAllConnectedNodes();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateBuildingInstitute(@RequestBody ConnectedNode connectedNode) {
        String output = ConnectedNodeStatements.createConnectedNodes(connectedNode);
        if (output.equals("yes")) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}