package com.bramgussekloo.projects.DataClasses;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Error {
    private LocalDateTime localDateTime;
    private Integer errorCode;
    private String error, stackTrace;

    public Error (Integer errorCode, String error){
        this.localDateTime = LocalDateTime.now();
        this.error = error;
        this.errorCode = errorCode;
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
}
