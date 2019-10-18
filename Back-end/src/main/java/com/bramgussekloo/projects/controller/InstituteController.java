package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Error;
import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.statements.InstituteStatements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/institute")
public class InstituteController {

    /**
     * Gets all Institutes as an Object and save them into a List
     *
     * @return List of Objects
     */
    @GetMapping
    private ResponseEntity getInstituteList() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(InstituteStatements.getAllInstitutes());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Get the Institute name from the Database
     * @param id
     * @return Institute Object
     */
    @GetMapping("/{id}")
    private ResponseEntity getInstituteById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(InstituteStatements.getInstituteName(id));
        } catch (SQLException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Creates a new Institute Object and add its value to the Database
     *
     * @param institute
     * @return HttpStatus
     */
    @PostMapping
    private ResponseEntity createInstitute(@RequestBody Institute institute) {
        try {
            Institute result = InstituteStatements.createInstitute(institute);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Deletes an entry from the Database.
     *
     * @param id
     * @return HttpStatus
     */
    @DeleteMapping("/{id}")
    private ResponseEntity deleteInstitute(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(InstituteStatements.deleteInstitute(id));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Updates an entry in the Database
     *
     * @param id
     * @param institute
     * @return updated Database entry
     */
    @PutMapping("/{id}")
    private ResponseEntity updateInstitute(@PathVariable Integer id, @RequestBody Institute institute) {
        try {
            if (id.equals(institute.getId())) {
                return ResponseEntity.status(HttpStatus.OK).body(InstituteStatements.updateInstitute(institute));
            } else {
                throw new IllegalArgumentException("ID's does not match!");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * puts the Error in the right format
     *
     * @param e
     * @param response
     * @throws IOException
     */
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
