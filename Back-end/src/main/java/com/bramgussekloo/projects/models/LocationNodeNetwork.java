package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.InternalServerException;
import com.bramgussekloo.projects.services.AddressService;
import com.bramgussekloo.projects.services.BuildingService;
import com.bramgussekloo.projects.utils.FileService;
import com.bramgussekloo.projects.utils.GetPropertyValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@ApiModel(description = "Model of LocationNodeNetwork")
@Service
public class LocationNodeNetwork {

    @ApiModelProperty(notes = "Name of the location", required = true)
    private String locationName;

    @ApiModelProperty(notes = "All the nodes in the locationNodeNetwork", required = true)
    private ArrayList<Node> nodes;

    @ApiModelProperty(notes = "All the connections in the locationNodeNetwork", required = true)
    private ArrayList<ConnectedNode> connections;

    public LocationNodeNetwork(String locationName, ArrayList<Node> nodes, ArrayList<ConnectedNode> connections) {
        this.locationName = locationName;
        this.connections = connections;
        this.nodes = nodes;
    }

    public LocationNodeNetwork() {
    }

    public LocationNodeNetwork(String locationName) {
        this.locationName = locationName;
    }

    @Autowired
    public LocationNodeNetwork(AddressService addressService, BuildingService service) {
        this.addressService = addressService;
        this.service = service;
    }

    public ArrayList<ConnectedNode> getConnections() {
        return connections;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public String getLocationName() {
        return locationName;
    }

    public AddressService addressService;

    public BuildingService service;

    /**
     * This will override all the existing values in the object.
     *
     * @param locationName The name of the locationNodeNetwork which you want the values of.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getLocationNodeNetwork(String locationName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (file.exists()) {
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            this.locationName = locationNodeNetwork.getLocationName();
            this.nodes = locationNodeNetwork.getNodes();
            this.connections = locationNodeNetwork.getConnections();
        } else {
            throw new BadRequestException("LocationNodeNetwork doesn't exist");
        }
    }

    /**
     * Create a locationNodeNetwork on the server. Will update this object with the values from the locationNodeNetwork.
     *
     * @param file The file of the locationNodeNetwork you want to have updated.
     * @param addressId The id of the address that is associated with the locationNodeNetwork.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void createLocationNodeNetwork(MultipartFile file, Integer addressId) throws Exception {
        File f = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
        Tika tika = new Tika();
        System.out.println(file.getContentType());
        String mimeType = tika.detect(file.getBytes());
        if (mimeType != null && mimeType.equals("text/plain")
                && Objects.equals(file.getContentType(), "application/json")
        ) {
            if (!f.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                FileService.uploadFile(file, "Locations", file.getOriginalFilename());
                File fileRef = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
                if (f.exists()) {
                    try {
                        LocationNodeNetwork locationNodeNetwork = mapper.readValue(fileRef, LocationNodeNetwork.class);
                        System.out.println(addressService.findAddress(addressId));
                        Building building = new Building(null ,addressService.findAddress(addressId), locationNodeNetwork.getLocationName());
                        service.createBuilding(building);
                        this.locationName = locationNodeNetwork.getLocationName();
                        this.connections = locationNodeNetwork.getConnections();
                        this.nodes = locationNodeNetwork.getNodes();
                    } catch (IOException e) {
                        if (fileRef.delete()) {
                            throw new InternalServerException(e.getMessage());
                        } else {
                            throw new InternalServerException(e.getMessage() + " and File deletion failed");
                        }
                    }
                } else {
                    throw new BadRequestException(file.getOriginalFilename() + ".json already exists. Try put if you wanna change it.");
                }
            } else {
                throw new BadRequestException("File already exists");
            }
        } else {
            throw new BadRequestException("That is not a json file");
        }
    }

    /**
     * Deletes the locationNodeNetwork on the server. This will be done by the locationName which is in this object.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     */
    public void deleteLocationNodeNetwork() throws Exception {
        File file = GetPropertyValues.getResourcePath("Locations", this.locationName + ".json");
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            String networkName = locationNodeNetwork.getLocationName();
            Building building = service.getBuildingByName(networkName);
            if (file.delete()) {
                service.deleteBuilding(building.getId());
            } else {
                throw new InternalServerException("Deleting of file " + this.locationName + ".json has failed.");
            }
        } else {
            throw new BadRequestException(this.locationName + ".json does not exist.");
        }
    }



    /**
     * Updates the file on the server by a new file. After the update the object will be updated with the new values.
     *
     * @param file The file that has to replace the old file.
     * @param addressId The id of the address of the location.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void updateLocationNodeNetwork(MultipartFile file, Integer addressId) throws Exception {
        File resource = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getBytes());
        if (mimeType != null && mimeType.equals("text/plain")
                && Objects.requireNonNull(file.getOriginalFilename()).startsWith(this.locationName)
                && Objects.equals(file.getContentType(), "application/json")
        ) {
            if (resource.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                FileService.uploadFile(file, "Locations", Objects.requireNonNull(file.getOriginalFilename()).replace(".json", "_1.json"));
                File tmpFile = GetPropertyValues.getResourcePath("Locations", Objects.requireNonNull(file.getOriginalFilename()).replace(".json", "_1.json"));
                try {
                    LocationNodeNetwork network = mapper.readValue(tmpFile, LocationNodeNetwork.class);
                    String name = network.getLocationName();
                    Building building = service.getBuildingByName(name);
                    if (resource.delete() && tmpFile.delete()) {
                        FileService.uploadFile(file, "Locations", file.getOriginalFilename());
                        building.setAddress(addressService.findAddress(addressId));
                        building.setName(network.getLocationName());
                        service.updateBuilding(building, building.getId());
                        LocationNodeNetwork locationNodeNetwork =  mapper.readValue(resource, LocationNodeNetwork.class);
                        this.connections = locationNodeNetwork.getConnections();
                        this.nodes = locationNodeNetwork.getNodes();
                        this.locationName = locationNodeNetwork.getLocationName();
                    } else {
                        throw new InternalServerException("File deletion failed");
                    }
                } catch (IOException e) {
                    if (tmpFile.delete()) {
                        throw new InternalServerException(e.getMessage());
                    } else {
                        throw new InternalServerException(e.getMessage() + " and Temporary file deletion failed");
                    }
                }
            } else {
                throw new BadRequestException("File does not exist yet. Use post for creating a new file.");
            }
        } else {
            throw new BadRequestException("That is not a json file or name of location and file name are not the same");
        }
    }


}
