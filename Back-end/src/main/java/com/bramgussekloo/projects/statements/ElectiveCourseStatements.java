package com.bramgussekloo.projects.statements;

import com.bramgussekloo.projects.FileHandling.FileService;
import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.ElectiveCourse;
import com.bramgussekloo.projects.dataclasses.ElectiveCourseDescription;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PDO implementation
 * source: https://javaconceptoftheday.com/statement-vs-preparedstatement-vs-callablestatement-in-java/
 */
public class ElectiveCourseStatements {
    private static String fileNameVar = "kv-lijst.xlsx";
    private static ElectiveCourse defaultList = new ElectiveCourse("","","","","","","","","","","");
    private static List<ElectiveCourse> electiveCourseList = new ArrayList<>();

    public static List<ElectiveCourse> uploadFile(MultipartFile file) throws IOException {
        File f = GetPropertyValues.getResourcePath("ElectiveCourse", fileNameVar);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        System.out.println(fileNameMap);
        String mimeType = fileNameMap.getContentTypeFor(file.getName());
        System.out.println(mimeType);

        assert (mimeType).equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if (!f.exists()) {
            FileService.uploadFile(file, "ElectiveCourse", fileNameVar);
        } else {
            throw new IOException("File already exists. Try using PUT if you want to update it.");
        }

        try{
            electiveCourseList = getExcelContent();
        }catch (SQLException e){
            electiveCourseList.clear();
            electiveCourseList.add(defaultList);
            throw new IllegalArgumentException(e.getMessage());
        }finally {
            return electiveCourseList;
        }
    }

    public static List<ElectiveCourse> getExcelContent() throws IOException, SQLException {
            Workbook workbook = null;
            File f = GetPropertyValues.getResourcePath("ElectiveCourse", fileNameVar);
            if (f.exists()) {

                FileInputStream excelFile = new FileInputStream(f);
                workbook = new XSSFWorkbook(excelFile);

                Sheet worksheet = workbook.getSheetAt(0);

                DataFormatter formatter = new DataFormatter();
                List<ElectiveCourseDescription> electiveCourseDescriptionList = new ArrayList<>();
                //Create a loop to get the cell values of a row for one iteration
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getLastCellNum() < row.getPhysicalNumberOfCells()) continue;

                    String courseCode = formatter.formatCellValue(row.getCell(0));
                    String name = formatter.formatCellValue(row.getCell(1));
                    String period = formatter.formatCellValue(row.getCell(2));
                    String groupNumber = formatter.formatCellValue(row.getCell(3));
                    String teacher = formatter.formatCellValue(row.getCell(4));
                    String dayOfTheWeek = formatter.formatCellValue(row.getCell(5));
                    String startTime = formatter.formatCellValue(row.getCell(6));
                    String endTime = formatter.formatCellValue(row.getCell(7));
                    String location = formatter.formatCellValue(row.getCell(8));
                    String classroom = formatter.formatCellValue(row.getCell(9));
                    String description = "";

                    electiveCourseList.add(new ElectiveCourse(
                            courseCode,
                            name,
                            period,
                            groupNumber,
                            teacher,
                            dayOfTheWeek,
                            startTime,
                            endTime,
                            location,
                            classroom,
                            description
                    ));
                }
                excelFile.close();


                electiveCourseDescriptionList = getAllElectiveCourseDescription();
                for (ElectiveCourse electiveCourse : electiveCourseList){
                    for (ElectiveCourseDescription electiveCourseDescription : electiveCourseDescriptionList){
                        if (electiveCourse.getCourseCode().equals(electiveCourseDescription.getCourseCode())){
                            electiveCourse.setDescription(electiveCourseDescription.getDescription());
                        }
                    }
                }
            } else if (!f.exists()){
                throw new IOException("File does not exist.");
            } else {
                electiveCourseList.clear();
                electiveCourseList.add(defaultList);
                throw new IllegalArgumentException("Could not retrieve data from Database.");
            }
        return electiveCourseList;
    }

    public static List<ElectiveCourse> updateFile(MultipartFile file) throws IOException {
        File files = GetPropertyValues.getResourcePath("ElectiveCourse", fileNameVar);

        assert(files).equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if (files.length() <= 2) {
            for (File f : files) {
                if (f.exists() && !f.toString().contains(".gitkeep")) {
                    if (f.delete()) {
                        FileService.uploadFile(file, "ElectiveCourse", fileNameVar);
                    } else {
                        throw new IOException("File deletion failed");
                    }
                }
            }
        } else {
            throw new IOException("There are more than one files on the server. Try to fix that.");
        }
        try{
            electiveCourseList = getExcelContent();
        }catch (SQLException e){
            electiveCourseList.clear();
            electiveCourseList.add(defaultList);
            throw new IllegalArgumentException(e.getMessage());
        }
        return electiveCourseList;
    }

    public static ElectiveCourseDescription createElectiveCourseDescription(ElectiveCourseDescription electiveCourseDescription) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO elective_course (electivecoursecode, electivecoursename, description) VALUES (?,?,?);");
        pstmt.setString(1, electiveCourseDescription.getCourseCode());
        pstmt.setString(2, electiveCourseDescription.getName());
        pstmt.setString(3, electiveCourseDescription.getDescription());
        pstmt.executeUpdate();
        return getElectiveCourseDescription(electiveCourseDescription.getCourseCode());
    }

    public static List<ElectiveCourseDescription> getAllElectiveCourseDescription() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        List<ElectiveCourseDescription> allElectiveCourseDescriptions = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM elective_course");
        if (!result.next()) {
            throw new SQLException("No Elective Course description data in database");
        } else {
            do {
                allElectiveCourseDescriptions.add(getResult(result));
            } while (result.next());
            return allElectiveCourseDescriptions;
        }
    }

    public static ElectiveCourseDescription getElectiveCourseDescription(String courseCode) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM elective_course WHERE electivecoursecode=?;");
        pstmt.setString(1, courseCode);
        ResultSet result = pstmt.executeQuery();
        if (result.next()) {
            return getResult(result);
        } else {
            throw new SQLException("The Elective Course Description at courseCode " + courseCode + " doesn't exist");
        }
    }

    public static ElectiveCourseDescription getElectiveCourseDescriptionByName(String name) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM elective_course WHERE electivecoursename=?;");
        pstmt.setString(1, name);
        ResultSet result = pstmt.executeQuery();
        if (!result.next()) {
            throw new SQLException("No Description with electiveCourseName " + name + " found in the database");
        } else {
            return getResult(result);
        }
    }

    public static ElectiveCourseDescription updateElectiveCourseDescription(ElectiveCourseDescription electiveCourseDescription) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE elective_course SET electivecoursename =?, description=?, electivecoursecode=? WHERE electivecoursecode=?;");
        pstmt.setString(1, electiveCourseDescription.getName());
        pstmt.setString(2, electiveCourseDescription.getDescription());
        pstmt.setString(3, electiveCourseDescription.getCourseCode());
        pstmt.setString(4, electiveCourseDescription.getCourseCode());
        pstmt.executeUpdate();
        return getElectiveCourseDescription(electiveCourseDescription.getCourseCode());
    }

    public static ElectiveCourseDescription deleteElectiveCourseDescription(String courseCode) throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();
        ElectiveCourseDescription deletedElectiveCourseDescription = getElectiveCourseDescription(courseCode);
        PreparedStatement deletePstmt = conn.prepareStatement("DELETE FROM elective_course WHERE electivecoursecode =?;");
        deletePstmt.setString(1, courseCode);
        deletePstmt.executeUpdate();
        return deletedElectiveCourseDescription;
    }

    private static ElectiveCourseDescription getResult(ResultSet result) throws SQLException {
        String ElectiveCourseCode = result.getString("electivecoursecode");
        String ElectiveCourseName = result.getString("electivecoursename");
        String ElectiveCourseDescription = result.getString("description");
        return new ElectiveCourseDescription(ElectiveCourseCode, ElectiveCourseName, ElectiveCourseDescription);
    }
}
