package com.bramgussekloo.projects.services;

import com.bramgussekloo.projects.config.DatabaseConnection;
import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.NotFoundException;
import com.bramgussekloo.projects.models.Address;
import com.bramgussekloo.projects.models.LocationNodeNetwork;
import com.bramgussekloo.projects.models.Node;
import com.bramgussekloo.projects.repositories.AddressRepository;
import com.bramgussekloo.projects.utils.GetPropertyValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository repository;
    private final BuildingService buildingService;

    public AddressService(AddressRepository repository, BuildingService buildingService) {
        this.repository = repository;
        this.buildingService = buildingService;
    }

    public Address saveAddress(Address address) {
        return repository.save(address);
    }

    public Iterable<Address> getAllAddresses() {
        return repository.findAll();
    }

    public Address findAddress(long id) throws Exception {
        Address address = repository.findAddressById(id);
        if (address != null) {
            return address;
        }
        throw new NotFoundException("Address not found");
    }

    public Address deleteAddress(long id) {
        return repository.deleteById(id);
    }

    public Address updateAddress(Address address, long id) throws Exception {
        Optional<Address> addressOptional = repository.findById(id);
        if (addressOptional.isPresent()) {
            throw new NotFoundException("Address doesn't exist");
        }
        address.setId(id);
        return repository.save(address);
    }

    public Address getFromDatabaseByRoomCode(String code) throws Exception {
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        String locationName = "";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                for (Node node : network.getNodes()) {
                    if (node.getType().equals("Room") && node.getCode().toLowerCase().equals(code.toLowerCase())) {
                        locationName = network.getLocationName();
                        break;
                    }
                }
            }
        }
        if (locationName.isEmpty()) {
            throw new IOException("Room cannot be found in the locationNodeNetworks");
        } else {
            return repository.findAddressById(buildingService.getBuildingByName(locationName).getId());
        }
    }

    /**
     * Get an address by building name. This will update the values in the object!
     *
     * @param buildingName The buildingName you want to get the address of.
     * @throws Exception Will be handled by the HandleExceptions class.
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public Address getFromDatabaseByBuildingName(String buildingName) throws Exception {
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        String name = "";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                if (network.getLocationName().equals(buildingName)) {
                    name = buildingName;
                    break;
                }
            }
        }
        if (!name.isEmpty()) {
            return repository.findAddressById(buildingService.getBuildingByName(name).getId());
        } else {
            throw new BadRequestException("Building does not exist");
        }
    }
}
