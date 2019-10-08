package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.ConnectedNode;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.Statements.ConnectedNodeStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/connectednode")
public class ConnectedNodeController {

    @GetMapping
    private ArrayList<ConnectedNode> getAllConnectedNode() {
        ArrayList<ConnectedNode> list = ConnectedNodeStatements.getAllConnectedNodes();
        return list;
    }

    @GetMapping("/{id}")
    private ConnectedNode getConnectedNode(@PathVariable Integer id){ return ConnectedNodeStatements.getConnectedNode(id); }

    @PostMapping("/{id}")
    private ResponseEntity createConnectedNode(@PathVariable Integer id, @RequestBody ConnectedNode connectedNode) {
        if (id.equals(connectedNode.getId())) {
            String output = ConnectedNodeStatements.createConnectedNode(connectedNode);
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
    private ResponseEntity deleteConnectedNode(@PathVariable Integer id){
        String output = ConnectedNodeStatements.deleteConnectedNode(id);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity updateConnectedNode(@PathVariable Integer id, @RequestBody ConnectedNode connectedNode) {
        if (id.equals(connectedNode.getId())) {
            String output = ConnectedNodeStatements.updateConnectedNode(connectedNode);
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