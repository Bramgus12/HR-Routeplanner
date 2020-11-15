package com.bramgussekloo.projects.repositories;

import com.bramgussekloo.projects.models.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address findAddressById(Long id);
}
