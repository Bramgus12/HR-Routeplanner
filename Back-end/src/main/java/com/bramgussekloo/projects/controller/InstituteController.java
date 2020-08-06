package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.Exceptions.BadRequestException;
import com.bramgussekloo.projects.dataclasses.Institute;
import com.bramgussekloo.projects.statements.InstituteStatements;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Api(value = "Institute controller")
@RestController
@RequestMapping("/api/")
public class InstituteController {

    @ApiOperation(value = "Get a list of all institutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("institute")
    private ResponseEntity<ArrayList<Institute>> getInstituteList() throws Exception {
            return new ResponseEntity<>(Institute.getAllInstitutes(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a certain institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved institute object"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("institute/{id}")
    private ResponseEntity<Institute> getInstituteById(
            @ApiParam(value = "Id of the institute", required = true) @PathVariable Integer id
    ) throws Exception {
        Institute institute = new Institute();
        institute.getNameFromDatabase(id);
        return new ResponseEntity<>(institute, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new institute")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/institute")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Institute> createInstitute(
            @ApiParam(value = "Institute that you want to create.", required = true) @RequestBody Institute institute
    ) throws Exception {
        institute.createInDatabase();
        return new ResponseEntity<>(institute, HttpStatus.OK);
    }


    @ApiOperation(value = "Delete an institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/institute/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Institute> deleteInstitute(
            @ApiParam(value = "Id of the institute that you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        Institute institute = new Institute(id);
        institute.deleteInstitute();
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update an institute")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated the institute"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/institute/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Institute> updateInstitute(
            @ApiParam(value = "The id of the institute you want to update", required = true) @PathVariable Integer id,
            @RequestBody Institute institute
    ) throws Exception {
        if (id.equals(institute.getId())) {
            institute.updateInstitute();
            return new ResponseEntity<>(institute, HttpStatus.OK);
        } else {
            throw new BadRequestException("ID's does not match!");
        }
    }
}
