package com.bramgussekloo.projects.dataclasses;

public class BuildingInstitute {
    private Integer id, buildingId, instituteId;

    public BuildingInstitute(Integer id, Integer buildingId, Integer instituteId) {
        this.id = id;
        this.buildingId = buildingId;
        this.instituteId = instituteId;
    }

    public BuildingInstitute(){}

    public Integer getId() {
        return id;
    }

    public Integer getBuilding_id() {
        return buildingId;
    }

    public Integer getInstituteId() {
        return instituteId;
    }
}
