package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class XlsxReadingStatements {

    public static void uploadFile(MultipartFile file) throws IOException {
        FileService.uploadFile(file, "ElectionCourse");
    }
}
