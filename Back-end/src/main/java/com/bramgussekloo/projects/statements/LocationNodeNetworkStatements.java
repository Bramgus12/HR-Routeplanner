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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

@Deprecated
public class LocationNodeNetworkStatements {

    @Deprecated
    public static LocationNodeNetwork getLocationNodeNetwork(String locationName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (file.exists()) {
            return mapper.readValue(file, LocationNodeNetwork.class);
        } else {
            throw new IOException("File not found");
        }
    }

    @Deprecated
    public static LocationNodeNetwork createLocationNodeNetwork(MultipartFile file, Integer addressId) throws Exception {
        File f = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
        String mimeType = Files.probeContentType(f.toPath());
        System.out.println(mimeType);
        if (mimeType != null && mimeType.equals("application/json")) {
            if (!f.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                FileService.uploadFile(file, "Locations", file.getOriginalFilename());
                File fileRef = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
                if (f.exists()) {
                    try {
                        LocationNodeNetwork locationNodeNetwork = mapper.readValue(fileRef, LocationNodeNetwork.class);
                        Building building = new Building(null, addressId, locationNodeNetwork.getLocationName());
                        building.createInDatabase();
                        return locationNodeNetwork;
                    } catch (IOException e) {
                        if (fileRef.delete()) {
                            throw new IOException(e.getMessage());
                        } else {
                            throw new IOException(e.getMessage() + " and File deletion failed");
                        }
                    }
                } else {
                    throw new IOException(file.getOriginalFilename() + ".json already exists. Try put if you wanna change it.");
                }
            } else {
                throw new IOException("File already exists");
            }
        } else {
            throw new IOException("That is not a json file");
        }
    }

    @Deprecated
    public static LocationNodeNetwork deleteLocationNodeNetwork(String locationName) throws Exception {
        File file = GetPropertyValues.getResourcePath("Locations", locationName + ".json");
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            String networkName = locationNodeNetwork.getLocationName();
            Building building = new Building();
            building.getFromDatabaseByName(networkName);
            if (file.delete()) {
                building.deleteBuilding();
                return locationNodeNetwork;
            } else {
                throw new IOException("Deleting of file " + locationName + ".json has failed.");
            }
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    @Deprecated
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

    @Deprecated
    public static LocationNodeNetwork updateLocationNodeNetwork(String locationName, MultipartFile file, Integer addressId) throws Exception {
        File resource = GetPropertyValues.getResourcePath("Locations", file.getOriginalFilename());
        String mimeType = Files.probeContentType(resource.toPath());
        if (mimeType != null && mimeType.equals("application/json") && Objects.requireNonNull(file.getOriginalFilename()).startsWith(locationName)) {
            if (resource.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                FileService.uploadFile(file, "Locations", Objects.requireNonNull(file.getOriginalFilename()).replace(".json", "_1.json"));
                File tmpFile = GetPropertyValues.getResourcePath("Locations", Objects.requireNonNull(file.getOriginalFilename()).replace(".json", "_1.json"));
                try {
                    LocationNodeNetwork network = mapper.readValue(tmpFile, LocationNodeNetwork.class);
                    String name = network.getLocationName();
                    Building building = new Building();
                    building.getFromDatabaseByName(name);
                    if (resource.delete() && tmpFile.delete()) {
                        FileService.uploadFile(file, "Locations", file.getOriginalFilename());
                        building.setAddress_id(addressId);
                        building.setName(network.getLocationName());
                        building.updateBuilding();
                        return mapper.readValue(resource, LocationNodeNetwork.class);
                    } else {
                        throw new IOException("File deletion did not go well");
                    }
                } catch (IOException e) {
                    if (tmpFile.delete()) {
                        throw new IOException(e.getMessage());
                    } else {
                        throw new IOException(e.getMessage() + " and Temporary file deletion failed");
                    }
                }
            } else {
                throw new IOException("File does not exist yet. Use post for creating a new file.");
            }
        } else {
            throw new IOException("That is not a json file or name of location and file name are not the same");
        }
    }

    @Deprecated
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
