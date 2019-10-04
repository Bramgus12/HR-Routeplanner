package com.bramgussekloo.projects.DataClasses;

public class EntryDest {
    private Integer id, node_id, building_id, level;
    private String name;

    public EntryDest(Integer id, Integer node_id, Integer building_id, Integer level, String name){
        this.id = id;
        this.node_id = node_id;
        this.building_id = building_id;
        this.level = level;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getNode_id() {
        return node_id;
    }
}

