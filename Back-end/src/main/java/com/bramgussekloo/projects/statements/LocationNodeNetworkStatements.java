package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.dataclasses.Building;
import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.bramgussekloo.projects.dataclasses.NodesAndBuildingName;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class LocationNodeNetworkStatements {

    public static LocationNodeNetwork getLocationNodeNetwork(String locationName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (file.exists()) {
            return mapper.readValue(file, LocationNodeNetwork.class);
        } else {
            throw new IOException("File not found");
        }
    }

    public static LocationNodeNetwork createLocationNodeNetwork(MultipartFile file, Integer addressId) throws IOException, SQLException {
        File f = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
        if (!f.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            FileService.uploadFile(file, "Locations", file.getOriginalFilename());
            File fileRef = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
            if (f.exists() && Objects.requireNonNull(file.getOriginalFilename()).contains(".json")) {
                LocationNodeNetwork locationNodeNetwork = mapper.readValue(fileRef, LocationNodeNetwork.class);
                Building building = new Building(null, addressId, locationNodeNetwork.getLocationName());
                BuildingStatements.createBuilding(building);
                return locationNodeNetwork;
            } else {
                throw new IOException(file.getOriginalFilename() + ".json already exists. Try put if you wanna change it.");
            }
        } else {
            throw new IOException("File already exists");
        }
    }

    public static LocationNodeNetwork deleteLocationNodeNetwork(String locationName) throws IOException, SQLException {
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            String networkName = locationNodeNetwork.getLocationName();
            Building building = BuildingStatements.getBuildingByName(networkName);
            if (file.delete()) {
                BuildingStatements.deleteBuilding(building.getId());
                return locationNodeNetwork;
            } else {
                throw new IOException("Deleting of file " + locationName + ".json has failed.");
            }
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static ArrayList<Node> getAllNodesByType(String locationName, String definedType) throws IOException {
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        ArrayList<Node> nodeList = new ArrayList<>();
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
            for (Node node : network.getNodes()) {
                if (node.getType().equals(definedType)) {
                    nodeList.add(node);
                }
            }
            return nodeList;
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static LocationNodeNetwork updateLocationNodeNetwork(String locationName, MultipartFile file, Integer addressId) throws IOException, SQLException {
        File resource = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (resource.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork network = mapper.readValue(resource, LocationNodeNetwork.class);
            String name = network.getLocationName();
            Building building = BuildingStatements.getBuildingByName(name);
            if (resource.delete()) {
                FileService.uploadFile(file, "Locations", file.getOriginalFilename());
                Building newBuilding = new Building(building.getId(), addressId, network.getLocationName());
                BuildingStatements.updateBuilding(newBuilding);
                return mapper.readValue(resource, LocationNodeNetwork.class);
            } else {
                throw new IOException("File deletion did not go well");
            }
        } else {
            throw new IOException("File does not exist yet. Use post for creating a new file.");
        }
    }

    public static ArrayList<NodesAndBuildingName> getAllRooms() throws IOException {
        File folder = GetPropertyValues.getResourcePath("Locations", "");
        File[] listOfFiles = folder.listFiles();
        ArrayList<NodesAndBuildingName> nodeArrayList = new ArrayList<>();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && !file.toString().contains(".gitkeep")) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(file, LocationNodeNetwork.class);
                ArrayList<Node> nodes = new ArrayList<>();
                for (Node node : network.getNodes()) {
                    if (node.getType().equals("Room")) {
                        String label = node.getLabel().toLowerCase();
                        if (!label.isEmpty()) {
                            if (!label.contains("trap") && !label.contains("lift")
                                    && !label.contains("toilet") && !label.contains("gang") && !label.contains("hal")
                                    && !label.contains("berging") && !label.contains("portaal")) {
                                nodes.add(node);
                            }
                        } else {
                            nodes.add(node);
                        }
                    }
                }
                NodesAndBuildingName rnib = new NodesAndBuildingName(nodes, network.getLocationName());
                nodeArrayList.add(rnib);
            }
        }
        return nodeArrayList;
    }
}
