package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.ProjectsApplication;
import com.bramgussekloo.projects.dataclasses.ElectionCourse;
import com.bramgussekloo.projects.dataclasses.ElectionCourseDescription;
import com.bramgussekloo.projects.statements.ElectionCourseStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

// Controller for xlsx reader
@Api(value = "Election Course list")
@RestController
@RequestMapping("/api/election-course")
public class ElectionCourseController {

    /**
     * Gets all Election Course as an Object and save them into a List
     *
     * @return List of Objects
     */
    @ApiOperation(value = "Get a list of all Election Course from the excel file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = ElectionCourse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping
    private ResponseEntity getElectionCourseList() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.getExcelContent());
        } catch (IOException e) {
            // File not found, throw new msg;
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Add a specific Election Course description
     * Validate for duplicate before adding data.
     *
     * @param electionCourseDescription
     * @return added ElectionCourseDescription object
     */
    @ApiOperation(value = "Add a specific Election Course with its description, use https://www.freeformatter.com/json-escape.html to escape text")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added an election course description", response = ElectionCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    private ResponseEntity createElectionCourseDescription(
            @ApiParam(value = "Add an Election Course description.", required = true) @RequestBody ElectionCourseDescription electionCourseDescription
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.createElectionCourseDescription(electionCourseDescription));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Lookup a specific Election Course for its description
     *
     * @param coursecode
     * @return ElectionCourseDescription object
     */
    @ApiOperation(value = "Lookup a specific Election Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved course description", response = ElectionCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/{coursecode}")
    private ResponseEntity getElectionCourseDescription(
            @ApiParam(value = "Course Code that you want to lookup.", required = true) @PathVariable String coursecode
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.getElectionCourseDescription(coursecode));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Lookup all Election Course with its description
     *
     * @return list of ElectionCourseDescription object
     */
    @ApiOperation(value = "Lookup all Election Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all existing courses with its description", response = ElectionCourseDescription.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping("/description")
    private ResponseEntity getAllElectionCourseDescription() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.getAllElectionCourseDescription());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Update a specific Election Course for its description
     *
     * @param coursecode
     * @return updated ElectionCourseDescription object
     */
    @ApiOperation(value = "Update a specific Election Course for its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated course description", response = ElectionCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping("/{coursecode}")
    private ResponseEntity updateElectionCourseDescription(
            @ApiParam(value = "Course Code that you want to update.", required = true) @PathVariable String coursecode,
            @ApiParam(value = "The Object that you want to update", required = true) @RequestBody ElectionCourseDescription electionCourseDescription) {
        try {
            if (coursecode.equals(electionCourseDescription.getCourseCode())) {
                ElectionCourseDescription oldData = ElectionCourseStatements.getElectionCourseDescription(coursecode);
                ElectionCourseDescription newData = new ElectionCourseDescription();
                newData.setCourseCode(oldData.getCourseCode());
                if (!electionCourseDescription.getDescription().equals(oldData.getDescription())) {
                    newData.setDescription(electionCourseDescription.getDescription());

                } else {
                    newData.setDescription(oldData.getDescription());
                }

                if (!electionCourseDescription.getName().equals(oldData.getName())) {
                    newData.setName(electionCourseDescription.getName());
                } else {
                    newData.setName(oldData.getName());
                }
                return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.updateElectionCourseDescription(newData));
            } else {
                throw new IllegalArgumentException("Election Course Code doesn't exist!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Delete a specific Election Course with its description
     *
     * @param coursecode
     * @return ElectionCourseDescription object of the deleted object
     */
    @ApiOperation(value = "Delete a specific Election Course with its description")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted election course description", response = ElectionCourseDescription.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @DeleteMapping("/{coursecode}")
    private ResponseEntity deleteElectionCourseDescription(
            @ApiParam(value = "Course Code that you want to delete.", required = true) @PathVariable String coursecode
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseStatements.deleteElectionCourseDescription(coursecode));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Update Election Course excel file in Election Course folder by deleting the file first if exist then upload again.
     *
     * @return void
     */
    @ApiOperation(value = "Update Election Course excel file in Election Course folder by deleting the file first if exist then upload again.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully file replaced", response = ElectionCourse.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PutMapping
    private ResponseEntity updateFile(@RequestParam MultipartFile file) {
        try {
            ElectionCourseStatements.updateFile(file);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Upload Excel file in Election Course folder if file doesn't exist.
     *
     * @return void
     */
    @ApiOperation(value = "Upload Excel file in Election Course folder if file doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully uploaded", response = ElectionCourse.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/upload")
    private ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            ElectionCourseStatements.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

