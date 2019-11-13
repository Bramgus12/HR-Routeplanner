package com.bramgussekloo.projects.FileHandling;

public class FileException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String msg;

    public FileException(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}

