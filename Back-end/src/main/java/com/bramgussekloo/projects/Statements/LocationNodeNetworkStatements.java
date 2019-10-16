package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.LocationNodeNetwork;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class LocationNodeNetworkStatements {

    public static LocationNodeNetwork getLocationNodeNetwork(String locationName) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(locationName + ".json");
        if (file.exists()){
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            return locationNodeNetwork;
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static void createLocationNodeNetwork(LocationNodeNetwork locationNodeNetwork) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(locationNodeNetwork.getLocationName() + ".json");
        if (!jsonFile.exists()) {
            String jsonObject = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(locationNodeNetwork);
            PrintWriter out = new PrintWriter(locationNodeNetwork.getLocationName() + ".json");
            out.println(jsonObject);
            out.flush();
            out.close();
        } else {
            throw new IOException(locationNodeNetwork.getLocationName() + ".json already exists. Try deleting it first");
        }
    }

    public static LocationNodeNetwork deleteLocationNodeNetwork(String locationName) throws IOException{
        File file = new File(locationName + ".json");
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            LocationNodeNetwork locationNodeNetwork = mapper.readValue(file, LocationNodeNetwork.class);
            if (file.delete()){
                return locationNodeNetwork;
            } else {
                throw new IOException("Deleting of file " + locationName + ".json has failed.");
            }
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static LocationNodeNetwork updateLocationNodeNetwork(String locationName, LocationNodeNetwork locationNodeNetwork) throws IOException{
        File file = new File(locationName + ".json");
        if (file.exists()){
            if (file.delete()){
                if (locationName.equals(locationNodeNetwork.getLocationName())) {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(locationNodeNetwork);
                    PrintWriter out = new PrintWriter(locationName + ".json");
                    out.println(jsonString);
                    out.flush();
                    out.close();
                    LocationNodeNetwork locationNodeNetworkOut = mapper.readValue(file, LocationNodeNetwork.class);
                    return locationNodeNetworkOut;
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
}
