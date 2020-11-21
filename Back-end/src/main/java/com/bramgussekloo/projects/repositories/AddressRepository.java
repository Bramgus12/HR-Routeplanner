package com.bramgussekloo.projects.repositories;

import com.bramgussekloo.projects.models.Address;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address findAddressById(long id);

    @Transactional
    Address deleteById(long id);
}
