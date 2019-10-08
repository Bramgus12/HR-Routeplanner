package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.BuildingInstitute;
import com.bramgussekloo.projects.Statements.BuildingInstituteStatements;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/institute")
public class InstituteController {

    @GetMapping
    private ArrayList<BuildingInstitute> getInstituteList(){
        ArrayList<BuildingInstitute> list = BuildingInstituteStatements.getAllABuildingInstitutes();
        return list;
    }


}
