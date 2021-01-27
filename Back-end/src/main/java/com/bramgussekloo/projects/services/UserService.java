package com.bramgussekloo.projects.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

import com.bramgussekloo.projects.config.SecurityConfig;
import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.NotFoundException;
import com.bramgussekloo.projects.models.User;
import com.bramgussekloo.projects.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public Iterable<User> getAll() throws Exception {
        return repository.findAll();
    }

    public User create(User user) throws Exception {
        boolean userExists = false;
        Iterable<User> users = getAll();

        for (User userFromList : users) {
            if (user.getUser_name().equals(userFromList.getUser_name())) {
                userExists = true;
                break;
            }
        }

        if (!userExists) {
            if (!user.getPassword().isEmpty() && user.getPassword().length() > 6) {
                user.setPassword(SecurityConfig.HashUserPassword(user.getPassword()));
                return repository.save(user);
            } else {
                throw new BadRequestException("Password is not long enough. It has to be at least a length of 6.");
            }
        } else {
            throw new BadRequestException("User already exists.");
        }
    }

    public void update(User user, int userId) throws NotFoundException {
        Optional<User> usersOptional = repository.findById(userId);
        if (!usersOptional.isPresent()) {
            throw new NotFoundException("User doesn't exist");
        }
        user.setId(userId);
        repository.save(user);
    }

    public void delete(int userId){
        repository.deleteById(userId);
    }
}
