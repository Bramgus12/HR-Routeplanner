package com.bramgussekloo.projects.repositories;

import org.springframework.data.repository.CrudRepository;
import com.bramgussekloo.projects.models.User;

public interface UserRepository extends CrudRepository<User, Integer>{
    
}
