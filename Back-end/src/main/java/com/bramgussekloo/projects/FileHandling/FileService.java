package com.bramgussekloo.projects.FileHandling;

import com.bramgussekloo.projects.Properties.GetPropertyValues;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    public static void uploadFile(MultipartFile file, String folder, String fileName) throws IOException {
        File f = GetPropertyValues.getResourcePath(folder, fileName);
        Path copyLocation = Paths.get(String.valueOf(f));
        Files.copy(file.getInputStream(), copyLocation);
        OutputStream os = Files.newOutputStream(copyLocation);
        os.write(file.getBytes());
    }
}