package com.bramgussekloo.projects.DataClasses;

public class Error {
    private Integer errorCode;
    private String error;

    public Error (Integer errorCode, String error){
        this.error = error;
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }
}
