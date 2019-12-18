import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ElectionCourseService } from '../election-courses.service';
import { ElectionCourse } from '../../shared/dataclasses';
@Component({
  selector: 'app-election-course-description',
  templateUrl: './election-course-description.component.html',
  styleUrls: ['./election-course-description.component.scss']
})
export class ElectionCourseDescriptionComponent implements OnInit {

  errorMessage = "";
  electionCourse: ElectionCourse = { 
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

  constructor(private service: ElectionCourseService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const code = params.get("code"),
        group = params.get("group");
      this.electionCourse.courseCode = code;
      this.electionCourse.groupNumber = group;

      this.service.getElectionCourses().subscribe(courses => {
        const course = courses.find(course => course.courseCode == code && course.groupNumber == group);
        if(typeof course != "undefined") this.electionCourse = course;
        else this.errorMessage = "Couldn't find election course";
      }, error => this.errorMessage = error.error.message);
    });
  }

}
