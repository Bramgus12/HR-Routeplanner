package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BlenderImport;
import com.bramgussekloo.projects.DataClasses.ConnectedNode;
import com.bramgussekloo.projects.DataClasses.Error;
import com.bramgussekloo.projects.DataClasses.Node;
import com.bramgussekloo.projects.Statements.ConnectedNodeStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/blenderimport")
public class BlenderImportController {

    @PostMapping
    private ResponseEntity createNodes(@RequestBody BlenderImport blenderImport){
        ArrayList<ConnectedNode> connectedNodeList = blenderImport.getConnectedNodeList();
        ArrayList<Node> nodeList = blenderImport.getNodeList();
        String locationName = blenderImport.getLocationName();
        String output = "";

        for (int i = 0; i < connectedNodeList.size(); i++) {
            ConnectedNode connectedNode = connectedNodeList.get(i);
            String Error = ConnectedNodeStatements.createConnectedNode(connectedNode);
            if (Error.equals("yes")){
                output = output + "";
            } else {
                output = output + Error;
            }
        }
        if (output.equals("")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }
}
