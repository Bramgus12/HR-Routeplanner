package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.ProjectsApplication;
import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.statements.InstituteStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Api(value = "Institute controller")
@RestController
@RequestMapping("/api/institute")
public class InstituteController {

    /**
     * Gets all Institutes as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all institutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = Institute.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
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
     *
     * @param id
     * @return Institute Object
     */
    @ApiOperation(value = "Get a certain institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved institute object", response = Institute.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{id}")
    private ResponseEntity getInstituteById(
            @ApiParam(value = "Id of the institute", required = true) @PathVariable Integer id
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(InstituteStatements.getInstituteName(id));
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
            @ApiResponse(code = 200, message = "Successfully created institute", response = Institute.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createInstitute(
            @ApiParam(value = "Institute that you want to create, leave id = null", required = true) @RequestBody Institute institute
    ) {
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
    @ApiOperation(value = "Delete an institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted institute", response = Institute.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity deleteInstitute(
            @ApiParam(value = "Id of the institute that you want to delete", required = true) @PathVariable Integer id
    ) {
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
    @ApiOperation(value = "Update an institute")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the institute", response = Institute.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{id}")
    private ResponseEntity updateInstitute(
            @ApiParam(value = "The id of the institute you want to update", required = true) @PathVariable Integer id,
            @RequestBody Institute institute
    ) {
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
        ProjectsApplication.printErrorInConsole(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
