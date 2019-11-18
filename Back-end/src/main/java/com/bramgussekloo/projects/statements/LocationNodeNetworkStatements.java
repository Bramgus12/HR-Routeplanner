package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.dataclasses.LocationNodeNetwork;
import com.bramgussekloo.projects.dataclasses.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class LocationNodeNetworkStatements {


    private static File getFile(String locationName) throws IOException {
        ClassLoader classLoader = LocationNodeNetworkStatements.class.getClassLoader();
        if (LocationNodeNetworkStatements.class.getResource("LocationNodeNetworkStatements.class").toString().contains("jar")) {
            File file = new File("/usr/share/hr-routeplanner/ProjectC/Back-end/src/main/resources/Locations/" + locationName);
            if (!file.exists()) {
                throw new IOException("File not found");
            } else {
                return file;
            }
        } else {
            URL resource = classLoader.getResource("Locations/" + locationName);
            if (resource == null) {
                throw new IOException("File not found");
            } else {
                return new File("../");
            }
        }
    }

    private static File getLocationsFolder() throws IOException {
        ClassLoader classLoader = LocationNodeNetworkStatements.class.getClassLoader();
        if (LocationNodeNetworkStatements.class.getResource("LocationNodeNetworkStatements.class").toString().contains("jar")) {
            File file = new File("/usr/share/hr-routeplanner/ProjectC/Back-end/src/main/resources/Locations/");
            if (!file.exists()) {
                throw new IOException("File not found");
            } else {
                return file;
            }
        } else {
            URL resource = classLoader.getResource("Locations/");
            if (resource == null) {
                throw new IOException("File not found");
            } else {
                return new File(resource.toString().replace("file:", ""));
            }
        }
    }

    public static LocationNodeNetwork getLocationNodeNetwork(String locationName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = getFile(locationName);
        return mapper.readValue(file, LocationNodeNetwork.class);
    }

    public static LocationNodeNetwork createLocationNodeNetwork(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            FileService.uploadFile(file, "Locations");
            System.out.println(file.getOriginalFilename());
            File fileRef = getFile(file.getOriginalFilename());
            if (fileRef.exists() && Objects.requireNonNull(file.getOriginalFilename()).contains(".json")) {
                return mapper.readValue(fileRef, LocationNodeNetwork.class);
            } else {
                throw new IOException(file.getOriginalFilename() + ".json already exists. Try put if you wanna change it.");
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static LocationNodeNetwork deleteLocationNodeNetwork(String locationName) throws IOException {
        File file = getFile(locationName);
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            if (file.delete()) {
                return locationNodeNetwork;
            } else {
                throw new IOException("Deleting of file " + locationName + ".json has failed.");
            }
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static ArrayList<Node> getAllNodesByType(String locationName, String definedType) throws IOException {
        File file = getFile(locationName);
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

    public static LocationNodeNetwork updateLocationNodeNetwork(String locationName, LocationNodeNetwork locationNodeNetwork) throws IOException {
        File file = getFile(locationName);
        if (file.exists()) {
            if (file.delete()) {
                if (locationName.equals(locationNodeNetwork.getLocationName())) {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(locationNodeNetwork);
                    PrintWriter out = new PrintWriter(file);
                    out.println(jsonString);
                    out.flush();
                    out.close();
                    return mapper.readValue(file, LocationNodeNetwork.class);
                } else {
                    throw new IOException("locationName in URL and locationName in JSON are not the same.");
                }
            } else {
                throw new IOException("File deletion did not go well");
            }
        } else {
            throw new IOException("File does not exist yet. Use post for creating a new file.");
        }
    }

    public static ArrayList<Node> getAllRooms() throws IOException {
        File folder = getLocationsFolder();
        File[] listOfFiles = folder.listFiles();
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                ObjectMapper mapper = new ObjectMapper();
                LocationNodeNetwork network = mapper.readValue(listOfFile, LocationNodeNetwork.class);
                for (Node node : network.getNodes()) {
                    if (node.getType().equals("Room")) {
                        nodeArrayList.add(node);
                    }
                }
            } else {
                break;
            }
        }
        return nodeArrayList;
    }

    public static void uploadFile(MultipartFile file) throws IOException {
        try {
            FileService.uploadFile(file, "Locations");
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
