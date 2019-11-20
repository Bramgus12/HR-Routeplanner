package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public class XlsxReadingStatements {

    public static void uploadFile(MultipartFile file) throws IOException {
        try{
            FileService.uploadFile(file, "ElectionCourse");
        }
        catch (IOException e){
            throw new IOException("Upload Election Course file failed. Try again.\n" + e.getMessage());
        }
    }
}
