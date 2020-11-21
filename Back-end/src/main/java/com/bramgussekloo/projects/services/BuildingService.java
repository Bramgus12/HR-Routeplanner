package com.bramgussekloo.projects.services;

import com.bramgussekloo.projects.exceptions.NotFoundException;
import com.bramgussekloo.projects.models.Address;
import com.bramgussekloo.projects.models.Building;
import com.bramgussekloo.projects.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuildingService {

    private final BuildingRepository repository;

    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    public Iterable<Building> getAllBuildings() {
        return repository.findAll();
    }

    public Building getBuildingByName(String name) {
        return repository.findBuildingByName(name);
    }

    public Building getBuildingById(long id) {
        return repository.findBuildingById(id);
    }

    public Building createBuilding(Building building) {
        return repository.save(building);
    }

    public Building deleteBuilding(long id) {
        return repository.deleteBuildingById(id);
    }

    public Building updateBuilding(Building building, long id) throws Exception {
        Optional<Building> buildingOptional = repository.findById(id);
        if (buildingOptional.isPresent()) {
            throw new NotFoundException("Address doesn't exist");
        }
        building.setId(id);
        return repository.save(building);
    }
}
