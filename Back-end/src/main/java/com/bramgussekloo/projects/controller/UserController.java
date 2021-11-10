package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.Error;
import com.bramgussekloo.projects.models.User;
import com.bramgussekloo.projects.services.UserService;

import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Address controller")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "Get a list of users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Invalid credentials", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @GetMapping
    private ResponseEntity<Iterable<User>> getAllUsers() throws Exception {
        Iterable<User> response = service.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new User")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully retrieved list"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Invalid credentials", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        User response = service.create(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update a certain object
    @ApiOperation(value = "Update an Address object")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully updated the User object"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<User> updateUser(
        @ApiParam(value = "Id of the User that you want to update", required = true) @PathVariable Integer id,
        @ApiParam(value = "The object with the User that you want to update", required = true) @RequestBody User user
    ) throws Exception {
        if (id.equals(user.getId())) {
            service.update(user, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new BadRequestException("ID's are different");
        }
    }

    // Delete a certain address object.
    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the user"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Bad credentials", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Void> deleteUser(
        @ApiParam(value = "Id for  the object you want to delete", required = true) @PathVariable Integer id
    ) throws Exception {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
