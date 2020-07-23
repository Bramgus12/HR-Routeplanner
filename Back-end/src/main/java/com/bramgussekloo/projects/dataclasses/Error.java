package com.bramgussekloo.projects.dataclasses;

import java.util.Date;

public class Error401 {
    private Date date = new Date();
    private int status;
    private String error;
    private String message;
    private String path;

    public Error401(int status, String error, String message, String path){
        date.getTime();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
