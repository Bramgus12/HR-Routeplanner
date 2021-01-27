package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.InternalServerException;
import com.bramgussekloo.projects.config.DatabaseConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BuildingInstitute {

    private Integer buildingId;

    private Integer instituteId;

    public BuildingInstitute(Integer buildingId, Integer instituteId) {
        this.buildingId = buildingId;
        this.instituteId = instituteId;
    }

    public BuildingInstitute() { }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }
}
