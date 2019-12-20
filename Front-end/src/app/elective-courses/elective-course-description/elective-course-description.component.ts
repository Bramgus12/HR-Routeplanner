import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ElectiveCourseService } from '../elective-courses.service';
import { ElectiveCourse } from '../../shared/dataclasses';
@Component({
  selector: 'app-elective-course-description',
  templateUrl: './elective-course-description.component.html',
  styleUrls: ['./elective-course-description.component.scss']
})
export class ElectiveCourseDescriptionComponent implements OnInit {

  errorMessage = "";
  electiveCourse: ElectiveCourse = { 
    courseCode: '',
    period: '',
    name: '',
    classroom: '',
    dayOfTheWeek: '',
    endTime: '',
    groupNumber: '',
    location: '',
    startTime: '',
    teacher: '',
    description: ''
  }

  constructor(private service: ElectiveCourseService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const code = params.get("code"),
        group = params.get("group");
      this.electiveCourse.courseCode = code;
      this.electiveCourse.groupNumber = group;

      this.service.getElectiveCourses().subscribe(courses => {
        const course = courses.find(course => course.courseCode == code && course.groupNumber == group);
        if(typeof course != "undefined") this.electiveCourse = course;
        else this.errorMessage = "Couldn't find election course";
      }, error => this.errorMessage = error.error.message);
    });
  }

}
