package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.dataclasses.XlsxReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class XlsxReadingStatements {
    public static void uploadFile(MultipartFile file) throws IOException {
        try{
            FileService.uploadFile(file, "ElectionCourse");
        }
        catch (IOException e){
            throw new IOException("Upload Election Course file failed. Try again.\n" + e.getMessage());
        }
    }

    private static File getFile(String resourceFolderName, String fileName, String statementClassName) throws IOException {
        ClassLoader classLoader = LocationNodeNetworkStatements.class.getClassLoader();
        if (LocationNodeNetworkStatements.class.getResource(statementClassName + ".class").toString().contains("jar")) {
            File file = new File("/usr/share/hr-routeplanner/ProjectC/Back-end/src/main/resources/" + resourceFolderName + "/" + fileName);
            if (!file.exists()) {
                throw new IOException("File not found");
            } else {
                return file;
            }
        }
        else {
            File resource = new File("src/main/resources/" + resourceFolderName + "/" + fileName);
            System.out.println(resource.getAbsolutePath());
            if (!resource.exists()) {
                throw new IOException("File not found");
            } else {
                return resource;
            }
        }
    }

    public static List<XlsxReader> getExcelContent() throws IOException{
        List rowList = new ArrayList<>();
        try {
            String fileName = "kv-lijst.xlsx";
            Workbook workbook = null;
            FileInputStream excelFile = new FileInputStream(getFile("ElectionCourse", fileName, "XlsxReadingStatements"));

            //Find the file extension by splitting file name in substring  and getting only extension name
            String fileExtensionName = fileName.substring(fileName.indexOf("."));

            //Check condition if the file is a .xls file or .xlsx file
            if(fileExtensionName.equals(".xls")){
                //If it is .xls file then create object of HSSFWorkbook class
                workbook = new HSSFWorkbook(excelFile);
            }
            else if(fileExtensionName.equals(".xlsx")) {
                //If it is .xlsx file then create object of XSSFWorkbook class
                workbook = new XSSFWorkbook(excelFile);
            }

            Sheet worksheet = workbook.getSheetAt(0);
            int rowcount = worksheet.getLastRowNum()- worksheet.getFirstRowNum();
            System.out.println("Total row number: "+rowcount);

            /**
             * Works up to this point.
             *
             */
            for(int i=0; i<rowcount+1; i++){
                List contentList = new ArrayList();
                //Create a loop to get the cell values of a row for one iteration
                Row row = worksheet.getRow(i);
                XlsxReader content = new XlsxReader();
                // Create an object reference of 'Cell' class
                content.setTitle(row.getCell(i).getStringCellValue());
                for(int j=0; j<row.getLastCellNum(); j++){
                    Cell cell = row.getCell(j);
                    // Add all the cell values of a particular row
                    content.setId(i);
                    switch (cell.getCellType()){
                        case NUMERIC:
                            content.setContent(cell.getNumericCellValue());
                            break;
                        case STRING:
                            content.setContent(cell.getStringCellValue());
                            break;
                        case BLANK:
                            break;
                    }
                    contentList.add(content);
                }
                System.out.println("Size of the arrayList: "+contentList.size());
                System.out.println(contentList.toString());
                // Create an iterator to iterate through the arrayList- 'arrName'
                rowList.add(contentList);
                System.out.println(rowList.get(i));
            }

            return rowList;
        } catch (IOException e) {
            throw new IOException("File not found");
        }
    }
}
