package com.bramgussekloo.projects.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import com.bramgussekloo.projects.models.Institute;

import org.springframework.data.repository.CrudRepository;

public interface InstituteRepository extends CrudRepository<Institute, Integer> {
    public Optional<Institute> findByName(String name);
    @Transactional
    Institute deleteById(int id);
}
