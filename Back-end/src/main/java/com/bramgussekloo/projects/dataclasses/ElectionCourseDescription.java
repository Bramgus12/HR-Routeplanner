package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModelProperty;

public class ElectionCourseDescription {

    @ApiModelProperty(notes = "Course code.", required = true)
    private String CourseCode;

    @ApiModelProperty(notes = "Name of the course.", required = true)
    private String Name;

    @ApiModelProperty(notes = "Election Course Description.", required = true)
    private String Description;

    public ElectionCourseDescription(String courseCode, String courseName, String description){
        CourseCode = courseCode;
        Name = courseName;
        Description = description;
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
}
