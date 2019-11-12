package com.bramgussekloo.projects.FileHandling;

import com.bramgussekloo.projects.statements.LocationNodeNetworkStatements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    public static void uploadFile(MultipartFile file) throws FileException, IOException {
        System.out.println(FileService.class.getResource("FileService.class").toString().toLowerCase());
        if (FileService.class.getResource("FileService.class").toString().toLowerCase().contains("file")) {
            Path copyLocation = Paths.get("src/main/resources/Locations/" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), copyLocation);
            OutputStream os = Files.newOutputStream(copyLocation);
            os.write(file.getBytes());
        } else if (FileService.class.getResource("FileService.class").toString().toLowerCase().contains("jar")) {
            Path copyLocation = Paths.get("/usr/share/hr-routeplanner/ProjectC/Back-end/src/main/resources/Locations/" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), copyLocation);
            OutputStream os = Files.newOutputStream(copyLocation);
            os.write(file.getBytes());
        } else {
            throw new FileException("file could not be made");
        }
    }
}