package com.bramgussekloo.projects.repositories;

import com.bramgussekloo.projects.models.Building;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Integer> {
    Building findByName(String name);
    Building findById(int id);
    Building deleteBuildingById(int id);
}
