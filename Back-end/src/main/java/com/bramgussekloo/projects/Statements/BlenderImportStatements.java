package com.bramgussekloo.projects.Statements;

import com.bramgussekloo.projects.DataClasses.BlenderImport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class BlenderImportStatements {

    public static BlenderImport getBlenderImport(String locationName) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(locationName + ".json");
        if (file.exists()){
            BlenderImport blenderImport = mapper.readValue(file, BlenderImport.class);
            return blenderImport;
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static void createBlenderImport(BlenderImport blenderImport) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(blenderImport.getLocationName() + ".json");
        if (!jsonFile.exists()) {
            String jsonObject = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(blenderImport);
            PrintWriter out = new PrintWriter(blenderImport.getLocationName() + ".json");
            out.println(jsonObject);
            out.flush();
            out.close();
        } else {
            throw new IOException(blenderImport.getLocationName() + ".json already exists. Try deleting it first");
        }
    }

    public static BlenderImport deleteBlenderImport(String locationName) throws IOException{
        File file = new File(locationName + ".json");
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            BlenderImport blenderImport = mapper.readValue(file, BlenderImport.class);
            if (file.delete()){
                return blenderImport;
            } else {
                throw new IOException("Deleting of file " + locationName + ".json has failed.");
            }
        } else {
            throw new IOException(locationName + ".json does not exist.");
        }
    }

    public static BlenderImport updateBlenderImport(String locationName, BlenderImport blenderImport) throws IOException{
        File file = new File(locationName + ".json");
        if (file.exists()){
            if (file.delete()){
                if (locationName.equals(blenderImport.getLocationName())) {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(blenderImport);
                    PrintWriter out = new PrintWriter(locationName + ".json");
                    out.println(jsonString);
                    out.flush();
                    out.close();
                    BlenderImport blenderImportOut = mapper.readValue(file, BlenderImport.class);
                    return blenderImportOut;
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
