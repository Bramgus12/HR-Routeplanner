package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Error;
import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.statements.InstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/institute")
public class InstituteController {

    @GetMapping
    private ArrayList<Institute> getInstituteList(){
        ArrayList<Institute> list = InstituteStatements.getAllInstitutes();
        return list;
    }

    @PostMapping
    private ResponseEntity create(@RequestBody Institute institute){
        String output = InstituteStatements.createInstitute(institute);
        if (output.equals("yes")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(400, output));
        }
    }

}
