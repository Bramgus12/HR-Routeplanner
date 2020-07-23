package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.dataclasses.User;
import com.bramgussekloo.projects.statements.UserStatements;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Api(value = "Address controller")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @ApiOperation(value = "Get a list of users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Invalid credentials")
    })
    @GetMapping
    private ResponseEntity<ArrayList<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(UserStatements.getAllUsers(), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @ApiOperation(value = "Create a new User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Invalid credentials")
    })
    @PostMapping
    private ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(UserStatements.createUser(user), HttpStatus.OK);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the User object"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("/{id}")
    private ResponseEntity<User> updateUser(
            @ApiParam(value = "Id of the User that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "The object with the User that you want to update", required = true) @RequestBody User user
    ) {
        try {
            if (id.equals(user.getId())) {
                User result = UserStatements.updateUser(user);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("ID's are different");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<User> deleteUser(
            @ApiParam(value = "Id for the object you want to delete", required = true) @PathVariable Integer id
    ) {
        try {
            return new ResponseEntity<>(UserStatements.deleteUser(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
