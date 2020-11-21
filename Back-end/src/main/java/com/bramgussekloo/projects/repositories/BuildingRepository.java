package com.bramgussekloo.projects.repositories;

import com.bramgussekloo.projects.models.Building;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long> {
    Building findBuildingByName(String name);
    Building findBuildingById(long id);
    Building deleteBuildingById(long id);
}
