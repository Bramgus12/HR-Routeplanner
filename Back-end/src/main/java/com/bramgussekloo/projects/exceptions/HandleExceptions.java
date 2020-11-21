package com.bramgussekloo.projects.exceptions;

import com.bramgussekloo.projects.ProjectsApplication;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Handles most of the Exceptions and creates a ResponseEntity of it. This will then be sent as a response to the API user.
 */
@ControllerAdvice
public class HandleExceptions {

    @ExceptionHandler(UnauthorizedException.class)
    public static void handleUnauthorizedException(Exception e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getLocalizedMessage());
        response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    private void handleNotFoundExceptions(NotFoundException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getLocalizedMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<Error> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        ProjectsApplication.printErrorInConsole(e.getLocalizedMessage());
        return new ResponseEntity<>(new Error(400, "Bad request", e.getMessage(), request.getServletPath()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InternalServerException.class)
    private void handleInternalServerException(InternalServerException e, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(PSQLException.class)
    private ResponseEntity<Error> handleSQLException(SQLException e, HttpServletRequest request) {
        ProjectsApplication.printErrorInConsole(e.getLocalizedMessage());
        return new ResponseEntity<>(new Error(400, "Bad request", e.getMessage(), request.getServletPath()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    private void handleFileNotFoundExceptions(FileNotFoundException e, HttpServletResponse response) throws IOException {
        ProjectsApplication.printErrorInConsole(e.getLocalizedMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
