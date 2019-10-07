package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.ConnectedNode;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.ConnectedNodeStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/API/ConnectedNode/")
public class ConnectedNodeController {
    @GetMapping("GET")
    private ArrayList<ConnectedNode> getConnectedNode() {
        ArrayList<ConnectedNode> list = ConnectedNodeStatements.getAllConnectedNodes();
        return list;
    }

    @PostMapping("POST")
    private ResponseEntity CreateConnectedNode(@RequestBody ConnectedNode connectedNode) {
        String output = ConnectedNodeStatements.createConnectedNodes(connectedNode);
        if (output.equals("yes")) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

    @DeleteMapping("DEL")
    private ResponseEntity deleteConnectedNode(@RequestParam Integer id){
        String output = ConnectedNodeStatements.deleteConnectedNode(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}