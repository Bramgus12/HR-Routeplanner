package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.ElectionCourse;
import com.bramgussekloo.projects.statements.ElectionCourseExcelReaderStatements;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// Controller for xlsx reader
@Api(value = "Keuzevakken lijst")
@RestController
@RequestMapping("/api/kv-lijst")
public class ElectionCourseExcelReaderController {

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
            return ResponseEntity.status(HttpStatus.OK).body(ElectionCourseExcelReaderStatements.getExcelContent());
        } catch (IOException e) {
            // File not found, throw new msg;
            e.printStackTrace();
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
            @ApiResponse(code = 200, message = "Successfully file replaced", response = ElectionCourse.class, responseContainer = "List"),
            @ApiResponse(code = 402, message = "No Content")
    })
    @PutMapping
    private ResponseEntity updateFile(@RequestParam MultipartFile file){
        try {
            ElectionCourseExcelReaderStatements.updateFile(file);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e){
            e.printStackTrace();
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
            @ApiResponse(code = 200, message = "Successfully uploaded", response = ElectionCourse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/upload")
    private ResponseEntity uploadFile (@RequestParam("file") MultipartFile file) {
        try{
            ElectionCourseExcelReaderStatements.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch(IOException e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    // puts the Error in the right format
    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

