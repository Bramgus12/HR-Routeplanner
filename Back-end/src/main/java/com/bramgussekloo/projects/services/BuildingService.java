package com.bramgussekloo.projects.services;

import com.bramgussekloo.projects.exceptions.NotFoundException;
import com.bramgussekloo.projects.models.Building;
import com.bramgussekloo.projects.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository repository;

    public BuildingService() { }

    public Iterable<Building> getAllBuildings() {
        return repository.findAll();
    }

    public Building getBuildingByName(String name) {
        return repository.findByName(name);
    }

    public Building getBuildingById(int id) {
        return repository.findById(id);
    }

    public Building createBuilding(Building building) {
        return repository.save(building);
    }

    public Building deleteBuilding(int id) {
        return repository.deleteBuildingById(id);
    }

    public Building updateBuilding(Building building, int id) throws Exception {
        Building buildingOptional = repository.findById(id);
        if (buildingOptional == null) {
            throw new NotFoundException("Building doesn't exist");
        }
        building.setId(id);
        return repository.save(building);
    }
}
