import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ElectionCourseService } from '../election-courses.service';
import { ElectionCourseExtended } from '../../shared/dataclasses';
@Component({
  selector: 'app-election-course-description',
  templateUrl: './election-course-description.component.html',
  styleUrls: ['./election-course-description.component.scss']
})
export class ElectionCourseDescriptionComponent implements OnInit {

  private code = "";

  errorMessage = "";
  electionCourse: ElectionCourseExtended = { 
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
      this.code = params.get("code");
      this.electionCourse.courseCode = this.code;

      this.service.getElectionCourse(this.code).subscribe(data => this.electionCourse = data,
        error => this.errorMessage = error.error.message
      );
    })
  }

}
