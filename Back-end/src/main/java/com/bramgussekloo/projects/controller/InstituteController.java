package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.Error;
import com.bramgussekloo.projects.models.Institute;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Api(value = "Institute controller")
@RestController
@RequestMapping("/api/")
public class InstituteController {

    @ApiOperation(value = "Get a list of all institutes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @GetMapping("institute")
    private ResponseEntity<ArrayList<Institute>> getInstituteList() throws Exception {
            return new ResponseEntity<>(Institute.getAllInstitutes(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a certain institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved institute object"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
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
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @PostMapping("admin/institute")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Institute> createInstitute(
            @ApiParam(value = "Institute that you want to create.", required = true) @RequestBody Institute institute
    ) throws Exception {
        institute.createInDatabase();
        return new ResponseEntity<>(institute, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Delete an institute by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted institute"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @DeleteMapping("admin/institute/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Void> deleteInstitute(
            @ApiParam(value = "Id of the institute that you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        Institute institute = new Institute(id);
        institute.deleteInstitute();
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update an institute")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated the institute"),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @PutMapping("admin/institute/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<Institute> updateInstitute(
            @ApiParam(value = "The id of the institute you want to update", required = true) @PathVariable Integer id,
            @RequestBody Institute institute
    ) throws Exception {
        if (id.equals(institute.getId())) {
            institute.updateInstitute();
            return new ResponseEntity<>(institute, HttpStatus.CREATED);
        } else {
            throw new BadRequestException("ID's does not match!");
        }
    }
}
