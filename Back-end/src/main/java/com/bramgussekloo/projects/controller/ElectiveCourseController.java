package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.Exceptions.BadRequestException;
import com.bramgussekloo.projects.dataclasses.ElectiveCourse;
import com.bramgussekloo.projects.dataclasses.ElectiveCourseDescription;
import com.bramgussekloo.projects.statements.ElectiveCourseStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// Controller for xlsx reader
@Api(value = "Elective Course list")
@RestController
@RequestMapping("/api/")
public class ElectiveCourseController {

    /**
     * Gets all Elective Course as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all Elective Course from the excel file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("elective-course")
    private ResponseEntity<ArrayList<ElectiveCourse>> getElectiveCourseList() throws Exception {
        return new ResponseEntity<>(ElectiveCourse.getExcelContent(), HttpStatus.OK);
    }

    /**
     * Add a specific Elective Course description
     * Validate for duplicate before adding data.
     *
     * @param electiveCourseDescription
     * @return added ElectiveCourseDescription object
     */
    @ApiOperation(value = "Add a specific Elective Course with its description, use https://www.freeformatter.com/json-escape.html to escape text")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added an elective course description"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/elective-course")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<ElectiveCourseDescription> createElectiveCourseDescription(
            @ApiParam(value = "Add an Elective Course description.", required = true) @RequestBody ElectiveCourseDescription electiveCourseDescription
    ) throws Exception {
        electiveCourseDescription.createInDatabase();
        return new ResponseEntity<>(electiveCourseDescription, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Lookup a specific Elective Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved course description"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("elective-course/{coursecode}")
    private ResponseEntity<ElectiveCourseDescription> getElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to lookup.", required = true) @PathVariable String coursecode
    ) throws Exception {
        ElectiveCourseDescription electiveCourseDescription = new ElectiveCourseDescription();
        electiveCourseDescription.getFromDatabase(coursecode);
        return new ResponseEntity<>(electiveCourseDescription, HttpStatus.OK);
    }

    @ApiOperation(value = "Lookup all Elective Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all existing courses with its description"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("elective-course/description")
    private ResponseEntity<ArrayList<ElectiveCourseDescription>> getAllElectiveCourseDescription() throws Exception {
        return new ResponseEntity<>(ElectiveCourseDescription.getAllFromDatabase(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a specific Elective Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated course description"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/elective-course/{coursecode}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<ElectiveCourseDescription> updateElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to update.", required = true) @PathVariable String coursecode,
            @ApiParam(value = "The Object that you want to update", required = true) @RequestBody ElectiveCourseDescription electiveCourseDescription
    ) throws Exception {
        if (coursecode.equals(electiveCourseDescription.getCourseCode())) {
            ElectiveCourseDescription oldData = new ElectiveCourseDescription();
            oldData.getFromDatabase(coursecode);
            ElectiveCourseDescription newData = new ElectiveCourseDescription();
            newData.setCourseCode(oldData.getCourseCode());
            if (!electiveCourseDescription.getDescription().equals(oldData.getDescription())) {
                newData.setDescription(electiveCourseDescription.getDescription());

            } else {
                newData.setDescription(oldData.getDescription());
            }

            if (!electiveCourseDescription.getName().equals(oldData.getName())) {
                newData.setName(electiveCourseDescription.getName());
            } else {
                newData.setName(oldData.getName());
            }

            newData.updateInDatabase();
            return new ResponseEntity<>(newData, HttpStatus.CREATED);
        } else {
            throw new BadRequestException("The Elective Coursecode that you've provided doesn't match.");
        }
    }

    @ApiOperation(value = "Delete a specific Elective Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted elective course description"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/elective-course/{coursecode}")
    private ResponseEntity<ElectiveCourseDescription> deleteElectiveCourseDescription(
            @ApiParam(value = "Course Code that you want to delete.", required = true) @PathVariable String coursecode
    ) throws Exception {
        ElectiveCourseDescription ecd = new ElectiveCourseDescription(coursecode);
        ecd.deleteInDatabase();
        return new ResponseEntity<>(ecd, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete the elective-course file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted elective course description"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("admin/elective-course")
    private ResponseEntity<ArrayList<ElectiveCourse>> deleteFile() throws Exception {
        return new ResponseEntity<>(ElectiveCourse.deleteFile(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Elective Course excel file in Elective Course folder by deleting the file first if exist then upload again.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully file replaced"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("admin/elective-course")
    private ResponseEntity<ArrayList<ElectiveCourse>> updateFile(@RequestPart MultipartFile file) throws Exception {
        ArrayList<ElectiveCourse> electiveCourses = ElectiveCourse.updateFile(file);
        return new ResponseEntity<>(electiveCourses, HttpStatus.OK);
    }

    @ApiOperation(value = "Upload Excel file in Elective Course folder if file doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PostMapping("admin/elective-course/upload")
    private ResponseEntity<ArrayList<ElectiveCourse>> uploadFile(@RequestPart MultipartFile file) throws Exception {
        ArrayList<ElectiveCourse> electiveCourses = ElectiveCourse.updateFile(file);
        return new ResponseEntity<>(electiveCourses, HttpStatus.OK);
    }
}

