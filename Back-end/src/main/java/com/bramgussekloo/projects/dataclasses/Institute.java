package com.bramgussekloo.projects.dataclasses;

public class Institute {
    private Integer id;
    private String name;

    public Institute(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Institute(){}

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
