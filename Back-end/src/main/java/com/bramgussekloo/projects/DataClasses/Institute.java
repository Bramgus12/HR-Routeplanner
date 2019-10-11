package com.bramgussekloo.projects.DataClasses;

public class Institute {
    private Integer id;
    private String name;

    public Institute(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
