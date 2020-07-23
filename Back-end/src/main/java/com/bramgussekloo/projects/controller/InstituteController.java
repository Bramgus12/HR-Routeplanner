package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.statements.InstituteStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Api(value = "Institute controller")
@RestController
@RequestMapping("/api/")
public class InstituteController {

    /**
     * Gets all Institutes as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all institutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("institute")
    private ResponseEntity<ArrayList<Institute>> getInstituteList() {
        try {
            return new ResponseEntity<>(InstituteStatements.getAllInstitutes(), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Get the Institute name from the Database
     *
     * @param id
     * @return Institute Object
     */
    @ApiOperation(value = "Get a certain institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved institute object"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("institute/{id}")
    private ResponseEntity<Institute> getInstituteById(
            @ApiParam(value = "Id of the institute", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(InstituteStatements.getInstituteName(id), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Creates a new Institute Object and add its value to the Database
     *
     * @param institute
     * @return HttpStatus
     */
    @ApiOperation(value = "Create a new institute")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/institute")
    private ResponseEntity<Institute> createInstitute(
            @ApiParam(value = "Institute that you want to create, leave id = null", required = true) @RequestBody Institute institute
    ) {
        try {
            Institute result = InstituteStatements.createInstitute(institute);
            return new ResponseEntity<>(result, HttpStatus.OK);
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
    @ApiOperation(value = "Delete an institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/institute/{id}")
    private ResponseEntity<Institute> deleteInstitute(
            @ApiParam(value = "Id of the institute that you want to delete", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(InstituteStatements.deleteInstitute(id), HttpStatus.OK);
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
    @ApiOperation(value = "Update an institute")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/institute/{id}")
    private ResponseEntity<Institute> updateInstitute(
            @ApiParam(value = "The id of the institute you want to update", required = true) @PathVariable Integer id,
            @RequestBody Institute institute
    ) {
        try {
            if (id.equals(institute.getId())) {
                return new ResponseEntity<>(InstituteStatements.updateInstitute(institute), HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("ID's does not match!");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
