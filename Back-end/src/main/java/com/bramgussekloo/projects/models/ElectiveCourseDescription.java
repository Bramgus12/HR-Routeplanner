package com.bramgussekloo.projects.models;

import com.bramgussekloo.projects.exceptions.BadRequestException;
import com.bramgussekloo.projects.config.DatabaseConnection;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ElectiveCourseDescription {

    @ApiModelProperty(notes = "Course code.", required = true)
    private String CourseCode;

    @ApiModelProperty(notes = "Name of the course.", required = true)
    private String Name;

    @ApiModelProperty(notes = "Elective Course Description.", required = true)
    private String Description;

    public ElectiveCourseDescription(String courseCode, String courseName, String description) {
        CourseCode = courseCode;
        Name = courseName;
        Description = description;
    }

    public ElectiveCourseDescription() { }

    public ElectiveCourseDescription(String courseCode) {
        this.CourseCode = courseCode;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void createInDatabase() throws SQLException {
        Connection conn = new DatabaseConnection().getConnection();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO elective_course (electivecoursecode, electivecoursename, description) VALUES (?,?,?);");
        ps.setString(1, this.CourseCode);
        ps.setString(2, this.Name);
        ps.setString(3, this.Description);
        ps.executeUpdate();
    }

    /**
     * @return A list of all the electiveCourseDescription resources.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public static ArrayList<ElectiveCourseDescription> getAllFromDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        ArrayList<ElectiveCourseDescription> allElectiveCourseDescriptions = new ArrayList<>();
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM elective_course");
        if (!result.next()) {
            throw new BadRequestException("No Elective Course description data in database");
        } else {
            do {
                allElectiveCourseDescriptions.add(getResult(result));
            } while (result.next());
            return allElectiveCourseDescriptions;
        }
    }

    /**
     * This will override all the existing values in the object.
     *
     * @param courseCode The coursecode you want the resource of.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void getFromDatabase(String courseCode) throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM elective_course WHERE electivecoursecode=?;");
        ps.setString(1, courseCode);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            setResultInObject(result);
        } else {
            throw new BadRequestException("Resource doesn't exist");
        }
    }

    /**
     * Updates the electiveCourseDescription resource in the database.
     * @throws Exception Will be handled by the HandleExceptions class.
     */
    public void updateInDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE elective_course SET electivecoursename =?, description=? WHERE electivecoursecode=?;");
        ps.setString(1, Name);
        ps.setString(2, Description);
        ps.setString(3, CourseCode);
        ps.executeUpdate();
    }

    /**
     * Deletes the resource in the database based on the electiveCourseCode.
     *
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    public void deleteInDatabase() throws Exception {
        Connection conn = new DatabaseConnection().getConnection();
        PreparedStatement deletePstmt = conn.prepareStatement("DELETE FROM elective_course WHERE electivecoursecode =?;");
        deletePstmt.setString(1, this.CourseCode);
        deletePstmt.executeUpdate();
    }

    /**
     * @param result The resultSet you want the values of in the object.
     * @throws Exception Will be handled by the HandleExceptions class.
     */
    private void setResultInObject(ResultSet result) throws Exception {
        this.CourseCode = result.getString("electivecoursecode");
        this.Name = result.getString("electivecoursename");
        this.Description = result.getString("description");
    }

    /**
     * @param result The resultSet you want the object of.
     * @return An ElectiveCourseDescription object with the values of the ResultSet.
     * @throws Exception Will be handled by the HandleExceptions class.
     *
     * @see com.bramgussekloo.projects.exceptions.HandleExceptions
     */
    private static ElectiveCourseDescription getResult(ResultSet result) throws Exception {
        String ElectiveCourseCode = result.getString("electivecoursecode");
        String ElectiveCourseName = result.getString("electivecoursename");
        String ElectiveCourseDescription = result.getString("description");
        return new ElectiveCourseDescription(ElectiveCourseCode, ElectiveCourseName, ElectiveCourseDescription);
    }
}
