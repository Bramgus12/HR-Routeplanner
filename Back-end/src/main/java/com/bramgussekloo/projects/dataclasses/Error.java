package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "The model of an error")
public class Error {

    @ApiModelProperty(notes = "The time that the error takes place", required = true)
    private LocalDateTime localDateTime;

    @ApiModelProperty(notes = "The HTTP status code", required = true)
    private Integer errorCode;

    @ApiModelProperty(notes = "The Errormessage", required = true)
    private String error;

    @ApiModelProperty(notes = "The stacktrace of the model", required = true)
    private String stackTrace;

    public Error(Integer errorCode, String error, String stackTrace) {
        this.localDateTime = LocalDateTime.now();
        this.error = error;
        this.errorCode = errorCode;
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
