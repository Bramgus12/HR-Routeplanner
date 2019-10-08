package com.bramgussekloo.projects.DataClasses;

public class BuildingInstitute {
    private Integer id, building_id, institute_id;

    public BuildingInstitute(Integer id, Integer building_id, Integer institute_id) {
        this.id = id;
        this.building_id = building_id;
        this.institute_id = institute_id;
    }

    public BuildingInstitute(){}

    public Integer getId() {
        return id;
    }

    public Integer getBuilding_id() {
        return building_id;
    }

    public Integer getInstitute_id() {
        return institute_id;
    }
}
