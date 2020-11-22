package com.bramgussekloo.projects.repositories;

import com.bramgussekloo.projects.models.Building;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Integer> {
    Building findBuildingByName(String name);
    Building findBuildingById(int id);
    Building deleteBuildingById(int id);
}
