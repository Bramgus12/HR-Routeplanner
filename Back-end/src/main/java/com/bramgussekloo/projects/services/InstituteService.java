package com.bramgussekloo.projects.services;

import java.util.Optional;

import com.bramgussekloo.projects.exceptions.NotFoundException;
import com.bramgussekloo.projects.models.Institute;
import com.bramgussekloo.projects.repositories.InstituteRepository;

import org.springframework.stereotype.Service;

@Service
public class InstituteService {
    
    private InstituteRepository repository;

    public InstituteService(InstituteRepository repository) {
        this.repository = repository;
    }

    public Iterable<Institute> getAll() {
        return repository.findAll();
    }

    public Institute findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public Institute findByName(String name) {
        return repository.findByName(name).orElseThrow();
    }

    public Institute create(Institute institute) {
        return repository.save(institute);
    }

    public Institute update(Institute institute, Integer id) throws Exception{
        Optional<Institute> optionalInstitute = repository.findById(id);
        if (!optionalInstitute.isPresent()) {
            throw new NotFoundException("Institute not found");
        }
        institute.setId(id);
        return repository.save(institute);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
