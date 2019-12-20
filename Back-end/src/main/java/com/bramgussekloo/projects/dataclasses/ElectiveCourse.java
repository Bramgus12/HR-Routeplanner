package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
}
