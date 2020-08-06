package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.exceptions.InternalServerException;
import com.bramgussekloo.projects.utilities.FileService;
import com.bramgussekloo.projects.utilities.GetPropertyValues;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@ApiModel(description = "Model for ElectiveCourse")
public class ElectiveCourse extends ElectiveCourseDescription{

    @ApiModelProperty(notes = "The period that the course take place in.", required = true)
    private String Period;

    @ApiModelProperty(notes = "Group numbers that are available for the course.", required = true)
    private String GroupNumber;

    @ApiModelProperty(notes = "Teacher code that is assigned to a group number.", required = false)
    private String Teacher;

    @ApiModelProperty(notes = "Day that the course take place: Tuesday or Thursday.", required = true)
    private String DayOfTheWeek;

    @ApiModelProperty(notes = "Start time of the Course for the group.", required = true)
    private String StartTime;

    @ApiModelProperty(notes = "End time of the course for the group.", required = true)
    private String EndTime;

    @ApiModelProperty(notes = "Location of Institute where the course will be given.", required = false)
    private String Location;

    @ApiModelProperty(notes = "Classroom. By default:look on Hint.", required = false)
    private String Classroom;

    public ElectiveCourse(String courseCode, String name, String period, String groupNumber, String teacher, String dayOfTheWeek, String startTime, String endTime, String location, String classroom, String description) {
        super(courseCode, name, description);
        Period = period;
        GroupNumber = groupNumber;
        Teacher = teacher;
        DayOfTheWeek = dayOfTheWeek;
        StartTime = startTime;
        EndTime = endTime;
        Location = location;
        Classroom = classroom;
    }

    // Empty object for initial run
    public ElectiveCourse() {
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        GroupNumber = groupNumber;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getDayOfTheWeek() {
        return DayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        DayOfTheWeek = dayOfTheWeek;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getClassroom() {
        return Classroom;
    }

    public void setClassroom(String classroom) {
        Classroom = classroom;
    }

    private static final String fileNameVar = "kv-lijst.xlsx";


    public static ArrayList<ElectiveCourse> uploadFile(MultipartFile file) throws Exception {
        ArrayList<ElectiveCourse> electiveCourseList;
        File f = GetPropertyValues.getResourcePath("ElectiveCourse", fileNameVar);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file.getName());

        assert (mimeType).equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if (!f.exists()) {
            FileService.uploadFile(file, "ElectiveCourse", fileNameVar);
            electiveCourseList = getExcelContent();
            return electiveCourseList;
        } else {
            throw new BadRequestException("File already exists. Try using PUT if you want to update it.");
        }
    }

    public static ArrayList<ElectiveCourse> deleteFile() throws Exception {
        File[] files = GetPropertyValues.getResourcePath("ElectiveCourse", "").listFiles();
        ArrayList<ElectiveCourse> electiveCourses;
        electiveCourses = getExcelContent();

        assert files != null;
        for (File f : files) {
            if (f.exists() && !f.toString().contains(".gitkeep")) {
                if (!f.delete()) {
                    throw new InternalServerException("Can't delete file.");
                }
            }
        }
        return electiveCourses;
    }


    public static ArrayList<ElectiveCourse> getExcelContent() throws Exception {
        Workbook workbook;
        ArrayList<ElectiveCourse> electiveCourseList = new ArrayList<>();
        File f = GetPropertyValues.getResourcePath("ElectiveCourse", fileNameVar);
        if (f.exists()) {

            FileInputStream excelFile = new FileInputStream(f);
            workbook = new XSSFWorkbook(excelFile);

            Sheet worksheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            List<ElectiveCourseDescription> ecdList = new ArrayList<>();
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


            ecdList = ElectiveCourseDescription.getAllFromDatabase();
            for (ElectiveCourse ec : electiveCourseList){
                for (ElectiveCourseDescription ecd : ecdList){
                    if (ec.getCourseCode().equals(ecd.getCourseCode())){
                        ec.setDescription(ecd.getDescription());
                    }
                }
            }
        } else {
            throw new BadRequestException("File does not exist.");
        }
        return electiveCourseList;
    }

    public static ArrayList<ElectiveCourse> updateFile(MultipartFile file) throws Exception {
        File[] files = GetPropertyValues.getResourcePath("ElectiveCourse", "").listFiles();
        ArrayList<ElectiveCourse> electiveCourses = new ArrayList<>();

        assert files != null;
        for (File f : files) {
            if (f.exists() && !f.toString().contains(".gitkeep")) {
                if (f.delete()) {
                    electiveCourses = uploadFile(file);
                } else {
                    throw new InternalServerException("File deletion failed");
                }
            } else {
                throw new BadRequestException("There is no file to delete");
            }
        }
        if (electiveCourses.isEmpty()){
            throw new BadRequestException("File is empty");
        } else {
            return electiveCourses;
        }
    }
}
