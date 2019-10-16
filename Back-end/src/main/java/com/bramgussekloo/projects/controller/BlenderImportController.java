package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BlenderImport;
import com.bramgussekloo.projects.Statements.BlenderImportStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.FloatControl;
import java.io.IOException;

@RestController
@RequestMapping("/api/blenderimport")
public class BlenderImportController {

    @GetMapping("/{locationName}")
    private ResponseEntity getBlenderImport(@PathVariable String locationName){
        try {
            BlenderImport blenderImport = BlenderImportStatements.getBlenderImport(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(blenderImport);
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping
    private ResponseEntity createBlenderImport(@RequestBody BlenderImport blenderImport){
        try {
            BlenderImportStatements.createBlenderImport(blenderImport);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @DeleteMapping("/{locationName}")
    private ResponseEntity deleteBlenderImport(@PathVariable String locationName){
        try {
            BlenderImport blenderImport = BlenderImportStatements.deleteBlenderImport(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(blenderImport);
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PutMapping("/{locationName}")
    private ResponseEntity updateBlenderImport(@PathVariable String locationName, @RequestBody BlenderImport blenderImport){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(BlenderImportStatements.updateBlenderImport(locationName, blenderImport));

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
