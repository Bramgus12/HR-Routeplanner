package com.bramgussekloo.projects.services;

import com.bramgussekloo.projects.models.Building;
import com.bramgussekloo.projects.models.BuildingInstitute;
import com.bramgussekloo.projects.models.Institute;
import com.bramgussekloo.projects.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BuildingInstituteService {

    private final InstituteService instituteService;

    private final BuildingService buildingService;

    public BuildingInstituteService(InstituteService instituteService, BuildingService buildingService) {
        this.instituteService = instituteService;
        this.buildingService = buildingService;
    }

    public BuildingInstitute linkBuildingInstitute(Integer buildingId, Integer instituteId) throws Exception {
        Building building = buildingService.getBuildingById(buildingId);
        Institute institute = instituteService.findById(instituteId);

        institute.getBuildings().add(building);
        building.getInstitutes().add(institute);

        buildingService.updateBuilding(building, buildingId);
        instituteService.update(institute, instituteId);

        return new BuildingInstitute(buildingId, instituteId);
    }

    public Set<Building> deleteInstituteFromBuildings(Integer instituteId) {
        Institute institute = instituteService.findById(instituteId);
        Set<Building> buildings = institute.getBuildings();

        for (Building building : buildings) {
            building.getInstitutes().remove(institute);
        }
        return buildings;
    }

    public Set<Institute> deleteBuildingFromInstitutes(Integer buildingId) {
        Building building = buildingService.getBuildingById(buildingId);
        Set<Institute> institutes = building.getInstitutes();

        for (Institute institute : institutes) {
            institute.getBuildings().remove(building);
        }
        return institutes;
    }
}
