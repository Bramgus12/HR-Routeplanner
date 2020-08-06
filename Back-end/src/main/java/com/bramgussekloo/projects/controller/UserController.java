package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.models.User;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ResponseEntity<ArrayList<User>> getAllUsers() throws Exception {
        return new ResponseEntity<>(User.getAllFromDatabase(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully retrieved list"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Invalid credentials")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        user.createInDatabase();
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully updated the User object"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<User> updateUser(
            @ApiParam(value = "Id of the User that you want to update", required = true) @PathVariable Integer id,
            @ApiParam(value = "The object with the User that you want to update", required = true) @RequestBody User user
    ) throws Exception {
        if (id.equals(user.getId())) {
            user.updateInDatabase();
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            throw new BadRequestException("ID's are different");
        }
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteUser(
            @ApiParam(value = "Id for the object you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        User user = new User(id);
        user.deleteInDatabase();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
